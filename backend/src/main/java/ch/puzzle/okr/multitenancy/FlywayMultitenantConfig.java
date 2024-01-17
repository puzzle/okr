package ch.puzzle.okr.multitenancy;

import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayMultitenantConfig {
    @Bean
    public FlywayMigrationStrategy flywayMigrationStrategy(FlywayMultitenantMigrationInitializer flywayMigration) {
        return flyway -> {
            flywayMigration.migrateFlyway();
        };
    }
}