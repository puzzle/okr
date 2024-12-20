package ch.puzzle.okr.multitenancy;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import java.util.List;
import java.util.Optional;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.FluentConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

/**
 * Test for FlywayMultitenantMigrationInitializer.migrateFlyway(). The
 * FlywayMultitenantMigrationInitializer constructor gets a mock implementation
 * of a TenantConfigProviderInterface, which provides some test data. Inside
 * migrateFlyway(), there are two calls to Flyway: Flyway.configure() (a static
 * method) and flyway.migrate() (an instance method). Flyway.configure() returns
 * a FluentConfiguration. Then several method calls are made on the
 * FluentConfiguration, which also return a FluentConfiguration. The last calls
 * is load(), which returns a Flyway Object. We can mock that with a manual Spy
 * FluentConfigurationSpy, which records the methods calls and load() returns a
 * Mockito Mock, which does nothing. And later, flyway.migrate() is called on
 * the Mockito Mock, which does nothing.
 */
public class FlywayMultitenantMigrationInitializerTest {

    private static final String NOT_USED = "not_used";
    private static final String URL = "url";
    private static final String NAME = "name";
    private static final String PASSWORD = "password";
    private static final String SCHEMA = "schema";
    private static final String SCRIPT_LOCATION = "script_location";

    private static class FluentConfigurationSpy extends FluentConfiguration {
        private String log = "";

        public FluentConfiguration dataSource(String url, String user, String password) {
            log += "dataSource(" + url + "," + user + "," + password + ")";
            return this;
        }

        public FluentConfiguration locations(String... locations) {
            log += " locations(" + String.join("", locations) + ")";
            return this;
        }

        public FluentConfiguration baselineOnMigrate(boolean baselineOnMigrate) {
            log += " baselineOnMigrate(" + baselineOnMigrate + ")";
            return this;
        }

        public FluentConfiguration schemas(String... schemas) {
            log += " schemas(" + String.join("", schemas) + ")";
            return this;
        }

        // the result of load() is used by flyway.migrate(), which we don't test. So
        // load() returns a mock which does
        // nothing.
        @Override
        public Flyway load() {
            return mock(Flyway.class);
        }

        public String getLog() {
            return log;
        }
    }

    private final TenantConfigProviderInterface providerInterfaceMock = new TenantConfigProviderInterface() {

        private final TenantConfigProvider.DataSourceConfig dataSourceConfig = new TenantConfigProvider.DataSourceConfig(NOT_USED,
                                                                                                                         URL,
                                                                                                                         NAME,
                                                                                                                         PASSWORD,
                                                                                                                         SCHEMA);

        private final TenantConfigProvider.TenantConfig tenantConfig = new TenantConfigProvider.TenantConfig(NOT_USED,
                                                                                                             new String[]{
                                                                                                                     NOT_USED },
                                                                                                             NOT_USED,
                                                                                                             NOT_USED,
                                                                                                             NOT_USED,
                                                                                                             dataSourceConfig);

        @Override
        public List<TenantConfigProvider.TenantConfig> getTenantConfigs() {
            return List.of(tenantConfig);
        }

        @Override
        public Optional<TenantConfigProvider.TenantConfig> getTenantConfigById(String tenantId) {
            return Optional.of(tenantConfig);
        }

        @Override
        public Optional<String> getJwkSetUri(String tenantId) {
            return Optional.empty();
        }
    };

    @DisplayName("Flyway.configure() should return FluentConfiguration which we can assert")
    @Test
    void flywayConfigureShouldReturnFluentConfigurationWhichWeCanAssert() {
        try (MockedStatic<Flyway> mockedStatic = Mockito.mockStatic(Flyway.class)) {
            // arrange
            FluentConfigurationSpy fluentConfiguration = new FluentConfigurationSpy();
            mockedStatic.when(Flyway::configure).thenReturn(fluentConfiguration);

            FlywayMultitenantMigrationInitializer migrationInitializer = new FlywayMultitenantMigrationInitializer(providerInterfaceMock,
                                                                                                                   new String[]{
                                                                                                                           SCRIPT_LOCATION });

            // act
            migrationInitializer.migrateFlyway();

            // assert
            String expected = "dataSource(url,name,password) locations(script_location) baselineOnMigrate(true) schemas(schema)";
            String actual = fluentConfiguration.getLog();
            assertEquals(expected, actual);
        }
    }

}