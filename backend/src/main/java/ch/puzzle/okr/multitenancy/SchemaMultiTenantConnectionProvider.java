package ch.puzzle.okr.multitenancy;

import static ch.puzzle.okr.multitenancy.TenantContext.DEFAULT_TENANT_ID;

import ch.puzzle.okr.exception.ConnectionProviderException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.*;
import org.hibernate.cfg.MappingSettings;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.hibernate.hikaricp.internal.HikariCPConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SchemaMultiTenantConnectionProvider extends AbstractMultiTenantConnectionProvider<String> {

    private static final Logger logger = LoggerFactory.getLogger(SchemaMultiTenantConnectionProvider.class);

    final transient Map<String, ConnectionProvider> connectionProviderMap;

    public SchemaMultiTenantConnectionProvider() {
        this.connectionProviderMap = new HashMap<>();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = super.getConnection(tenantIdentifier);
        return getConnection(tenantIdentifier, connection);
    }

    protected Connection getConnection(String tenantIdentifier, Connection connection) throws SQLException {
        String schema = convertTenantIdToSchemaName(tenantIdentifier);
        logger.debug("Setting schema to {}", schema);
        try (Statement sqlStatement = connection.createStatement()) {
            sqlStatement.execute(String.format("SET SCHEMA '%s';", schema));
        }

        return connection;
    }

    private String convertTenantIdToSchemaName(String tenantIdentifier) {
        return Objects.equals(tenantIdentifier, DEFAULT_TENANT_ID) ? tenantIdentifier
                : MessageFormat.format("okr_{0}", tenantIdentifier);
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return getConnectionProvider(DEFAULT_TENANT_ID);
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return getConnectionProvider(tenantIdentifier);
    }

    protected ConnectionProvider getConnectionProvider(String tenantIdentifier) {
        return Optional
                .ofNullable(tenantIdentifier) //
                .map(connectionProviderMap::get) //
                .orElseGet(() -> createNewConnectionProvider(tenantIdentifier));
    }

    private ConnectionProvider createNewConnectionProvider(String tenantIdentifier) {
        return Optional
                .ofNullable(tenantIdentifier) //
                .map(this::createConnectionProvider) //
                .map(connectionProvider -> {
                    connectionProviderMap.put(tenantIdentifier, connectionProvider);
                    return connectionProvider;
                }) //
                .orElseThrow(() -> new ConnectionProviderException(String
                        .format("Cannot create new connection provider for tenant: %s", tenantIdentifier)));
    }

    private ConnectionProvider createConnectionProvider(String tenantIdentifier) {
        return Optional
                .ofNullable(tenantIdentifier) //
                .map(this::getCombinedProperties) //
                .map(this::initConnectionProvider) //
                .orElse(null);
    }

    protected Properties getCombinedProperties(String tenantIdentifier) {
        Properties hibernateProperties = getPropertiesForTenantIdentifier(tenantIdentifier, getHibernateProperties(), "hibernate");
        Properties hikariProperties = getPropertiesForTenantIdentifier(tenantIdentifier, getHikariProperties(), "hikari");
        Properties combinedProperties = new Properties();
        combinedProperties.putAll(hibernateProperties);
        combinedProperties.putAll(hikariProperties);
        return combinedProperties;
    }

    protected Properties getPropertiesForTenantIdentifier(String tenantIdentifier,  Properties properties, String propertiesName) {
        if (properties == null || properties.isEmpty()) {
            throw new ConnectionProviderException(MessageFormat.format("Cannot load {0} properties from application.properties)", propertiesName));
        }
        if (!Objects.equals(tenantIdentifier, DEFAULT_TENANT_ID)) {
            properties.put(MappingSettings.DEFAULT_SCHEMA, MessageFormat.format("okr_{0}", tenantIdentifier));
        }
        return properties;
    }

    private ConnectionProvider initConnectionProvider(Properties properties) {
        Map<String, Object> configProperties = convertPropertiesToMap(properties);
        HikariCPConnectionProvider connectionProvider = getDriverManagerConnectionProviderImpl();
        connectionProvider.configure(configProperties);
        return connectionProvider;
    }

    protected HikariCPConnectionProvider getDriverManagerConnectionProviderImpl() {
        return new HikariCPConnectionProvider();
    }

    private Map<String, Object> convertPropertiesToMap(Properties properties) {
        Map<String, Object> configProperties = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            configProperties.put(key, value);
        }
        return configProperties;
    }

    protected Properties getHibernateProperties() {
        return HibernateContext.getHibernateConfig();
    }

    protected Properties getHikariProperties() {
        return HikariContext.getHikariConfig();
    }

}