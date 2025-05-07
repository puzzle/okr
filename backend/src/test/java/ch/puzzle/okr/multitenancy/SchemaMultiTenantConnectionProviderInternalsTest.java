package ch.puzzle.okr.multitenancy;

import static ch.puzzle.okr.multitenancy.HibernateContext.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

import ch.puzzle.okr.exception.ConnectionProviderException;
import java.util.Properties;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.MockedStatic;

class SchemaMultiTenantConnectionProviderInternalsTest {

    private static final String TENANT_ID = "pitc";
    private static final String TENANT_ID_ACME = "acme";
    private static final String DEFAULT_TENANT_ID = TenantContext.DEFAULT_TENANT_ID;
    private static final String NOT_USED = "";

    private static MockedStatic<HibernateContext> mockedStatic;

    /**
     * A mock for the SchemaMultiTenantConnectionProvider, which is configurable. It
     * can be configured via static factory methods: use cachedConnectionProvider(),
     * for a cached ConnectionProvider.Or use
     * inPropertiesDefinedConnectionProvider(), for no cached ConnectionProvider,
     * but the data for creating a ConnectionProvider is defined in the (hibernate)
     * properties.
     */
    private static class ConnectionProviderMock extends SchemaMultiTenantConnectionProvider {

        public static ConnectionProviderMock cachedConnectionProvider( //
                                                                      String tenantId,
                                                                      ConnectionProvider connectionProvider) {
            return new ConnectionProviderMock(tenantId, connectionProvider);
        }

        private ConnectionProviderMock(String tenantId, ConnectionProvider connectionProvider) {
            connectionProviderMap.put(tenantId, connectionProvider);
        }

        public static ConnectionProviderMock inPropertiesDefinedConnectionProvider( //
                                                                                   String tenantId,
                                                                                   Properties hibernateProperties) {
            return new ConnectionProviderMock(tenantId, hibernateProperties);
        }

        private ConnectionProviderMock(String tenantId, Properties hibernateProperties) {
            // mock dependencies to HibernateContext used in getHibernateProperties()
            mockedStatic.when(() -> getHibernateConfig(tenantId)).thenReturn(hibernateProperties);
            mockedStatic.when(() -> getHibernateConfig()).thenReturn(hibernateProperties);
        }

        @Override
        protected ConnectionProvider initConnectionProvider(Properties hibernateProperties) {
            return mock(ConnectionProvider.class);
        }

        public boolean isCached(String tenantIdentifier) {
            return connectionProviderMap.containsKey(tenantIdentifier);
        }
    }

    private Properties getConfigAsProperties(String tenantId) {
        var properties = new Properties();
        properties.put(HIBERNATE_CONNECTION_URL, concat("url", tenantId));
        properties.put(HIBERNATE_CONNECTION_USERNAME, concat("username", tenantId));
        properties.put(HIBERNATE_CONNECTION_PASSWORD, concat("password", tenantId));
        properties.put(HIBERNATE_MULTITENANCY, concat("multitenancy", tenantId));
        properties.put(SPRING_DATASOURCE_URL, concat("url", tenantId));
        properties.put(SPRING_DATASOURCE_USERNAME, concat("username", tenantId));
        properties.put(SPRING_DATASOURCE_PASSWORD, concat("password", tenantId));
        return properties;
    }

    private String concat(String data, String tenantId) {
        return data + "_" + tenantId;
    }

    @BeforeEach
    public void setUp() {
        mockedStatic = mockStatic(HibernateContext.class);
    }

    @AfterEach
    public void tearDown() {
        mockedStatic.close();
    }

    @DisplayName("getConnectionProvider() should throw exception when tenantId is null")
    @Test
    void getConnectionProviderShouldThrowExceptionWhenTenantIdIsNull() {
        // arrange
        String tenantId = null;
        var mockProvider = ConnectionProviderMock.inPropertiesDefinedConnectionProvider(NOT_USED, new Properties());

        // act + assert
        var exception = assertThrows(ConnectionProviderException.class,
                                     () -> mockProvider.getConnectionProvider(tenantId));

        // assert
        assertEquals("Cannot create new connection provider for tenant: null", exception.getMessage());
        assertGetHibernateConfigNotCalled();
    }

