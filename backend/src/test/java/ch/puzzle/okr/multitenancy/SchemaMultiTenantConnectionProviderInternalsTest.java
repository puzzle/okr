package ch.puzzle.okr.multitenancy;

import static org.mockito.Mockito.mock;

import ch.puzzle.okr.exception.ConnectionProviderException;
import java.util.Properties;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class SchemaMultiTenantConnectionProviderInternalsTest {

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

    @DisplayName("Should return ConnectionProvider if tenantId is registered after calling getConnectionProvider()")
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

    @DisplayName("Should throw Exception when lookup tenantId is null after calling getConnectionProvider()")
    @Test
    void getConnectionProviderThrowsExceptionWhenLookupTenantIdIsNull() {
        // arrange
        ConfigurableConnectionProviderMock mockProvider = new ConfigurableConnectionProviderMock();

        // act + assert
        Assertions.assertThrows(ConnectionProviderException.class, () -> mockProvider.getConnectionProvider(null));
    }

    @DisplayName("Should return ConnectionProvider if tenantId is registered after calling selectConnectionProvider()")
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

    @DisplayName("Should return ConnectionProvider for tenantId 'public' after calling getAnyConnectionProvider()")
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

    @DisplayName("Should throw RuntimeException when no properties are found after calling getConnectionProvider()")
    @Test
    void getConnectionProviderShouldThrowRuntimeExceptionWhenNoPropertiesAreFound() {
        ConfigurableConnectionProviderMock mockProvider = new ConfigurableConnectionProviderMock();

        Assertions.assertThrows(RuntimeException.class, () -> mockProvider.getConnectionProvider(TENANT_ID));
    }
}
