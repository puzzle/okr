package ch.puzzle.okr.security.helper;

import static ch.puzzle.okr.security.JwtHelper.CLAIM_ISS;
import static ch.puzzle.okr.security.JwtHelper.CLAIM_TENANT;
import static ch.puzzle.okr.security.helper.JwtStatusLogger.logStatus;
import static ch.puzzle.okr.security.helper.UrlHelper.extractTenantFromIssUrl;

import java.util.Optional;
import org.springframework.security.oauth2.jwt.Jwt;

public class TokenHelper {

    public Optional<String> getTenantFromTokenUsingClaimTenant(Jwt token) {
        return getTenant(token);
    }

    private Optional<String> getTenant(Jwt token) {
        String tenant = token.getClaimAsString(CLAIM_TENANT); // can return null
        logStatus(CLAIM_TENANT, token, tenant);
        return Optional.ofNullable(tenant);
    }

    public Optional<String> getTenantFromTokenUsingClaimIss(Jwt token) {
        return getIssUrl(token).flatMap(url -> getTenant(token, url));
    }

    private Optional<String> getIssUrl(Jwt token) {
        String issUrl = token.getClaimAsString(CLAIM_ISS); // can return null
        logStatus(CLAIM_ISS, token, issUrl);
        return Optional.ofNullable(issUrl);
    }

    private Optional<String> getTenant(Jwt token, String issUrl) {
        Optional<String> tenant = extractTenantFromIssUrl(issUrl);
        logStatus(CLAIM_ISS, token, tenant.isPresent());
        return tenant;
    }
}
