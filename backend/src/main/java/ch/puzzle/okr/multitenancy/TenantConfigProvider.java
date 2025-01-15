package ch.puzzle.okr.multitenancy;

import static java.text.MessageFormat.format;

import java.text.MessageFormat;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * Reads the configuration of the tenants (as TenantConfig objects) from the
 * applicationX.properties and caches each TenantConfig in the TenantConfigs
 * class.
 */
@Component
public class TenantConfigProvider implements TenantConfigProviderInterface {
    private static final String EMAIL_DELIMITER = ",";
    private final Map<String, TenantConfig> tenantConfigs = new HashMap<>();
    private final Environment env;

    private enum DbType {
        BOOTSTRAP,
        APP,
        FLY;

        public String nameUsedInProperties() {
            return this.name().toLowerCase();
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(TenantConfigProvider.class);

    public TenantConfigProvider(final @Value("${okr.tenant-ids}")
    String[] tenantIds, Environment env) {
        this.env = env;
        for (String tenantId : tenantIds) {
            OauthConfig c = readOauthConfig(tenantId);
            TenantConfig tenantConfig = createTenantConfig(c.jwkSetUri(),
                                                           c.frontendClientIssuerUrl(),
                                                           c.frontendClientId(),
                                                           tenantId);

            tenantConfigs.put(tenantId, tenantConfig);
            cacheTenantConfig(tenantId, tenantConfig); // cache tenantConfig for Hibernate connections
        }
    }

    private void cacheTenantConfig(String tenantId, TenantConfig tenantConfig) {
        TenantConfigs.add(tenantId, tenantConfig);
        logCachingTenantConfig(tenantId, tenantConfig);
    }

    private void logCachingTenantConfig(String tenantId, TenantConfig tenantConfig) {
        logger
                .info("cache TenantConfig: tenantId={}, users={}", //
                      tenantId, //
                      tenantConfig.dataSourceConfigFlyway().name() + " | " + tenantConfig.dataSourceConfigApp().name());
    }

    public static TenantConfigProvider.TenantConfig getCachedTenantConfig(String tenantId) {
        return TenantConfigs.get(tenantId);
    }

    // for tests
    public static void clearTenantConfigsCache() {
        TenantConfigs.clear();
    }

    private OauthConfig readOauthConfig(String tenantId) {
        return new OauthConfig( //
                               getProperty("okr.tenants.{0}.security.oauth2.resourceserver.jwt.jwk-set-uri", tenantId),
                               getProperty("okr.tenants.{0}.security.oauth2.frontend.issuer-url", tenantId),
                               getProperty("okr.tenants.{0}.security.oauth2.frontend.client-id", tenantId));
    }

    private TenantConfig createTenantConfig(String jwkSetUriTemplate, String frontendClientIssuerUrl,
                                            String frontendClientId, String tenantId) {

        return new TenantConfig(tenantId,
                                getOkrChampionEmailsFromTenant(tenantId),
                                jwkSetUriTemplate, //
                                frontendClientIssuerUrl,
                                frontendClientId, //
                                this.readDataSourceConfigFlyway(tenantId), //
                                this.readDataSourceConfigApp(tenantId));
    }

    private String[] getOkrChampionEmailsFromTenant(String tenantId) {
        return Arrays
                .stream(getProperty("okr.tenants.{0}.user.champion.emails", tenantId, "").split(EMAIL_DELIMITER))
                .map(String::trim)
                .toArray(String[]::new);
    }

    public List<TenantConfig> getTenantConfigs() {
        return this.tenantConfigs.values().stream().toList();
    }

    private DataSourceConfig readDataSourceConfigFlyway(String tenantId) {
        return readDataSourceConfig(tenantId, DbType.FLY);
    }

    private DataSourceConfig readDataSourceConfigApp(String tenantId) {
        return readDataSourceConfig(tenantId, DbType.APP);
    }

    private DataSourceConfig readDataSourceConfig(String tenantId, DbType dbType) {
        return new DataSourceConfig( //
                                    getProperty("okr.datasource.driver-class-name"),
                                    getProperty("okr.tenants.{0}.datasource.url", tenantId),
                                    getProperty("okr.tenants.{0}.datasource.username." + dbType.nameUsedInProperties(),
                                                tenantId),
                                    getProperty("okr.tenants.{0}.datasource.password." + dbType.nameUsedInProperties(),
                                                tenantId),
                                    getProperty("okr.tenants.{0}.datasource.schema", tenantId));
    }

    private String getProperty(String key) {
        return env.getProperty(String.format(key));
    }

    private String getProperty(String key, String tenantId) {
        return env.getProperty(MessageFormat.format(key, tenantId));
    }

    private String getProperty(String key, String tenantId, String defaultValue) {
        return env.getProperty(format(key, tenantId), defaultValue);
    }

    public Optional<TenantConfig> getTenantConfigById(String tenantId) {
        return Optional.ofNullable(this.tenantConfigs.get(tenantId));
    }

    public Optional<String> getJwkSetUri(String tenantId) {
        return getTenantConfigById(tenantId).map(TenantConfig::jwkSetUri);
    }

    public record TenantConfig(String tenantId, String[] okrChampionEmails, String jwkSetUri, String issuerUrl,
            String clientId, DataSourceConfig dataSourceConfigFlyway, DataSourceConfig dataSourceConfigApp) {

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TenantConfig that)) {
                return false;
            }
            return Objects.equals(tenantId(), that.tenantId()) && Objects.equals(clientId(), that.clientId())
                   && Objects.equals(jwkSetUri(), that.jwkSetUri()) && Objects.equals(issuerUrl(), that.issuerUrl())
                   && Objects.deepEquals(okrChampionEmails(), that.okrChampionEmails())
                   && Objects.equals(dataSourceConfigFlyway(), that.dataSourceConfigFlyway())
                   && Objects.equals(dataSourceConfigApp(), that.dataSourceConfigApp());
        }

        @Override
        public int hashCode() {
            return Objects
                    .hash(tenantId(),
                          Arrays.hashCode(okrChampionEmails()),
                          jwkSetUri(),
                          issuerUrl(),
                          clientId(),
                          dataSourceConfigFlyway(),
                          dataSourceConfigApp());
        }

        @Override
        public String toString() {
            return "TenantConfig{" + "tenantId='" + tenantId + '\'' + ", okrChampionEmails="
                   + Arrays.toString(okrChampionEmails) + ", jwkSetUri='" + jwkSetUri + '\'' + ", issuerUrl='"
                   + issuerUrl + '\'' + ", clientId='" + clientId + '\'' + ", dataSourceConfigFlyway="
                   + dataSourceConfigFlyway + ", dataSourceConfigApp=" + dataSourceConfigApp + '}';
        }
    }

    public record DataSourceConfig(String driverClassName, String url, String name, String password, String schema) {

    }

    public record OauthConfig(String jwkSetUri, String frontendClientIssuerUrl, String frontendClientId) {
    }
}