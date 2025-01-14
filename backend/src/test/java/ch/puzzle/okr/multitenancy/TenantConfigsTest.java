package ch.puzzle.okr.multitenancy;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TenantConfigsTest {

    private static final String PITC = "pitc";
    private static final String ACME = "acme";

    @BeforeEach
    void setUp() {
        TenantConfigs.clear();
    }

    @AfterEach
    void tearDown() {
        TenantConfigs.clear();
    }

    private TenantConfigProvider.TenantConfig createTenantConfig(String tenantId) {
        return new TenantConfigProvider.TenantConfig( //
                tenantId, null, "jwkSetUri", "issuerUrl", "clientId", //
                new TenantConfigProvider.DataSourceConfig("driverClassName", "url", //
                        "flyway_" + tenantId, "password", "schema_" + tenantId),
                new TenantConfigProvider.DataSourceConfig("driverClassName", "url", //
                        "app_" + tenantId, "password", "schema_" + tenantId));
    }

    @DisplayName("add() should add TenantConfig for tenantId in the map")
    @ParameterizedTest
    @ValueSource(strings = { PITC, ACME })
    void addShouldAddTenantConfigForTenantIdToMap(String tenantId) {
        // arrange + act
        TenantConfigs.add(tenantId, createTenantConfig(tenantId));

        // act
        var currentTenantConfig = TenantConfigs.get(tenantId);

        // assert
        assertTenantConfig(tenantId, currentTenantConfig);
    }

    @DisplayName("add() should not override TenantConfig for existing tenantId in the map")
    @Test
    void addShouldNotOverrideTenantConfigForExistingTenantIdInMap() {
        // arrange + act
        TenantConfigs.add(PITC, createTenantConfig(PITC));
        TenantConfigs.add(PITC, createTenantConfig("dummy"));

        // act
        var currentTenantConfig = TenantConfigs.get(PITC);

        // assert
        assertTenantConfig(PITC, currentTenantConfig);
    }

    private void assertTenantConfig(String tenantId, TenantConfigProvider.TenantConfig current) {
        assertEquals(tenantId, current.tenantId());
        assertEquals("flyway_" + tenantId, current.dataSourceConfigFlyway().name());
        assertEquals("app_" + tenantId, current.dataSourceConfigApp().name());
    }

}
