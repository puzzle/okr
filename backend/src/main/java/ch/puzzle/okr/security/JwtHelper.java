package ch.puzzle.okr.security;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import ch.puzzle.okr.security.helper.ClaimHelper;
import ch.puzzle.okr.security.helper.TokenHelper;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;
import java.util.Optional;

import static ch.puzzle.okr.Constants.USER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class JwtHelper {
    public static final String CLAIM_TENANT = "tenant";
    public static final String CLAIM_ISS = "iss";

    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    private final TenantConfigProvider tenantConfigProvider;
    private final String firstname;
    private final String lastname;
    private final String email;

    public JwtHelper(TenantConfigProvider tenantConfigProvider,
            @Value("${okr.jwt.claim.firstname}") final String tokenClaimsKeyFirstname,
            @Value("${okr.jwt.claim.lastname}") final String tokenClaimsKeyLastname,
            @Value("${okr.jwt.claim.email}") final String tokenClaimsKeyEmail) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.firstname = tokenClaimsKeyFirstname;
        this.lastname = tokenClaimsKeyLastname;
        this.email = tokenClaimsKeyEmail;
    }

    public User getUserFromJwt(Jwt token) {
        Map<String, Object> claims = token.getClaims();
        logger.debug("claims {}", claims);

        try {
            return User.Builder.builder() //
                    .withFirstname(claims.get(firstname).toString()) //
                    .withLastname(claims.get(lastname).toString()) //
                    .withEmail(claims.get(email).toString()) //
                    .build();
        } catch (Exception e) {
            logger.warn("can not convert user from claims {}", claims);
            throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.CONVERT_TOKEN, USER);
        }
    }

    public String getTenantFromToken(Jwt token) {
        TokenHelper helper = new TokenHelper();

        Optional<String> tenantUsingClaimIss = helper.getTenantFromTokenUsingClaimIss(token);
        if (tenantUsingClaimIss.isPresent()) {
            return getMatchingTenantFromConfigOrThrow(tenantUsingClaimIss.get());
        }

        Optional<String> tenantUsingClaimTenant = helper.getTenantFromTokenUsingClaimTenant(token);
        if (tenantUsingClaimTenant.isPresent()) {
            return getMatchingTenantFromConfigOrThrow(tenantUsingClaimTenant.get());
        }

        logErrorAndThrowException(CLAIM_TENANT, CLAIM_ISS);
        return null; // only to make the compiler happy
    }

    public String getTenantFromJWTClaimsSet(JWTClaimsSet claimSet) {
        ClaimHelper helper = new ClaimHelper();

        Optional<String> tenantUsingClaimIss = helper.getTenantFromClaimsSetUsingClaimIss(claimSet);
        if (tenantUsingClaimIss.isPresent()) {
            return getMatchingTenantFromConfigOrThrow(tenantUsingClaimIss.get());
        }

        Optional<String> tenantUsingClaimTenant = helper.getTenantFromClaimsSetUsingClaimTenant(claimSet);
        if (tenantUsingClaimTenant.isPresent()) {
            return getMatchingTenantFromConfigOrThrow(tenantUsingClaimTenant.get());
        }

        logErrorAndThrowException(CLAIM_TENANT, CLAIM_ISS);
        return null; // only to make the compiler happy
    }

    private String getMatchingTenantFromConfigOrThrow(String tenant) {
        // Ensure we return only tenants for realms which really exist
        return this.tenantConfigProvider.getTenantConfigById(tenant)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cannot find tenant {0}", tenant)))
                .tenantId();
    }

    private void logErrorAndThrowException(String tenant, String iss) throws RuntimeException {
        String errorInfo = "* Missing `" + tenant + "` and '" + iss + "' claims in JWT token!";
        logger.error(errorInfo);
        throw new RuntimeException(errorInfo);
    }

}