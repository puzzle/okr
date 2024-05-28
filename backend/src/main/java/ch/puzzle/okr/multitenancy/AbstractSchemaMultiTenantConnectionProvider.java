package ch.puzzle.okr.multitenancy;

import ch.puzzle.okr.exception.ConnectionProviderException;
import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

import static ch.puzzle.okr.multitenancy.TenantContext.DEFAULT_TENANT_ID;

public abstract class AbstractSchemaMultiTenantConnectionProvider
        extends AbstractMultiTenantConnectionProvider<String> {
    private static final Logger logger = LoggerFactory.getLogger(AbstractSchemaMultiTenantConnectionProvider.class);

    final Map<String, ConnectionProvider> connectionProviderMap;

    public AbstractSchemaMultiTenantConnectionProvider() {
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

        connection.createStatement().execute(String.format("SET SCHEMA '%s';", schema));
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
        return Optional.ofNullable(tenantIdentifier) //
                .map(connectionProviderMap::get) //
                .orElseGet(() -> createNewConnectionProvider(tenantIdentifier));
    }

    private ConnectionProvider createNewConnectionProvider(String tenantIdentifier) {
        return Optional.ofNullable(tenantIdentifier) //
                .map(this::createConnectionProvider) //
                .map(connectionProvider -> {
                    connectionProviderMap.put(tenantIdentifier, connectionProvider);
                    return connectionProvider;
                }) //
                .orElseThrow(() -> new ConnectionProviderException(
                        String.format("Cannot create new connection provider for tenant: %s", tenantIdentifier)));
    }

    private ConnectionProvider createConnectionProvider(String tenantIdentifier) {
        return Optional.ofNullable(tenantIdentifier) //
                .map(this::getHibernatePropertiesForTenantIdentifier) //
                .map(this::initConnectionProvider) //
                .orElse(null);
    }

    protected Properties getHibernatePropertiesForTenantIdentifier(String tenantIdentifier) {
        try {
            Properties properties = getPropertiesFromFilePaths();
            if (!Objects.equals(tenantIdentifier, DEFAULT_TENANT_ID)) {
                properties.put(AvailableSettings.DEFAULT_SCHEMA, MessageFormat.format("okr_{0}", tenantIdentifier));
            }
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(
                    String.format("Cannot open hibernate properties: %s)", this.getHibernatePropertiesFilePaths()));
        }
    }

    protected Properties getPropertiesFromFilePaths() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream(this.getHibernatePropertiesFilePaths()));
        return properties;
    }

    private ConnectionProvider initConnectionProvider(Properties hibernateProperties) {
        Map<String, Object> configProperties = convertPropertiesToMap(hibernateProperties);
        DriverManagerConnectionProviderImpl connectionProvider = getDriverManagerConnectionProviderImpl();
        connectionProvider.configure(configProperties);
        return connectionProvider;
    }

    protected DriverManagerConnectionProviderImpl getDriverManagerConnectionProviderImpl() {
        return new DriverManagerConnectionProviderImpl();
    }

    private Map<String, Object> convertPropertiesToMap(Properties properties) {
        Map<String, Object> configProperties = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            configProperties.put(key, value);
        }
        return configProperties;
    }

    protected abstract String getHibernatePropertiesFilePaths();
}