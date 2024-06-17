package ch.puzzle.okr.multitenancy;

import ch.puzzle.okr.exception.ConnectionProviderException;
import org.hibernate.engine.jdbc.connections.internal.DriverManagerConnectionProviderImpl;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Properties;

import static org.mockito.Mockito.mock;

public class SchemaMultiTenantConnectionProviderInternalsTest {

    private static final String TENANT_ID = "pitc";

    private static class BasicConnectionProviderMock extends AbstractSchemaMultiTenantConnectionProvider {
        @Override
        protected String getHibernatePropertiesFilePaths() {
            return null;
        }

        @Override
        protected DriverManagerConnectionProviderImpl getDriverManagerConnectionProviderImpl() {
            return mock(DriverManagerConnectionProviderImpl.class);
        }
    }

    private static class ConfigurableConnectionProviderMock extends BasicConnectionProviderMock {

        @Override
        protected Properties getPropertiesFromFilePaths() {
            Properties properties = new Properties();
            properties.put("hibernate.connection.url", "no_value");
            return properties;
        }

        public void registerProvider(String tenantIdentifier, ConnectionProvider connectionProvider) {
            connectionProviderMap.put(tenantIdentifier, connectionProvider);
        }
    }

    @DisplayName("getConnectionProvider() return ConnectionProvider if TenantId is registered")
    @Test
    void getConnectionProviderReturnConnectionProviderIfTenantIdIsRegistered() {
        // arrange
        ConfigurableConnectionProviderMock mockProvider = new ConfigurableConnectionProviderMock();
        mockProvider.registerProvider(TENANT_ID, mock(ConnectionProvider.class));

        // act
        ConnectionProvider foundConnectionProvider = mockProvider.getConnectionProvider(TENANT_ID);

        // assert
        Assertions.assertNotNull(foundConnectionProvider);
    }

    @DisplayName("getConnectionProvider() creates ConnectionProvider if TenantId is not registered")
    @Test
    void getConnectionProviderCreatesConnectionProviderIfTenantIdIsNotRegistered() {
        // arrange
        ConfigurableConnectionProviderMock mockProvider = new ConfigurableConnectionProviderMock();

        // act
        ConnectionProvider notFoundConnectionProvider = mockProvider.getConnectionProvider(TENANT_ID);

        // assert
        Assertions.assertNotNull(notFoundConnectionProvider);
    }

    @DisplayName("getConnectionProvider() throws Exception when lookup TenantId is null")
    @Test
    void getConnectionProviderThrowsExceptionWhenLookupTenantIdIsNull() {
        // arrange
        ConfigurableConnectionProviderMock mockProvider = new ConfigurableConnectionProviderMock();

        // act + assert
        Assertions.assertThrows(ConnectionProviderException.class, () -> mockProvider.getConnectionProvider(null));
    }

    @DisplayName("selectConnectionProvider() return ConnectionProvider if TenantId is registered")
    @Test
    void selectConnectionProviderReturnConnectionProviderIfTenantIdIsRegistered() {
        // arrange
        ConfigurableConnectionProviderMock mockProvider = new ConfigurableConnectionProviderMock();
        mockProvider.registerProvider(TENANT_ID, mock(ConnectionProvider.class));

        // act
        ConnectionProvider foundConnectionProvider = mockProvider.selectConnectionProvider(TENANT_ID);

        // assert
        Assertions.assertNotNull(foundConnectionProvider);
    }

    @DisplayName("getAnyConnectionProvider() return ConnectionProvider for TenantId public")
    @Test
    void getAnyConnectionProviderReturnConnectionProviderForTenantIdPublic() {
        // arrange
        ConfigurableConnectionProviderMock mockProvider = new ConfigurableConnectionProviderMock();
        mockProvider.registerProvider("public", mock(ConnectionProvider.class));

        // act
        ConnectionProvider foundConnectionProvider = mockProvider.getAnyConnectionProvider();

        // assert
        Assertions.assertNotNull(foundConnectionProvider);
    }

    @DisplayName("throws a RuntimeException when getPropertiesFromFilePaths() throws an IOException")
    @Test
    void throwsRuntimeExceptionWhenGetPropertiesFromFilePathsThrowsIOException() {
        BasicConnectionProviderMock mockProviderWhichThrowsIOException = new BasicConnectionProviderMock() {
            @Override
            protected Properties getPropertiesFromFilePaths() throws IOException {
                throw new IOException("no properties found");
            }
        };

        Assertions.assertThrows(RuntimeException.class,
                () -> mockProviderWhichThrowsIOException.getConnectionProvider(TENANT_ID));
    }
}
