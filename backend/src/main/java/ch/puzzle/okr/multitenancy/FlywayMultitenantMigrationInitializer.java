package ch.puzzle.okr.multitenancy;

import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityNotFoundException;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
public class FlywayMultitenantMigrationInitializer {
    private final MultitenantDataSource multitenantDataSource;
    private final TenantConfigProvider tenantConfigProvider;
    private final String[] scriptLocations;

    public FlywayMultitenantMigrationInitializer(final MultitenantDataSource multitenantDataSource,
            TenantConfigProvider tenantConfigProvider,
            final @Value("${spring.flyway.locations}") String[] scriptLocations) {
        this.multitenantDataSource = multitenantDataSource;
        this.tenantConfigProvider = tenantConfigProvider;
        this.scriptLocations = scriptLocations;
    }

    public void migrateFlyway() {
        this.multitenantDataSource.getResolvedDataSources().entrySet().forEach(objectDataSourceEntry -> {
            String tenantId = (String) objectDataSourceEntry.getKey();

            TenantConfigProvider.TenantConfig tenantConfig = this.tenantConfigProvider.getTenantConfigById(tenantId)
                    .orElseThrow(
                            () -> new EntityNotFoundException("Cannot find tenant for configuring flyway migration"));

            Flyway flyway = Flyway.configure().locations(scriptLocations).baselineOnMigrate(Boolean.TRUE)
                    .dataSource(objectDataSourceEntry.getValue()).schemas(tenantConfig.dataSourceConfig().schema())
                    .load();

            flyway.migrate();
        });

    }
}