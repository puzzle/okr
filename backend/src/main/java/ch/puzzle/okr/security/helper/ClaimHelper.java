package ch.puzzle.okr.security.helper;

import com.nimbusds.jwt.JWTClaimsSet;

import java.text.ParseException;
import java.util.Optional;

import static ch.puzzle.okr.security.JwtHelper.CLAIM_ISS;
import static ch.puzzle.okr.security.JwtHelper.CLAIM_TENANT;
import static ch.puzzle.okr.security.helper.JwtStatusLogger.logStatus;
import static ch.puzzle.okr.security.helper.UrlHelper.extractTenantFromIssUrl;

public class ClaimHelper {

    public Optional<String> getTenantFromClaimsSetUsingClaimTenant(JWTClaimsSet claimSet) {
        try {
            String tenant = getTenant(claimSet);
            return Optional.ofNullable(tenant);
        } catch (ParseException e) {
            logStatus(CLAIM_TENANT, claimSet, e);
            return Optional.empty();
        }
    }

    private static String getTenant(JWTClaimsSet claimSet) throws ParseException {
        String tenant = claimSet.getStringClaim(CLAIM_TENANT);
        logStatus(CLAIM_TENANT, claimSet, tenant);
        return tenant;
    }

    public Optional<String> getTenantFromClaimsSetUsingClaimIss(JWTClaimsSet claimSet) {
        try {
            String issUrl = getIssUrl(claimSet);
            if (issUrl == null) {
                return Optional.empty();
            }
            return getTenant(claimSet, issUrl);
        } catch (ParseException e) {
            logStatus(CLAIM_ISS, claimSet, e);
            return Optional.empty();
        }
    }

    private static String getIssUrl(JWTClaimsSet claimSet) throws ParseException {
        String issUrl = claimSet.getStringClaim(CLAIM_ISS);
        logStatus(CLAIM_ISS, claimSet, issUrl);
        return issUrl;
    }

    private static Optional<String> getTenant(JWTClaimsSet claimSet, String issUrl) {
        String tenant = extractTenantFromIssUrl(issUrl);
        logStatus(CLAIM_ISS, claimSet, tenant);
        return Optional.ofNullable(tenant);
    }

}
