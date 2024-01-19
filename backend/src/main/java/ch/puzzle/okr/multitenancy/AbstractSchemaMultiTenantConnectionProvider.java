package ch.puzzle.okr.multitenancy;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.*;

public abstract class AbstractSchemaMultiTenantConnectionProvider extends AbstractMultiTenantConnectionProvider {
    private final Map<String, ConnectionProvider> connectionProviderMap;

    public AbstractSchemaMultiTenantConnectionProvider() {
        this.connectionProviderMap = new HashMap<>();
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = super.getConnection(tenantIdentifier);

        String schema = Objects.equals(tenantIdentifier, "public") ? tenantIdentifier : MessageFormat.format("okr_{0}", tenantIdentifier);

        connection.createStatement().execute(String.format("SET SCHEMA '%s';", schema));

        return connection;
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return getConnectionProvider(TenantContext.DEFAULT_TENANT_ID);
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return getConnectionProvider(tenantIdentifier);
    }

    private ConnectionProvider getConnectionProvider(String tenantIdentifier) {
        return Optional.ofNullable(tenantIdentifier)
                .map(connectionProviderMap::get)
                .orElseGet(() -> createNewConnectionProvider(tenantIdentifier));
    }

    private ConnectionProvider createNewConnectionProvider(String tenantIdentifier) {
        return Optional.ofNullable(tenantIdentifier)
                .map(this::createConnectionProvider)
                .map(connectionProvider -> {
                    connectionProviderMap.put(tenantIdentifier, connectionProvider);
                    return connectionProvider;
                })
                .orElseThrow(() -> new ConnectionProviderException(String.format("Cannot create new connection provider for tenant: %s", tenantIdentifier)));
    }

    private ConnectionProvider createConnectionProvider(String tenantIdentifier) {
        return Optional.ofNullable(tenantIdentifier)
                .map(this::getHibernatePropertiesForTenantId)
                .map(this::initConnectionProvider)
                .orElse(null);
    }

    private Properties getHibernatePropertiesForTenantId(String tenantId) {
        try {
            Properties properties = new Properties();
            properties.load(getClass().getResourceAsStream(this.getHibernatePropertiesFilePaths()));
            if(!Objects.equals(tenantId, "public")) {
                Object put = properties.put(AvailableSettings.DEFAULT_SCHEMA, MessageFormat.format("okr_{0}", tenantId));
            }
            return properties;
        } catch (IOException e) {
            throw new RuntimeException(String.format("Cannot open hibernate properties: %s)", this.getHibernatePropertiesFilePaths()));
        }
    }

    private ConnectionProvider initConnectionProvider(Properties hibernateProperties) {
        Map<String, Object> configProperties = new HashMap<>();
        for (String key : hibernateProperties.stringPropertyNames()) {
            String value = hibernateProperties.getProperty(key);
            configProperties.put(key, value);
        }

        DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl();
        connectionProvider.configure(configProperties);
        return connectionProvider;
    }

    protected abstract String getHibernatePropertiesFilePaths();
}