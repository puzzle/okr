package ch.puzzle.okr.multitenancy;

import java.text.MessageFormat;
import java.util.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TenantConfigProvider implements TenantConfigProviderInterface {
    private static final String EMAIL_DELIMITER = ",";
    private final Map<String, TenantConfig> tenantConfigs = new HashMap<>();
    private final Environment env;

    public TenantConfigProvider(final @Value("${okr.tenant-ids}")
    String[] tenantIds, Environment env) {
        this.env = env;
        for (String tenantId : tenantIds) {
            OauthConfig c = readOauthConfig(tenantId);
            tenantConfigs
                    .put(tenantId,
                         createTenantConfig(c.jwkSetUri(),
                                            c.frontendClientIssuerUrl(),
                                            c.frontendClientId(),
                                            tenantId));
        }
    }

    private OauthConfig readOauthConfig(String tenantId) {
        return new OauthConfig(env
                .getProperty(MessageFormat
                        .format("okr.tenants.{0}.security.oauth2.resourceserver.jwt.jwk-set-uri", tenantId)),
                               env
                                       .getProperty(MessageFormat
                                               .format("okr.tenants.{0}.security.oauth2.frontend.issuer-url",
                                                       tenantId)),
                               env
                                       .getProperty(MessageFormat
                                               .format("okr.tenants.{0}.security.oauth2.frontend.client-id",
                                                       tenantId)));
    }

    private TenantConfig createTenantConfig(String jwkSetUriTemplate, String frontendClientIssuerUrl,
                                            String frontendClientId, String tenantId) {
        return new TenantConfig(tenantId,
                                getOkrChampionEmailsFromTenant(tenantId),
                                jwkSetUriTemplate,
                                frontendClientIssuerUrl,
                                frontendClientId,
                                this.readDataSourceConfig(tenantId));
    }

    private String[] getOkrChampionEmailsFromTenant(String tenantId) {
        return Arrays
                .stream(env
                        .getProperty(MessageFormat.format("okr.tenants.{0}.user.champion.emails", tenantId), "")
                        .split(EMAIL_DELIMITER))
                .map(String::trim)
                .toArray(String[]::new);
    }

    public List<TenantConfig> getTenantConfigs() {
        return this.tenantConfigs.values().stream().toList();
    }

    private DataSourceConfig readDataSourceConfig(String tenantId) {
        return new DataSourceConfig(env.getProperty("okr.datasource.driver-class-name"),
                                    env.getProperty(MessageFormat.format("okr.tenants.{0}.datasource.url", tenantId)),
                                    env
                                            .getProperty(MessageFormat
                                                    .format("okr.tenants.{0}.datasource.username", tenantId)),
                                    env
                                            .getProperty(MessageFormat
                                                    .format("okr.tenants.{0}.datasource.password", tenantId)),
                                    env
                                            .getProperty(MessageFormat
                                                    .format("okr.tenants.{0}.datasource.schema", tenantId)));
    }

    public Optional<TenantConfig> getTenantConfigById(String tenantId) {
        return Optional.ofNullable(this.tenantConfigs.get(tenantId));
    }

    public Optional<String> getJwkSetUri(String tenantId) {
        return getTenantConfigById(tenantId).map(TenantConfig::jwkSetUri);
    }

    public List<String> getAllTenantIds() {
        return new ArrayList<>(this.tenantConfigs.keySet());
    }

    public record TenantConfig(String tenantId, String[] okrChampionEmails, String jwkSetUri, String issuerUrl,
            String clientId, DataSourceConfig dataSourceConfig) {

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof TenantConfig that)) {
                return false;
            }
            return Objects.equals(tenantId(), that.tenantId()) && Objects.equals(clientId(), that.clientId())
                   && Objects.equals(jwkSetUri(), that.jwkSetUri()) && Objects.equals(issuerUrl(), that.issuerUrl())
                   && Objects.deepEquals(okrChampionEmails(), that.okrChampionEmails())
                   && Objects.equals(dataSourceConfig(), that.dataSourceConfig());
        }

        @Override
        public int hashCode() {
            return Objects
                    .hash(tenantId(),
                          Arrays.hashCode(okrChampionEmails()),
                          jwkSetUri(),
                          issuerUrl(),
                          clientId(),
                          dataSourceConfig());
        }

        @Override
        public String toString() {
            return "TenantConfig{" + "tenantId='" + tenantId + '\'' + ", okrChampionEmails="
                   + Arrays.toString(okrChampionEmails) + ", jwkSetUri='" + jwkSetUri + '\'' + ", issuerUrl='"
                   + issuerUrl + '\'' + ", clientId='" + clientId + '\'' + ", dataSourceConfig=" + dataSourceConfig
                   + '}';
        }
    }

    public record DataSourceConfig(String driverClassName, String url, String name, String password, String schema) {

    }

    public record OauthConfig(String jwkSetUri, String frontendClientIssuerUrl, String frontendClientId) {
    }
}