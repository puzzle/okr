package ch.puzzle.okr.multitenancy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SchemaMultiTenantConnectionProviderH2Test {

    @Test
    void returnsPathToH2Properties() {
        SchemaMultiTenantConnectionProviderH2 connectionProvider = new SchemaMultiTenantConnectionProviderH2();
        String filePath = connectionProvider.getHibernatePropertiesFilePaths();
        assertEquals("/hibernate-multitenancy-h2.properties", filePath);
    }
}