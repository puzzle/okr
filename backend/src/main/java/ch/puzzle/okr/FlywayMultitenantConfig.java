package ch.puzzle.okr;

import ch.puzzle.okr.multitenancy.FlywayMultitenantMigrationInitializer;
import org.flywaydb.core.Flyway;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayMultitenantConfig {

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy(FlywayMultitenantMigrationInitializer flywayMigration) {
        return flyway -> flywayMigration.migrateFlyway();
    }

}