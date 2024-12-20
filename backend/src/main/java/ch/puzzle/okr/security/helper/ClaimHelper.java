package ch.puzzle.okr.security.helper;

import static ch.puzzle.okr.security.JwtHelper.CLAIM_ISS;
import static ch.puzzle.okr.security.JwtHelper.CLAIM_TENANT;
import static ch.puzzle.okr.security.helper.JwtStatusLogger.logStatus;
import static ch.puzzle.okr.security.helper.UrlHelper.extractTenantFromIssUrl;

import com.nimbusds.jwt.JWTClaimsSet;
import java.text.ParseException;
import java.util.Optional;

public class ClaimHelper {

    public Optional<String> getTenantFromClaimsSetUsingClaimTenant(JWTClaimsSet claimSet) {
        try {
            return getTenant(claimSet);
        } catch (ParseException e) {
            logStatus(CLAIM_TENANT, claimSet, e);
            return Optional.empty();
        }
    }

    private Optional<String> getTenant(JWTClaimsSet claimSet) throws ParseException {
        String tenant = claimSet.getStringClaim(CLAIM_TENANT);
        logStatus(CLAIM_TENANT, claimSet, tenant);
        return Optional.ofNullable(tenant);
    }

    public Optional<String> getTenantFromClaimsSetUsingClaimIss(JWTClaimsSet claimSet) {
        try {
            return getIssUrl(claimSet).flatMap(url -> getTenant(claimSet, url));
        } catch (ParseException e) {
            logStatus(CLAIM_ISS, claimSet, e);
            return Optional.empty();
        }
    }

    private Optional<String> getIssUrl(JWTClaimsSet claimSet) throws ParseException {
        String issUrl = claimSet.getStringClaim(CLAIM_ISS);
        logStatus(CLAIM_ISS, claimSet, issUrl);
        return Optional.ofNullable(issUrl);
    }

    private Optional<String> getTenant(JWTClaimsSet claimSet, String issUrl) {
        Optional<String> tenant = extractTenantFromIssUrl(issUrl);
        logStatus(CLAIM_ISS, claimSet, tenant.isPresent());
        return tenant;
    }

}
