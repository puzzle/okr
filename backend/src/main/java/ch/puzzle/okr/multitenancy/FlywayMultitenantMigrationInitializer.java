package ch.puzzle.okr.multitenancy;

import jakarta.persistence.EntityNotFoundException;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class FlywayMultitenantMigrationInitializer {
    private final TenantConfigProviderInterface tenantConfigProvider;
    private final String[] scriptLocations;

    private static final Logger logger = LoggerFactory.getLogger(FlywayMultitenantMigrationInitializer.class);

    public FlywayMultitenantMigrationInitializer(TenantConfigProviderInterface tenantConfigProvider,
                                                 final @Value("${spring.flyway.locations}")
                                                 String[] scriptLocations) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.scriptLocations = scriptLocations;
    }

    public void migrateFlyway() {
        this.tenantConfigProvider.getTenantConfigs().forEach(tenantConfig -> {
            TenantConfigProvider.DataSourceConfig dataSourceConfigFlyway = this.tenantConfigProvider
                    .getTenantConfigById(tenantConfig.tenantId())
                    .map(TenantConfigProvider.TenantConfig::dataSourceConfigFlyway)
                    .orElseThrow(() -> new EntityNotFoundException("Cannot find tenant for configuring flyway migration"));

            logUsedHibernateConfig(dataSourceConfigFlyway);

            Flyway tenantSchemaFlyway = Flyway
                    .configure() //
                    .dataSource(dataSourceConfigFlyway.url(),
                                dataSourceConfigFlyway.name(),
                                dataSourceConfigFlyway.password()) //
                    .locations(scriptLocations) //
                    .baselineOnMigrate(Boolean.TRUE) //
                    .schemas(dataSourceConfigFlyway.schema()) //
                    .load();

            tenantSchemaFlyway.migrate();
        });
    }

    private void logUsedHibernateConfig(TenantConfigProvider.DataSourceConfig dataSourceConfig) {
        logger.info("use DbConfig: user={}", dataSourceConfig.name());
    }
}