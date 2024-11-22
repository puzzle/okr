package ch.puzzle.okr.multitenancy;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import jakarta.persistence.EntityNotFoundException;

@Component
public class FlywayMultitenantMigrationInitializer {
    private final TenantConfigProviderInterface tenantConfigProvider;
    private final String[] scriptLocations;

    public FlywayMultitenantMigrationInitializer(TenantConfigProviderInterface tenantConfigProvider, final @Value("${spring.flyway.locations}") String[] scriptLocations) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.scriptLocations = scriptLocations;
    }

    public void migrateFlyway() {
        this.tenantConfigProvider.getTenantConfigs()
                                 .forEach((tenantConfig) -> {
                                     TenantConfigProvider.DataSourceConfig dataSourceConfig = this.tenantConfigProvider.getTenantConfigById(tenantConfig.tenantId())
                                                                                                                       .map(TenantConfigProvider.TenantConfig::dataSourceConfig)
                                                                                                                       .orElseThrow(() -> new EntityNotFoundException("Cannot find tenant for configuring flyway migration"));

                                     Flyway tenantSchemaFlyway = Flyway.configure() //
                                                                       .dataSource(dataSourceConfig.url(),
                                                                                   dataSourceConfig.name(),
                                                                                   dataSourceConfig.password()) //
                                                                       .locations(scriptLocations) //
                                                                       .baselineOnMigrate(Boolean.TRUE) //
                                                                       .schemas(dataSourceConfig.schema()) //
                                                                       .load();

                                     tenantSchemaFlyway.migrate();
                                 });

    }
}