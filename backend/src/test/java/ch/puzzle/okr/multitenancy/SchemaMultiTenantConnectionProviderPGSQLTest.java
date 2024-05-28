package ch.puzzle.okr.multitenancy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchemaMultiTenantConnectionProviderPGSQLTest {

    @Test
    void returnsPathToPostgresProperties() {
        SchemaMultiTenantConnectionProviderPGSQL connectionProvider = new SchemaMultiTenantConnectionProviderPGSQL();
        String filePath = connectionProvider.getHibernatePropertiesFilePaths();
        assertEquals("/hibernate-multitenancy-pgsql.properties", filePath);
    }
}