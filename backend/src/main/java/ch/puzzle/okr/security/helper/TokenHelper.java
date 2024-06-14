package ch.puzzle.okr.security.helper;

import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Optional;

import static ch.puzzle.okr.security.JwtHelper.CLAIM_ISS;
import static ch.puzzle.okr.security.JwtHelper.CLAIM_TENANT;
import static ch.puzzle.okr.security.helper.JwtStatusLogger.logStatus;
import static ch.puzzle.okr.security.helper.UrlHelper.extractTenantFromIssUrl;

public class TokenHelper {

    public Optional<String> getTenantFromTokenUsingClaimTenant(Jwt token) {
        String tenant = getTenant(token);
        return Optional.ofNullable(tenant);
    }

    private static String getTenant(Jwt token) {
        String tenant = token.getClaimAsString(CLAIM_TENANT); // can return null
        logStatus(CLAIM_TENANT, token, tenant);
        return tenant;
    }

    public Optional<String> getTenantFromTokenUsingClaimIss(Jwt token) {
        String issUrl = getIssUrl(token);
        if (issUrl == null) {
            return Optional.empty();
        }
        return getTenant(token, issUrl);
    }

    private String getIssUrl(Jwt token) {
        String issUrl = token.getClaimAsString(CLAIM_ISS); // can return null
        logStatus(CLAIM_ISS, token, issUrl);
        return issUrl;
    }

    private Optional<String> getTenant(Jwt token, String issUrl) {
        String tenant = extractTenantFromIssUrl(issUrl);
        logStatus(CLAIM_ISS, token, tenant);
        return Optional.ofNullable(tenant);
    }
}