    @DisplayName("getConnectionProvider() should return ConnectionProvider from cache")
    @ParameterizedTest
    @ValueSource(strings = { TENANT_ID, TENANT_ID_ACME })
    void getConnectionProviderShouldReturnConnectionProviderFromCache(String tenantId) {
        // arrange
        var mockProvider = ConnectionProviderMock.cachedConnectionProvider(tenantId, mock(ConnectionProvider.class));

        // pre-conditions
        assertCached(mockProvider, tenantId);

        // act
        var foundConnectionProvider = mockProvider.getConnectionProvider(tenantId);

        // assert
        assertNotNull(foundConnectionProvider);
        assertGetHibernateConfigNotCalled(tenantId);

        // post-condition
        assertCached(mockProvider, tenantId);
    }

    @DisplayName("getConnectionProvider() should throw exception when ConnectionProvider is not cached and tenant is not configured in hibernate properties")
    @ParameterizedTest
    @ValueSource(strings = { TENANT_ID, TENANT_ID_ACME })
    void getConnectionProviderShouldThrowExceptionWhenConnectionProviderIsNotCachedAndTenantIsNotConfiguredInHibernateProperties(String tenantId) {
        // arrange
        var mockProvider = ConnectionProviderMock.inPropertiesDefinedConnectionProvider(tenantId, new Properties());

        // pre-conditions
        assertNotCached(mockProvider, tenantId);

        // act + assert
        var exception = assertThrows(RuntimeException.class, () -> mockProvider.getConnectionProvider(tenantId));

        // assert
        assertEquals("Cannot load hibernate properties from application.properties", exception.getMessage());
        assertGetHibernateConfigCalledWithTenantId(tenantId);

        // post-conditions
        assertNotCached(mockProvider, tenantId);
    }

    @DisplayName("getConnectionProvider() should cache ConnectionProvider for DEFAULT_TENANT_ID ('public') if ConnectionProvider is not cached but in hibernate properties")
    @Test
    void getConnectionProviderShouldCacheConnectionProviderForDefaultTenantIdIfConnectionProviderIsNotCachedButInHibernateProperties() {
        // arrange
        Properties properties = getConfigAsProperties(DEFAULT_TENANT_ID);
        var mockProvider = ConnectionProviderMock.inPropertiesDefinedConnectionProvider(DEFAULT_TENANT_ID, properties);

        // pre-condition
        assertNotCached(mockProvider, DEFAULT_TENANT_ID);

        // act
        var connectionProvider = mockProvider.getConnectionProvider(DEFAULT_TENANT_ID);

        // assert
        assertNotNull(connectionProvider);
        assertGetHibernateConfigCalled(DEFAULT_TENANT_ID);

        // post-condition
        assertCached(mockProvider, DEFAULT_TENANT_ID);
    }

    @DisplayName("selectConnectionProvider() should cache ConnectionProvider for DEFAULT_TENANT_ID ('public') if ConnectionProvider is not cached but in hibernate properties")
    @Test
    void selectConnectionProviderShouldCacheConnectionProviderForDefaultTenantIdIfConnectionProviderIsNotCachedButInHibernateProperties() {
        // arrange
        Properties properties = getConfigAsProperties(DEFAULT_TENANT_ID);
        var mockProvider = ConnectionProviderMock.inPropertiesDefinedConnectionProvider(DEFAULT_TENANT_ID, properties);

        // pre-condition
        assertNotCached(mockProvider, DEFAULT_TENANT_ID);

        // act
        var connectionProvider = mockProvider.selectConnectionProvider(DEFAULT_TENANT_ID);

        // assert
        assertNotNull(connectionProvider);
        assertGetHibernateConfigCalled(DEFAULT_TENANT_ID);

        // post-condition
        assertCached(mockProvider, DEFAULT_TENANT_ID);
    }

