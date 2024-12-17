package ch.puzzle.okr.multitenancy;

import ch.puzzle.okr.exception.ConnectionProviderException;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Properties;

import static org.mockito.Mockito.mock;

public class SchemaMultiTenantConnectionProviderInternalsTest {

    private static final String TENANT_ID = "pitc";

    private static class ConfigurableConnectionProviderMock extends SchemaMultiTenantConnectionProvider {

        @Override
        protected Properties getHibernateProperties() {
            return new Properties();
        }

        public void registerProvider(String tenantIdentifier, ConnectionProvider connectionProvider) {
            connectionProviderMap.put(tenantIdentifier, connectionProvider);
        }
    }

    @DisplayName("getConnectionProvider() returns ConnectionProvider if TenantId is registered")
    @Test
    void getConnectionProviderReturnsConnectionProviderIfTenantIdIsRegistered() {
        // arrange
        ConfigurableConnectionProviderMock mockProvider = new ConfigurableConnectionProviderMock();
        mockProvider.registerProvider(TENANT_ID, mock(ConnectionProvider.class));

        // act
        ConnectionProvider foundConnectionProvider = mockProvider.getConnectionProvider(TENANT_ID);

        // assert
        Assertions.assertNotNull(foundConnectionProvider);
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

    @DisplayName("getConnectionProviderShouldThrowRuntimeExceptionWhenNoPropertiesAreFound")
    @Test
    void getConnectionProviderShouldThrowRuntimeExceptionWhenNoPropertiesAreFound() {
        ConfigurableConnectionProviderMock mockProvider = new ConfigurableConnectionProviderMock();

        Assertions.assertThrows(RuntimeException.class, () -> mockProvider.getConnectionProvider(TENANT_ID));
    }
}
