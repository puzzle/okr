package ch.puzzle.okr;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TenantConfigProvider {
    final String jwkSetUriTemplate;
    private final Map<String, TenantConfig> tenantConfigs = new HashMap<>();

    public TenantConfigProvider(
            final @Value("${okr.tenant-ids}") String[] tenantIds,
            final @Value("${okr.security.oauth2.resourceserver.jwt.jwk-set-uri-template}") String jwkSetUriTemplate,
            final @Value("${okr.security.oauth2.frontend.issuer-url-template}") String frontendClientIssuerUrl,
            final @Value("${okr.security.oauth2.frontend.client-id-template}") String frontendClientId
    ) {
        this.jwkSetUriTemplate = jwkSetUriTemplate;
        for (String tenantId : tenantIds) {
            tenantConfigs.put(
                    tenantId,
                    new TenantConfig(
                            tenantId,
                            jwkSetUriTemplate.replace("{tenantid}", tenantId),
                            frontendClientIssuerUrl.replace("{tenantid}", tenantId),
                            frontendClientId.replace("{tenantid}", tenantId)
                    )
            );
        }
    }

    public Optional<TenantConfig> getTenantConfigById(String tenantId) {
        return Optional.ofNullable(this.tenantConfigs.get(tenantId));
    }

    public Optional<String> getJwkSetUri(String tenantId) {
        return getTenantConfigById(tenantId).map(TenantConfig::jwkSetUri);
    }

    public Optional<String> getTenantById(String tenantId) {
        return getTenantConfigById(tenantId).map(TenantConfig::tenantId);
    }

    public record TenantConfig(
            String tenantId,
            String jwkSetUri,
            String issuerUrl,
            String clientId
    ) {
    }
}