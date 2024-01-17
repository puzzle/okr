package ch.puzzle.okr.multitenancy;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Component
public class TenantConfigProvider {
    final String jwkSetUriTemplate;
    private final Map<String, TenantConfig> tenantConfigs = new HashMap<>();
    private final Environment env;

    public TenantConfigProvider(final @Value("${okr.tenant-ids}") String[] tenantIds,
            final @Value("${okr.security.oauth2.resourceserver.jwt.jwk-set-uri-template}") String jwkSetUriTemplate,
            final @Value("${okr.security.oauth2.frontend.issuer-url-template}") String frontendClientIssuerUrl,
            final @Value("${okr.security.oauth2.frontend.client-id-template}") String frontendClientId,
            Environment env) {
        this.jwkSetUriTemplate = jwkSetUriTemplate;
        this.env = env;

        for (String tenantId : tenantIds) {
            tenantConfigs.put(tenantId,
                    new TenantConfig(tenantId, jwkSetUriTemplate.replace("{tenantid}", tenantId),
                            frontendClientIssuerUrl.replace("{tenantid}", tenantId),
                            frontendClientId.replace("{tenantid}", tenantId), this.readDataSourceConfig(tenantId)));
        }
    }

    public List<TenantConfig> getTenantConfigs() {
        return this.tenantConfigs.values().stream().toList();
    }

    private DataSourceConfig readDataSourceConfig(String tenantId) {
        return new DataSourceConfig(env.getProperty(MessageFormat.format("okr.datasource.driver-class-name", tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.datasource.url", tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.datasource.username", tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.datasource.password", tenantId)),
                env.getProperty(MessageFormat.format("okr-{0}", tenantId)));
    }

    public Optional<TenantConfig> getTenantConfigById(String tenantId) {
        return Optional.ofNullable(this.tenantConfigs.get(tenantId));
    }

    public Optional<String> getJwkSetUri(String tenantId) {
        return getTenantConfigById(tenantId).map(TenantConfig::jwkSetUri);
    }

    public record TenantConfig(String tenantId, String jwkSetUri, String issuerUrl, String clientId,
            DataSourceConfig dataSourceConfig) {
    }

    public record DataSourceConfig(String driverClassName, String url, String name, String password, String schema) {

    }
}