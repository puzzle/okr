package ch.puzzle.okr.multitenancy;

import static ch.puzzle.okr.multitenancy.TenantContext.DEFAULT_TENANT_ID;

import ch.puzzle.okr.exception.ConnectionProviderException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.MessageFormat;
import java.util.*;
import org.hibernate.cfg.MappingSettings;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The central piece of code of multitenancy.
 *
 * <pre>
 * getConnection(tenantId) sets in each tenant request the specific db schema for the
 * tenant. This guarantees that each tenant always works in its own DB schema.
 *
 * getConnection(tenantId) -> Connection calls in the abstract super class the
 * getConnection(tenantId) -> Connection which calls the abstract
 * selectConnectionProvider(tenantIdentifier) -> ConnectionProvider which is implemented
 * in SchemaMultiTenantConnectionProvider.
 * </pre>
 *
 * <pre>
 * Some coding details:
 *
 * selectConnectionProvider(tenantId) -> ConnectionProvider returns for a tenant a
 * ConnectionProvider. It first checks if the ConnectionProvider for the tenant is already
 * cached (in connectionProviderMap). If the ConnectionProvider is cached, it returns it.
 * Otherwise it creates a ConnectionProvider for the tenant, cache it and return it.
 *
 * To create a ConnectionProvider for the tenant, it tries to load the configuration from
 * the hibernate properties. For this it uses 2 methods of HibernateContext:
 * getHibernateConfig() if the tenant is the DEFAULT_TENANT_ID (public) and
 * getHibernateConfig(tenantId) for all other tenants. With this information its then
 * possible to create and cache a ConnectionProvider for the tenant. If no matching
 * hibernate properties are found, then an exception is thrown.
 * </pre>
 */
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

    Connection getConnection(String tenantIdentifier, Connection connection) throws SQLException {
        String schema = convertTenantIdToSchemaName(tenantIdentifier);
        logger.debug("Setting schema to {}", schema);

        try (Statement sqlStatement = connection.createStatement()) {
            sqlStatement.execute(String.format("SET SCHEMA '%s';", schema));
        }

        return connection;
    }

    String convertTenantIdToSchemaName(String tenantIdentifier) {
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

    ConnectionProvider getConnectionProvider(String tenantIdentifier) {
        return Optional
                .ofNullable(tenantIdentifier) //
                .map(connectionProviderMap::get) //
                .orElseGet(() -> createAndCacheNewConnectionProvider(tenantIdentifier));
    }

    ConnectionProvider createAndCacheNewConnectionProvider(String tenantIdentifier) {
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
                .map(this::getHibernatePropertiesForTenantIdentifier) //
                .map(this::initConnectionProvider) //
                .orElse(null);
    }

    Properties getHibernatePropertiesForTenantIdentifier(String tenantIdentifier) {
        Properties properties = getHibernateProperties(tenantIdentifier);
        if (properties.isEmpty()) {
            throw new ConnectionProviderException("Cannot load hibernate properties from application.properties");
        }
        if (!Objects.equals(tenantIdentifier, DEFAULT_TENANT_ID)) {
            properties.put(MappingSettings.DEFAULT_SCHEMA, MessageFormat.format("okr_{0}", tenantIdentifier));
        }
        return properties;
    }

    ConnectionProvider initConnectionProvider(Properties hibernateProperties) {
        Map<String, Object> configProperties = convertPropertiesToMap(hibernateProperties);
        DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl();
        connectionProvider.configure(configProperties);
        return connectionProvider;
    }

    Map<String, Object> convertPropertiesToMap(Properties properties) {
        Map<String, Object> configProperties = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            configProperties.put(key, value);
        }
        return configProperties;
    }

    private Properties getHibernateProperties(String tenantIdentifier) {
        if (tenantIdentifier == null) {
            throw new ConnectionProviderException("No hibernate configuration found for tenant: " + tenantIdentifier);
        }
        try {
            if (tenantIdentifier.equals(DEFAULT_TENANT_ID)) {
                return HibernateContext.getHibernateConfig();
            }
            return HibernateContext.getHibernateConfig(tenantIdentifier);
        } catch (RuntimeException e) {
            throw new ConnectionProviderException(e.getMessage());
        }
    }
}