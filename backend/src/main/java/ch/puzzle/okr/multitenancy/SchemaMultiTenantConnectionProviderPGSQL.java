package ch.puzzle.okr.multitenancy;

import org.hibernate.engine.jdbc.connections.spi.AbstractMultiTenantConnectionProvider;

public class SchemaMultiTenantConnectionProviderPGSQL extends AbstractSchemaMultiTenantConnectionProvider {
    protected String getHibernatePropertiesFilePaths() {
        return "/hibernate-multitenancy-pgsql.properties";
    }
}