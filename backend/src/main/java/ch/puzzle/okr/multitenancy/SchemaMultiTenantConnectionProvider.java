package ch.puzzle.okr.multitenancy;

import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


@Component
public class SchemaMultiTenantConnectionProvider extends AbstractMultiTenantConnectionProvider {

    private final ConnectionProvider connectionProvider;

    public SchemaMultiTenantConnectionProvider() throws IOException {
        connectionProvider = initConnectionProvider();
    }

    @Override
    protected ConnectionProvider getAnyConnectionProvider() {
        return connectionProvider;
    }

    @Override
    protected ConnectionProvider selectConnectionProvider(String tenantIdentifier) {
        return connectionProvider;
    }

    @Override
    public Connection getConnection(String tenantIdentifier) throws SQLException {
        Connection connection = super.getConnection(tenantIdentifier);
        connection.createStatement()
                .execute(String.format("SET SCHEMA %s;", tenantIdentifier));
        return connection;
    }

    private ConnectionProvider initConnectionProvider() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/hibernate-multitenancy.properties"));
        Map<String, Object> configProperties = new HashMap<>();
        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            configProperties.put(key, value);
        }

        DriverManagerConnectionProviderImpl connectionProvider = new DriverManagerConnectionProviderImpl();
        connectionProvider.configure(configProperties);
        return connectionProvider;
    }

    @Override
    public void releaseConnection(String tenantIdentifier, Connection connection)
            throws SQLException {
        connection.setSchema("okr_pitc");
        releaseAnyConnection(connection);
    }
    
}