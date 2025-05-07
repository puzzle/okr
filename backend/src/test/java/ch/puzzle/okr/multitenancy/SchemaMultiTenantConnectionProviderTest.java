package ch.puzzle.okr.multitenancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.exception.ConnectionProviderException;
import ch.puzzle.okr.test.SpringIntegrationTest;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

@SpringIntegrationTest
class SchemaMultiTenantConnectionProviderTest {
    @Mock
    private Connection connection;
    @Mock
    private Statement statement;

    private SchemaMultiTenantConnectionProvider connectionProvider;

    private static final String PITC_TENANT_ID = "pitc";
    private static final String DEFAULT_TENANT_ID = TenantContext.DEFAULT_TENANT_ID;

    @BeforeEach
    void setup() {
        connectionProvider = new SchemaMultiTenantConnectionProvider();
    }

    @DisplayName("getConnection() should set schema with okr prefix")
    @Test
    void getConnectionShouldSetSchemaWithOkrPrefix() throws SQLException {
        // arrange
        when(connection.createStatement()).thenReturn(statement);

        // act
        connectionProvider.getConnection(PITC_TENANT_ID, connection);

        // assert
        verify(statement).execute("SET SCHEMA 'okr_" + PITC_TENANT_ID + "';");
    }

    @DisplayName("getConnection() should set schema without okr prefix if tenant id is DEFAULT_TENANT_ID")
    @Test
    void getConnectionShouldSetSchemaWithoutOkrPrefixIfTenantIdIsDefaultTenantId() throws SQLException {
        // arrange
        when(connection.createStatement()).thenReturn(statement);

        // act
        connectionProvider.getConnection(DEFAULT_TENANT_ID, connection);

        // assert
        verify(statement).execute("SET SCHEMA '" + DEFAULT_TENANT_ID + "';");
    }

    @DisplayName("createAndCacheNewConnectionProvider() should throw exception if tenantId is null")
    @Test
    void createAndCacheNewConnectionProviderShouldThrowExceptionIfTenantIdIsNull() {
        // act + assert
        var exception = assertThrows(ConnectionProviderException.class,
                                     () -> connectionProvider.createAndCacheNewConnectionProvider(null));

        assertEquals("Cannot create new connection provider for tenant: null", exception.getMessage());
    }

    @DisplayName("getHibernatePropertiesForTenantIdentifier() should throw exception if tenantId is null")
    @Test
    void getHibernatePropertiesForTenantIdentifierShouldThrowExceptionIfTenantIdIsNull() {
        // act + assert
        var exception = assertThrows(ConnectionProviderException.class,
                                     () -> connectionProvider.getHibernatePropertiesForTenantIdentifier(null));

        assertEquals("No hibernate configuration found for tenant: null", exception.getMessage());
    }

}
