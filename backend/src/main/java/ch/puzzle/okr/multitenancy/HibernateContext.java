package ch.puzzle.okr.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ch.puzzle.okr.exception.HibernateContextException;
import java.util.Properties;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * Reads the (not tenant specific) hibernate configuration form the "hibernate.x" properties in the
 * applicationX.properties file. It then caches the configuration as DbConfig object. The data from the DbConfig object
 * is used by the SchemaMultiTenantConnectionProvider via getHibernateConfig() and getHibernateConfig(tenantId).
 *
 * <pre>
 * getHibernateConfig() returns the cached DbConfig as properties.
 * </pre>
 *
 * <pre>
 * getHibernateConfig(tenantId) patches the DbConfig data with tenant specific data (from
 * TenantConfigProvider) and returns the patched data as properties
 * </pre>
 */
public class HibernateContext {
    public static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    public static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
    public static final String HIBERNATE_MULTITENANCY = "hibernate.multi-tenancy";

    public static final String SPRING_DATASOURCE_URL = "spring.datasource.url";
    public static final String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
    public static final String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";

    private static final Logger logger = LoggerFactory.getLogger(HibernateContext.class);

    public record DbConfig(String url, String username, String password, String multiTenancy) {

        public boolean isValid() {
            return !hasNullValues() && !hasEmptyValues();
        }

        private boolean hasNullValues() {
            return url() == null || username() == null || password() == null || multiTenancy() == null;
        }

        private boolean hasEmptyValues() {
            return url().isBlank() || username().isBlank() || password().isBlank() || multiTenancy().isBlank();
        }
    }

    // general (not tenant specific) hibernate config
    private static DbConfig cachedHibernateConfig;

    public static void extractAndSetHibernateConfig(ConfigurableEnvironment environment) {
        DbConfig dbConfig = extractHibernateConfig(environment);
        setHibernateConfig(dbConfig);
        logUsedHibernateConfig(dbConfig);
    }

    public static void setHibernateConfig(DbConfig dbConfig) {
        if (dbConfig == null || !dbConfig.isValid()) {
            throw new HibernateContextException("Invalid hibernate configuration " + dbConfig);
        }
        cachedHibernateConfig = dbConfig;
    }

    private static DbConfig extractHibernateConfig(ConfigurableEnvironment environment) {
        String url = environment.getProperty(HibernateContext.HIBERNATE_CONNECTION_URL);
        String username = environment.getProperty(HibernateContext.HIBERNATE_CONNECTION_USERNAME);
        String password = environment.getProperty(HibernateContext.HIBERNATE_CONNECTION_PASSWORD);
        String multiTenancy = environment.getProperty(HibernateContext.HIBERNATE_MULTITENANCY);
        return new DbConfig(url, username, password, multiTenancy);
    }

    // for testing
    public static void resetHibernateConfig() {
        cachedHibernateConfig = null;
    }

    public static Properties getHibernateConfig() {
        if (cachedHibernateConfig == null) {
            throw new HibernateContextException("No cached hibernate configuration found");
        }
        var config = getConfigAsProperties(cachedHibernateConfig);
        logUsedHibernateConfig(config);
        return config;
    }

    private static Properties getConfigAsProperties(DbConfig dbConfig) {
        Properties properties = new Properties();
        properties.put(HibernateContext.HIBERNATE_CONNECTION_URL, dbConfig.url());
        properties.put(HibernateContext.HIBERNATE_CONNECTION_USERNAME, dbConfig.username());
        properties.put(HibernateContext.HIBERNATE_CONNECTION_PASSWORD, dbConfig.password());
        properties.put(HibernateContext.HIBERNATE_MULTITENANCY, dbConfig.multiTenancy());
        properties.put(HibernateContext.SPRING_DATASOURCE_URL, dbConfig.url());
        properties.put(HibernateContext.SPRING_DATASOURCE_USERNAME, dbConfig.username());
        properties.put(HibernateContext.SPRING_DATASOURCE_PASSWORD, dbConfig.password());
        return properties;
    }

    public static Properties getHibernateConfig(String tenantIdentifier) {
        if (cachedHibernateConfig == null) {
            throw new RuntimeException("No cached hibernate configuration found (for tenant " + tenantIdentifier + ")");
        }
        var config = getConfigAsPropertiesAndPatch(cachedHibernateConfig, tenantIdentifier);
        logUsedHibernateConfig(tenantIdentifier, config);
        return config;
    }

    private static Properties getConfigAsPropertiesAndPatch(DbConfig dbConfig, String tenantIdentifier) {
        Properties properties = getConfigAsProperties(dbConfig);
        return patchConfigAppForTenant(properties, tenantIdentifier);
    }

    private static Properties patchConfigAppForTenant(Properties properties, String tenantIdentifier) {
        TenantConfigProvider.TenantConfig cachedTenantConfig = TenantConfigProvider
                .getCachedTenantConfig(tenantIdentifier);
        if (cachedTenantConfig == null) {
            throw new RuntimeException("No cached tenant configuration found (for tenant " + tenantIdentifier + ")");
        }

        TenantConfigProvider.DataSourceConfig dataSourceConfigApp = cachedTenantConfig.dataSourceConfigApp();
        properties.put(HibernateContext.HIBERNATE_CONNECTION_USERNAME, dataSourceConfigApp.name());
        properties.put(HibernateContext.HIBERNATE_CONNECTION_PASSWORD, dataSourceConfigApp.password());
        properties.put(HibernateContext.SPRING_DATASOURCE_USERNAME, dataSourceConfigApp.name());
        properties.put(HibernateContext.SPRING_DATASOURCE_PASSWORD, dataSourceConfigApp.password());
        return properties;
    }

    private static void logUsedHibernateConfig(DbConfig hibernateConfig) {
        logger.info("set DbConfig: user={}", hibernateConfig.username());
    }

    private static void logUsedHibernateConfig(Properties hibernateConfig) {
        logger.info("use DbConfig: user={}",
                hibernateConfig.getProperty(HibernateContext.HIBERNATE_CONNECTION_USERNAME)); //
    }

    private static void logUsedHibernateConfig(String tenantId, Properties hibernateConfig) {
        logger.info("use DbConfig: tenant={} user={}", tenantId,
                hibernateConfig.getProperty(HibernateContext.HIBERNATE_CONNECTION_USERNAME));
    }

}
