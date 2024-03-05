package ch.puzzle.okr.security;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.text.ParseException;
import java.util.Map;

import static ch.puzzle.okr.Constants.USER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class JwtHelper {
    private static final String CLAIM_TENANT = "tenant";

    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    private final TenantConfigProvider tenantConfigProvider;
    private final String firstname;
    private final String lastname;
    private final String email;

    public JwtHelper(TenantConfigProvider tenantConfigProvider,
            @Value("${okr.jwt.claim.firstname}") final String firstname,
            @Value("${okr.jwt.claim.lastname}") final String lastname,
            @Value("${okr.jwt.claim.email}") final String email) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public User getUserFromJwt(Jwt token) {
        Map<String, Object> claims = token.getClaims();
        logger.debug("claims {}", claims);

        try {
            return User.Builder.builder().withFirstname(claims.get(firstname).toString())
                    .withLastname(claims.get(lastname).toString()).withEmail(claims.get(email).toString()).build();
        } catch (Exception e) {
            logger.warn("can not convert user from claims {}", claims);
            throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.CONVERT_TOKEN, USER);
        }
    }

    public String getTenantFromToken(Jwt token) {
        return getTenantOrThrow(token.getClaimAsString(CLAIM_TENANT));
    }

    private String getTenantOrThrow(String tenant) {
        // Ensure we return only tenants for realms which really exist
        return this.tenantConfigProvider.getTenantConfigById(tenant)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cannot find tenant {0}", tenant)))
                .tenantId();
    }

    public String getTenantFromJWTClaimsSet(JWTClaimsSet claimSet) {
        try {
            return this.getTenantOrThrow(claimSet.getStringClaim(CLAIM_TENANT));
        } catch (ParseException e) {
            throw new RuntimeException("Missing `tenant` claim in JWT token!", e);
        }

    }
}