package ch.puzzle.okr.security;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Map;

import static ch.puzzle.okr.Constants.USER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class JwtHelper {
    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    private final TenantConfigProvider tenantConfigProvider;
    private final String username;
    private final String firstname;
    private final String lastname;
    private final String email;

    public JwtHelper(
            TenantConfigProvider tenantConfigProvider,
            @Value("${okr.jwt.claim.username}") final String username,
            @Value("${okr.jwt.claim.firstname}") final String firstname,
            @Value("${okr.jwt.claim.lastname}") final String lastname,
            @Value("${okr.jwt.claim.email}") final String email
    ) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.username = username;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
    }

    public String getTenantFromIssuer(String issuer) {
        final String tenantId = issuer.split("/realms/")[1];

        // Ensure we return only tenantIds for realms which really exist
        return this.tenantConfigProvider
                .getTenantConfigById(tenantId)
                .orElseThrow(
                        () -> new EntityNotFoundException(MessageFormat.format("Cannot find tenant for issuer{0}", issuer)))
                .tenantId();
    }

    public User getUserFormJwt(Jwt token) {
        Map<String, Object> claims = token.getClaims();
        logger.debug("claims {}", claims);

        try {
            return User.Builder.builder()
                    .withUsername(claims.get(username).toString())
                    .withFirstname(claims.get(firstname).toString())
                    .withLastname(claims.get(lastname).toString())
                    .withEmail(claims.get(email).toString()).build();
        } catch (Exception e) {
            logger.warn("can not convert user from claims {}", claims);
            throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.CONVERT_TOKEN, USER);
        }
    }
}