    @DisplayName("getAnyConnectionProvider() should cache ConnectionProvider for DEFAULT_TENANT_ID ('public') if ConnectionProvider is not cached but in hibernate properties")
    @Test
    void getAnyConnectionProviderShouldCacheConnectionProviderForDefaultTenantIdIfConnectionProviderIsNotCachedButInHibernateProperties() {
        // arrange
        Properties properties = getConfigAsProperties(DEFAULT_TENANT_ID);
        var mockProvider = ConnectionProviderMock.inPropertiesDefinedConnectionProvider(DEFAULT_TENANT_ID, properties);

        // pre-condition
        assertNotCached(mockProvider, DEFAULT_TENANT_ID);

        // act
        var connectionProvider = mockProvider.getAnyConnectionProvider();

        // assert
        assertNotNull(connectionProvider);
        assertGetHibernateConfigCalled(DEFAULT_TENANT_ID);

        // post-condition
        assertCached(mockProvider, DEFAULT_TENANT_ID);
    }

    @DisplayName("getConnectionProvider() should cache ConnectionProvider for tenantId if ConnectionProvider is not cached but in hibernate properties")
    @ParameterizedTest
    @ValueSource(strings = { TENANT_ID, TENANT_ID_ACME })
    void getConnectionProviderShouldCacheConnectionProviderForTenantIdIfConnectionProviderIsNotCachedButInHibernateProperties(String tenantId) {
        // arrange
        Properties properties = getConfigAsProperties(tenantId);
        var mockProvider = ConnectionProviderMock.inPropertiesDefinedConnectionProvider(tenantId, properties);

        // pre-condition
        assertNotCached(mockProvider, tenantId);

        // act
        var connectionProvider = mockProvider.getConnectionProvider(tenantId);

        // assert
        assertNotNull(connectionProvider);
        assertGetHibernateConfigCalledWithTenantId(tenantId);

        // post-condition
        assertCached(mockProvider, tenantId);
    }

    @DisplayName("convertPropertiesToMap() should convert properties to map")
    @Test
    void convertPropertiesToMapShouldConvertPropertiesToMap() {
        // arrange
        var connectionProvider = new SchemaMultiTenantConnectionProvider();
        var properties = getConfigAsProperties(TENANT_ID);

        // act
        var map = connectionProvider.convertPropertiesToMap(properties);

        // assert
        assertEquals(properties.size(), map.size());

        assertEquals("url_pitc", map.get(HIBERNATE_CONNECTION_URL));
        assertEquals("username_pitc", map.get(HIBERNATE_CONNECTION_USERNAME));
        assertEquals("password_pitc", map.get(HIBERNATE_CONNECTION_PASSWORD));
        assertEquals("multitenancy_pitc", map.get(HIBERNATE_MULTITENANCY));
        assertEquals("url_pitc", map.get(SPRING_DATASOURCE_URL));
        assertEquals("username_pitc", map.get(SPRING_DATASOURCE_USERNAME));
        assertEquals("password_pitc", map.get(SPRING_DATASOURCE_PASSWORD));
    }

    @DisplayName("connectionProvider() should return the schema name for tenantId")
    @Test
    void connectionProviderShouldReturnTheSchemaNameForTenantId() {
        // arrange
        var connectionProvider = new SchemaMultiTenantConnectionProvider();
        // act
        var schema = connectionProvider.convertTenantIdToSchemaName(TENANT_ID);
        // assert
        assertEquals("okr_pitc", schema);
    }

    private void assertCached(ConnectionProviderMock mockProvider, String tenantId) {
        assertTrue(mockProvider.isCached(tenantId));
    }

    private void assertNotCached(ConnectionProviderMock mockProvider, String tenantId) {
        assertFalse(mockProvider.isCached(tenantId));
    }

    private void assertGetHibernateConfigCalledWithTenantId(String tenantId) {
        mockedStatic.verify(() -> getHibernateConfig(tenantId));
        mockedStatic.verify(() -> getHibernateConfig(), never());
    }

    private void assertGetHibernateConfigCalled(String tenantId) {
        mockedStatic.verify(() -> getHibernateConfig());
        mockedStatic.verify(() -> getHibernateConfig(tenantId), never());
    }

    private void assertGetHibernateConfigNotCalled() {
        mockedStatic.verify(() -> getHibernateConfig(), never());
        mockedStatic.verify(() -> getHibernateConfig(anyString()), never());
    }

    private void assertGetHibernateConfigNotCalled(String tenantId) {
        mockedStatic.verify(() -> getHibernateConfig(), never());
        mockedStatic.verify(() -> getHibernateConfig(tenantId), never());
    }

}
