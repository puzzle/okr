package ch.puzzle.okr.multitenancy;

import ch.puzzle.okr.exception.HibernateContextException;
import java.util.Properties;
import org.springframework.core.env.ConfigurableEnvironment;

public class HikariContext {
    public static final String HIKARI_MAXIMUM_POOL_SIZE = "hibernate.hikari.maximumPoolSize";

    public record DbConfig(String maximumPoolSize) {

        public boolean isValid() {
            return !hasNullValues() && !hasEmptyValues();
        }

        private boolean hasNullValues() {
            return maximumPoolSize() == null;
        }

        private boolean hasEmptyValues() {
            return maximumPoolSize().isBlank();
        }
    }

    private static DbConfig cachedHibernateConfig;

    public static void setHikariConfig(DbConfig dbConfig) {
        if (dbConfig == null || !dbConfig.isValid()) {
            throw new HibernateContextException("Invalid hikari configuration " + dbConfig);
        }
        cachedHibernateConfig = dbConfig;
    }

    public static void extractAndSetHikariConfig(ConfigurableEnvironment environment) {
        DbConfig dbConfig = extractHikariConfig(environment);
        HikariContext.setHikariConfig(dbConfig);
    }

    private static DbConfig extractHikariConfig(ConfigurableEnvironment environment) {
        String maximumPoolSize = environment.getProperty(HikariContext.HIKARI_MAXIMUM_POOL_SIZE);
        return new DbConfig(maximumPoolSize);
    }

    // for testing
    public static void resetHikariConfig() {
        cachedHibernateConfig = null;
    }

    public static Properties getHikariConfig() {
        if (cachedHibernateConfig == null) {
            throw new HibernateContextException("No cached hibernate configuration found");
        }
        return getConfigAsProperties(cachedHibernateConfig);
    }

    private static Properties getConfigAsProperties(DbConfig dbConfig) {
        Properties properties = new Properties();
        properties.put(HikariContext.HIKARI_MAXIMUM_POOL_SIZE, dbConfig.maximumPoolSize());
        return properties;
    }
}
