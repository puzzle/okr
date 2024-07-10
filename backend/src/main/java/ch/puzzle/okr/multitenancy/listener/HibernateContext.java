package ch.puzzle.okr.multitenancy.listener;

import org.springframework.core.env.ConfigurableEnvironment;

import java.util.Properties;

public class HibernateContext {
    public static final String HIBERNATE_CONNECTION_URL = "hibernate.connection.url";
    public static final String HIBERNATE_CONNECTION_USERNAME = "hibernate.connection.username";
    public static final String HIBERNATE_CONNECTION_PASSWORD = "hibernate.connection.password";
    public static final String HIBERNATE_MULTITENANCY = "hibernate.multiTenancy";

    public static String SPRING_DATASOURCE_URL = "spring.datasource.url";
    public static String SPRING_DATASOURCE_USERNAME = "spring.datasource.username";
    public static String SPRING_DATASOURCE_PASSWORD = "spring.datasource.password";

    public record DbConfig(String url, String username, String password, String multiTenancy) {
    }

    private static DbConfig cachedHibernateConfig;

    public static void setHibernateConfig(DbConfig dbConfig) {
        cachedHibernateConfig = dbConfig;
    }

    public static Properties getHibernateConfig() {
        return getConfigAsProperties(cachedHibernateConfig);
    }

    public static Properties getConfigAsProperties(DbConfig dbConfig) {
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

    public static void cacheHibernateProperties(ConfigurableEnvironment environment) {
        String url = environment.getProperty(HibernateContext.HIBERNATE_CONNECTION_URL);
        String username = environment.getProperty(HibernateContext.HIBERNATE_CONNECTION_USERNAME);
        String password = environment.getProperty(HibernateContext.HIBERNATE_CONNECTION_PASSWORD);
        String multiTenancy = environment.getProperty(HibernateContext.HIBERNATE_MULTITENANCY);

        HibernateContext.DbConfig h2DbConfig = new HibernateContext.DbConfig(url, username, password, multiTenancy);
        HibernateContext.setHibernateConfig(h2DbConfig);
    }

}
