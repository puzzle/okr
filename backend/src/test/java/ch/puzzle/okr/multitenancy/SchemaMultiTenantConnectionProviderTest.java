package ch.puzzle.okr.multitenancy;

import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringIntegrationTest
public class SchemaMultiTenantConnectionProviderTest {
    @Mock
    Connection connection;
    @Mock
    Statement statement;

    private static final String TENANT_ID = "pitc";

    @DisplayName("getConnection() should set schema with okr prefix")
    @Test
    void getConnectionShouldSetSchemaWithOkrPrefix() throws SQLException {
        // arrange
        when(connection.createStatement()).thenReturn(statement);
        AbstractSchemaMultiTenantConnectionProvider provider = new AbstractSchemaMultiTenantConnectionProvider() {
        };

        // act
        provider.getConnection(TENANT_ID, connection);

        // assert
        verify(statement).execute("SET SCHEMA 'okr_" + TENANT_ID + "';");
    }

    @DisplayName("getConnection() should set schema without okr prefix if tenant id is DEFAULT_TENANT_ID")
    @Test
    void getConnectionShouldSetSchemaWithoutOkrPrefixIfTenantIdIsDefaultTenantId() throws SQLException {
        // arrange
        when(connection.createStatement()).thenReturn(statement);
        AbstractSchemaMultiTenantConnectionProvider provider = new AbstractSchemaMultiTenantConnectionProvider() {
        };

        // act
        provider.getConnection(TenantContext.DEFAULT_TENANT_ID, connection);

        // assert
        verify(statement).execute("SET SCHEMA '" + TenantContext.DEFAULT_TENANT_ID + "';");
    }

}
