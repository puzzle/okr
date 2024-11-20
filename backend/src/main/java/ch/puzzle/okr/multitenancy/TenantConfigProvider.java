package ch.puzzle.okr.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.*;

@Component
public class TenantConfigProvider implements TenantConfigProviderInterface {
    private static final String EMAIL_DELIMITER = ",";
    private final Map<String, TenantConfig> tenantConfigs = new HashMap<>();
    private final Environment env;

    private enum DbType {
        bootstrap, app, fly
    }

    private static final Logger logger = LoggerFactory.getLogger(TenantConfigProvider.class);

    public TenantConfigProvider(final @Value("${okr.tenant-ids}") String[] tenantIds, Environment env) {
        this.env = env;
        for (String tenantId : tenantIds) {
            OauthConfig c = readOauthConfig(tenantId);
            TenantConfig tenantConfig = createTenantConfig(c.jwkSetUri(), c.frontendClientIssuerUrl(),
                    c.frontendClientId(), tenantId);

            tenantConfigs.put(tenantId, tenantConfig);
            cacheTenantConfig(tenantId, tenantConfig); // cache tenantConfig for Hibernate connections
        }
    }

    private void cacheTenantConfig(String tenantId, TenantConfig tenantConfig) {
        logger.error("cacheTenantConfig: tenantId={}, tenantConfig={}", tenantId, tenantConfig);
        TenantConfigs.add(tenantId, tenantConfig);
    }

    public static TenantConfigProvider.TenantConfig getCachedTenantConfig(String tenantId) {
        return TenantConfigs.get(tenantId);
    }

    // for tests
    public static void clearTenantConfigsCache() {
        TenantConfigs.clear();
    }

    private OauthConfig readOauthConfig(String tenantId) {
        return new OauthConfig(
                env.getProperty(MessageFormat.format("okr.tenants.{0}.security.oauth2.resourceserver.jwt.jwk-set-uri",
                        tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.security.oauth2.frontend.issuer-url", tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.security.oauth2.frontend.client-id", tenantId)));
    }

    private TenantConfig createTenantConfig(String jwkSetUriTemplate, String frontendClientIssuerUrl,
            String frontendClientId, String tenantId) {

        return new TenantConfig(tenantId, getOkrChampionEmailsFromTenant(tenantId), jwkSetUriTemplate, //
                frontendClientIssuerUrl, frontendClientId, //
                this.readDataSourceConfigFlyway(tenantId), //
                this.readDataSourceConfigApp(tenantId));
    }

    private String[] getOkrChampionEmailsFromTenant(String tenantId) {
        return Arrays.stream(env.getProperty(MessageFormat.format("okr.tenants.{0}.user.champion.emails", tenantId), "")
                .split(EMAIL_DELIMITER)).map(String::trim).toArray(String[]::new);
    }

    public List<TenantConfig> getTenantConfigs() {
        return this.tenantConfigs.values().stream().toList();
    }

    private DataSourceConfig readDataSourceConfigFlyway(String tenantId) {
        return readDataSourceConfig(tenantId, DbType.fly);
    }

    private DataSourceConfig readDataSourceConfigApp(String tenantId) {
        return readDataSourceConfig(tenantId, DbType.app);
    }

    private DataSourceConfig readDataSourceConfig(String tenantId, DbType dbType) {
        return new DataSourceConfig(env.getProperty("okr.datasource.driver-class-name"),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.datasource.url", tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.datasource.username." + dbType, tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.datasource.password." + dbType, tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.datasource.schema", tenantId)));
    }

    public Optional<TenantConfig> getTenantConfigById(String tenantId) {
        return Optional.ofNullable(this.tenantConfigs.get(tenantId));
    }

    public Optional<String> getJwkSetUri(String tenantId) {
        return getTenantConfigById(tenantId).map(TenantConfig::jwkSetUri);
    }

    public record TenantConfig(String tenantId, String[] okrChampionEmails, String jwkSetUri, String issuerUrl,
            String clientId, DataSourceConfig dataSourceConfigFlyway, DataSourceConfig dataSourceConfigApp) {
    }

    public record DataSourceConfig(String driverClassName, String url, String name, String password, String schema) {

    }

    public record OauthConfig(String jwkSetUri, String frontendClientIssuerUrl, String frontendClientId) {
    }
}