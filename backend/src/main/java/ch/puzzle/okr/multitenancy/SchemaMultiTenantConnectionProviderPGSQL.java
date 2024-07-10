package ch.puzzle.okr.multitenancy;

import ch.puzzle.okr.multitenancy.listener.HibernateContext;

import java.util.Properties;

public class SchemaMultiTenantConnectionProviderPGSQL extends AbstractSchemaMultiTenantConnectionProvider {

    @Override
    protected Properties getHibernateProperties() {
        return HibernateContext.getHibernateConfig();
    }
}