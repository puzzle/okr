package ch.puzzle.okr.multitenancy;

import ch.puzzle.okr.exception.HikariContextException;
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

    private static DbConfig cachedHikariConfig;

    public static void setHikariConfig(DbConfig dbConfig) {
        cachedHikariConfig = dbConfig;
    }

    public static void extractAndSetHikariConfig(ConfigurableEnvironment environment) {
        DbConfig dbConfig = extractHikariConfig(environment);
        if (!dbConfig.isValid()) {
            throw new HikariContextException("Invalid hikari configuration " + dbConfig);
        }
        HikariContext.setHikariConfig(dbConfig);
    }

    private static DbConfig extractHikariConfig(ConfigurableEnvironment environment) {
        String maximumPoolSize = environment.getProperty(HikariContext.HIKARI_MAXIMUM_POOL_SIZE);
        return new DbConfig(maximumPoolSize);
    }

    public static Properties getHikariConfig() {
        // Assert non null due to prevent null pointer in method getConfigAsProperties()
        if (cachedHikariConfig == null || !cachedHikariConfig.isValid()) {
            throw new HikariContextException("No cached hikari configuration found");
        }
        return getConfigAsProperties(cachedHikariConfig);
    }

    private static Properties getConfigAsProperties(DbConfig dbConfig) {
        Properties properties = new Properties();
        properties.put(HikariContext.HIKARI_MAXIMUM_POOL_SIZE, dbConfig.maximumPoolSize());
        return properties;
    }
}