package ch.puzzle.okr.security;

import static ch.puzzle.okr.Constants.USER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import ch.puzzle.okr.security.helper.ClaimHelper;
import ch.puzzle.okr.security.helper.TokenHelper;
import com.nimbusds.jwt.JWTClaimsSet;
import jakarta.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtHelper {
    public static final String CLAIM_TENANT = "tenant";
    public static final String CLAIM_ISS = "iss";
    public static final String ERROR_MESSAGE = "Missing `" + CLAIM_TENANT + "` and '" + CLAIM_ISS
                                               + "' claims in JWT token!";

    private static final Logger logger = LoggerFactory.getLogger(JwtHelper.class);

    private final TenantConfigProvider tenantConfigProvider;
    private final String firstName;
    private final String lastName;
    private final String email;

    public JwtHelper(TenantConfigProvider tenantConfigProvider,
                     @Value("${okr.jwt.claim.first-name}") final String tokenClaimsKeyFirstname,
                     @Value("${okr.jwt.claim.last-name}") final String tokenClaimsKeyLastname,
                     @Value("${okr.jwt.claim.email}") final String tokenClaimsKeyEmail) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.firstName = tokenClaimsKeyFirstname;
        this.lastName = tokenClaimsKeyLastname;
        this.email = tokenClaimsKeyEmail;
    }

    public User getUserFromJwt(Jwt token) {
        Map<String, Object> claims = token.getClaims();
        logger.debug("claims {}", claims);

        try {
            return User.Builder
                    .builder() //
                    .withFirstName(claims.get(firstName).toString()) //
                    .withLastName(claims.get(lastName).toString()) //
                    .withEmail(claims.get(email).toString()) //
                    .build();
        } catch (Exception e) {
            logger.warn("can not convert user from claims {}", claims);
            throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.CONVERT_TOKEN, USER);
        }
    }

    public String getTenantFromToken(Jwt token) {
        TokenHelper helper = new TokenHelper();
        List<Function<Jwt, Optional<String>>> getTenantFromTokenFunctions = Arrays
                .asList( //
                        helper::getTenantFromTokenUsingClaimIss, //
                        helper::getTenantFromTokenUsingClaimTenant //
                );

        return getFirstMatchingTenantUsingListOfHelperFunctions(token, getTenantFromTokenFunctions);
    }

    private String getFirstMatchingTenantUsingListOfHelperFunctions(Jwt token,
                                                                    List<Function<Jwt, Optional<String>>> getTenantFunctions) {

        return getTenantFunctions
                .stream() //
                .map(func -> func.apply(token)) //
                .filter(Optional::isPresent) //
                .map(Optional::get) //
                .map(this::getMatchingTenantFromConfigOrThrow) //
                .findFirst() //
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    public String getTenantFromJWTClaimsSet(JWTClaimsSet claimSet) {
        ClaimHelper helper = new ClaimHelper();
        List<Function<JWTClaimsSet, Optional<String>>> getTenantFromClaimsSetFunctions = Arrays
                .asList( //
                        helper::getTenantFromClaimsSetUsingClaimIss, //
                        helper::getTenantFromClaimsSetUsingClaimTenant //
                );

        return getFirstMatchingTenantUsingListOfHelperFunctions(claimSet, getTenantFromClaimsSetFunctions);
    }

    private String getFirstMatchingTenantUsingListOfHelperFunctions(JWTClaimsSet claimSet,
                                                                    List<Function<JWTClaimsSet, Optional<String>>> getTenantFunctions) {

        return getTenantFunctions
                .stream() //
                .map(func -> func.apply(claimSet)) //
                .filter(Optional::isPresent) //
                .map(Optional::get) //
                .map(this::getMatchingTenantFromConfigOrThrow)
                .findFirst() //
                .orElseThrow(() -> new RuntimeException(ERROR_MESSAGE));
    }

    private String getMatchingTenantFromConfigOrThrow(String tenant) {
        // Ensure we return only tenants for realms which really exist
        return this.tenantConfigProvider
                .getTenantConfigById(tenant)
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Cannot find tenant {0}", tenant)))
                .tenantId();
    }

}