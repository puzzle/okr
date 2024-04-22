package ch.puzzle.okr.multitenancy;

import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringIntegrationTest
public class SchemaMultiTenantConnectionProviderTest {
    @Mock
    Connection connection;
    @Mock
    Statement statement;

    private static final String TENANT_ID = "pitc";

    @Test
    void testSetSchemaOfTenant() throws SQLException {
        when(connection.createStatement()).thenReturn(statement);

        AbstractSchemaMultiTenantConnectionProvider provider = new AbstractSchemaMultiTenantConnectionProvider() {
            @Override
            protected String getHibernatePropertiesFilePaths() {
                return null;
            }
        };
        provider.getConnection(TENANT_ID, connection);

        verify(statement).execute("SET SCHEMA 'okr_" + TENANT_ID + "';");
    }

}
