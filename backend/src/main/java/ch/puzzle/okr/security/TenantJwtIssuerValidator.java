package ch.puzzle.okr.security;

import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtIssuerValidator;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class TenantJwtIssuerValidator implements OAuth2TokenValidator<Jwt> {
    private final TenantConfigProvider tenantConfigProvider;

    private final Map<String, JwtIssuerValidator> validators = new ConcurrentHashMap<>();

    private final JwtHelper jwtHelper;

    public TenantJwtIssuerValidator(TenantConfigProvider tenantConfigProvider, JwtHelper jwtHelper) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.jwtHelper = jwtHelper;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        return this.validators.computeIfAbsent(toTenant(token), this::fromTenant) //
                .validate(token);
    }

    private String toTenant(Jwt jwt) {
        return jwtHelper.getTenantFromToken(jwt);
    }

    private JwtIssuerValidator fromTenant(String tenant) {
        return this.tenantConfigProvider.getTenantConfigById(tenant) //
                .map(TenantConfigProvider.TenantConfig::issuerUrl) //
                .map(this::createValidator) //
                .orElseThrow(() -> new IllegalArgumentException("unknown tenant"));
    }

    JwtIssuerValidator createValidator(String issuer) {
        return new JwtIssuerValidator(issuer);
    }

}