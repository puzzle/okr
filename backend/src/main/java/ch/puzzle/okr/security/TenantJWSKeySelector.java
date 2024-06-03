package ch.puzzle.okr.security;

import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.KeySourceException;
import com.nimbusds.jose.proc.JWSAlgorithmFamilyJWSKeySelector;
import com.nimbusds.jose.proc.JWSKeySelector;
import com.nimbusds.jose.proc.SecurityContext;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.proc.JWTClaimsSetAwareJWSKeySelector;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.security.Key;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantJWSKeySelector implements JWTClaimsSetAwareJWSKeySelector<SecurityContext> {

    private final TenantConfigProvider tenantConfigProvider;

    private final Map<String, JWSKeySelector<SecurityContext>> selectors = new ConcurrentHashMap<>();
    private final JwtHelper jwtHelper;

    public TenantJWSKeySelector(final TenantConfigProvider tenantConfigProvider, JwtHelper jwtHelper) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public List<? extends Key> selectKeys(JWSHeader jwsHeader, JWTClaimsSet jwtClaimsSet,
            SecurityContext securityContext) throws KeySourceException {
        return this.selectors.computeIfAbsent(toTenant(jwtClaimsSet), this::fromTenant).selectJWSKeys(jwsHeader,
                securityContext);
    }

    private String toTenant(JWTClaimsSet claimSet) {
        return jwtHelper.getTenantFromJWTClaimsSet(claimSet);
    }

    private JWSKeySelector<SecurityContext> fromTenant(String tenantId) {
        return this.tenantConfigProvider.getJwkSetUri(tenantId).map(this::fromUri)
                .orElseThrow(() -> new IllegalArgumentException("unknown tenant"));
    }

    JWSKeySelector<SecurityContext> fromUri(String uri) {
        try {
            return JWSAlgorithmFamilyJWSKeySelector.fromJWKSetURL(new URL(uri));
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}