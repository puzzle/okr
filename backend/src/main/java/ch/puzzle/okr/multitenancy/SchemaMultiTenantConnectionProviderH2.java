package ch.puzzle.okr.multitenancy;

public class SchemaMultiTenantConnectionProviderH2 extends AbstractSchemaMultiTenantConnectionProvider {
    protected String getHibernatePropertiesFilePaths() {
        return "/hibernate-multitenancy-h2.properties";
    }
}