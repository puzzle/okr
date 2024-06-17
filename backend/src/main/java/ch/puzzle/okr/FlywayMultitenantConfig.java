package ch.puzzle.okr;

import ch.puzzle.okr.multitenancy.FlywayMultitenantMigrationInitializer;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FlywayMultitenantConfig {

    @Bean
    public FlywayMigrationStrategy cleanMigrateStrategy(FlywayMultitenantMigrationInitializer flywayMigration) {
        return flyway -> flywayMigration.migrateFlyway();
    }

    @Bean("customKeyGenerator")
    public KeyGenerator keyGenerator() {
        return new UserKeyGenerator();
    }

}