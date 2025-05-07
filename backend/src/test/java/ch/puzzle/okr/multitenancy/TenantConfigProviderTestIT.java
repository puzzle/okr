package ch.puzzle.okr.multitenancy;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.test.SpringIntegrationTest;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.core.env.Environment;

@SpringIntegrationTest
@SpringBootConfiguration
class TenantConfigProviderTestIT {
    private static final String JWK_SET_URI = "jwkSetUri";
    private static final String FRONTEND_CLIENT_ISSUER_URL = "frontendClientIssuerUrl";
    private static final String FRONTEND_CLIENT_ID = "frontendClientId";
    private static final String DATASOURCE_URL = "datasourceUrl";
    private static final String DATASOURCE_NAME_FLY = "datasourceNameFly";
    private static final String DATASOURCE_PASSWORD_FLY = "datasourcePasswordFly";
    private static final String DATASOURCE_NAME_APP = "datasourceNameApp";
    private static final String DATASOURCE_PASSWORD_APP = "datasourcePasswordApp";
    private static final String DATASOURCE_SCHEMA = "datasourceSchema";
    private static final String DRIVER_CLASS_NAME = "driverClassName";
    private static final String CHAMPION_EMAILS_1 = "a@pitc.ch";
    private static final String CHAMPION_EMAILS_2 = "b@pitc.ch";
    private static final String CHAMPION_EMAILS = CHAMPION_EMAILS_1 + "," + CHAMPION_EMAILS_2;

    private final String[] tenantIds = { "pitc", "acme" };

    @Mock
    private Environment env;

    private TenantConfigProvider configProvider;

    @BeforeEach
    void setUp() {
        for (String tenantId : tenantIds) {
            setupPropertiesForTenantWithId(tenantId);
        }
        configProvider = new TenantConfigProvider(tenantIds, env);
    }

    @AfterEach
    void tearDown() {
        TenantConfigProvider.clearTenantConfigsCache();
    }

    private void setupPropertiesForTenantWithId(String id) {
        mockProperty("okr.tenants.{0}.security.oauth2.resourceserver.jwt.jwk-set-uri", JWK_SET_URI, id);
        mockProperty("okr.tenants.{0}.security.oauth2.frontend.issuer-url", FRONTEND_CLIENT_ISSUER_URL, id);
        mockProperty("okr.tenants.{0}.security.oauth2.frontend.client-id", FRONTEND_CLIENT_ID, id);
        mockProperty("okr.tenants.{0}.datasource.url", DATASOURCE_URL, id);
        mockProperty("okr.tenants.{0}.datasource.username.fly", DATASOURCE_NAME_FLY, id);
        mockProperty("okr.tenants.{0}.datasource.password.fly", DATASOURCE_PASSWORD_FLY, id);
        mockProperty("okr.tenants.{0}.datasource.username.app", DATASOURCE_NAME_APP, id);
        mockProperty("okr.tenants.{0}.datasource.password.app", DATASOURCE_PASSWORD_APP, id);
        mockProperty("okr.tenants.{0}.datasource.schema", DATASOURCE_SCHEMA, id);

        mockProperty("okr.datasource.driver-class-name", DRIVER_CLASS_NAME);
        mockPropertyWithDefaultValue("okr.tenants.{0}.user.champion.emails", CHAMPION_EMAILS, id);
    }

    private void mockProperty(String propertyName, String propertyValue, String tenantId) {
        when(env.getProperty(MessageFormat.format(propertyName, tenantId))) //
                .thenReturn(prefix(tenantId) + propertyValue);
    }

    private void mockProperty(String propertyName, String propertyValue) {
        when(env.getProperty(propertyName)).thenReturn(propertyValue);
    }

    private void mockPropertyWithDefaultValue(String propertyName, String propertyValue, String id) {
        when(env.getProperty(MessageFormat.format(propertyName, id), "")) //
                .thenReturn(propertyValue);
    }

    private String prefix(String tenantId) {
        return tenantId + "_";
    }

    @DisplayName("Should return all tenant-configs as a list after calling getTenantConfigs")
    @Test
    void shouldGetTenantConfigs() {
        List<TenantConfigProvider.TenantConfig> tenantConfigs = configProvider.getTenantConfigs();
        for (TenantConfigProvider.TenantConfig config : tenantConfigs) {
            assertTenantConfigProvider(config);
            assertTenantConfigIsCached(config.tenantId());
        }
    }

    @ParameterizedTest(name = "getTenantConfigById returns for an existing TenantId the TenantConfig as Optional")
    @CsvSource({ "pitc, acme" })
    void shouldGetTenantConfigByIdForExistingTenantId(String tenantId) {
        Optional<TenantConfigProvider.TenantConfig> config = configProvider.getTenantConfigById(tenantId);
        assertTrue(config.isPresent());
        assertTenantConfigProvider(config.get());
    }

    @ParameterizedTest(name = "getTenantConfigById returns for a non existing TenantId an empty Optional")
    @CsvSource({ "PITC-London" })
    void shouldGetEmptyTenantConfigForNonExistingTenantId(String nonExistingTenantId) {
        Optional<TenantConfigProvider.TenantConfig> config = configProvider.getTenantConfigById(nonExistingTenantId);
        assertTrue(config.isEmpty());
    }

    @ParameterizedTest(name = "getJwkSetUri returns for an existing TenantId the JwkSetUri as Optional")
    @CsvSource({ "pitc", "acme" })
    void shouldGetJwkSetUriForExistingTenantId(String tenantId) {
        // act
        Optional<String> jwkSetUri = configProvider.getJwkSetUri(tenantId);

        // assert
        assertTrue(jwkSetUri.isPresent());
        assertEquals(prefix(tenantId) + JWK_SET_URI, jwkSetUri.get());
    }

    @ParameterizedTest(name = "getJwkSetUri returns for a non existing TenantId an empty Optional")
    @CsvSource({ "PITC-London" })
    void shouldGetEmptyJwkSetUriForNonExistingTenantId(String nonExistingTenantId) {
        Optional<String> jwkSetUri = configProvider.getJwkSetUri(nonExistingTenantId);
        assertTrue(jwkSetUri.isEmpty());
    }

    private void assertTenantConfigProvider(TenantConfigProvider.TenantConfig tenantConfig) {
        String tenantId = tenantConfig.tenantId();
        assertEquals(tenantId, tenantConfig.tenantId());
        assertEquals(prefix(tenantId) + JWK_SET_URI, tenantConfig.jwkSetUri());
        assertEquals(prefix(tenantId) + FRONTEND_CLIENT_ISSUER_URL, tenantConfig.issuerUrl());
        assertEquals(prefix(tenantId) + FRONTEND_CLIENT_ID, tenantConfig.clientId());
        assertEquals(prefix(tenantId) + DATASOURCE_URL, tenantConfig.dataSourceConfigFlyway().url());
        assertEquals(prefix(tenantId) + DATASOURCE_NAME_FLY, tenantConfig.dataSourceConfigFlyway().name());
        assertEquals(prefix(tenantId) + DATASOURCE_PASSWORD_FLY, tenantConfig.dataSourceConfigFlyway().password());
        assertEquals(prefix(tenantId) + DATASOURCE_NAME_APP, tenantConfig.dataSourceConfigApp().name());
        assertEquals(prefix(tenantId) + DATASOURCE_PASSWORD_APP, tenantConfig.dataSourceConfigApp().password());
        assertEquals(prefix(tenantId) + DATASOURCE_SCHEMA, tenantConfig.dataSourceConfigApp().schema());

        assertArrayEquals(new String[]{ CHAMPION_EMAILS_1, CHAMPION_EMAILS_2 }, tenantConfig.okrChampionEmails());
        assertEquals(DRIVER_CLASS_NAME, tenantConfig.dataSourceConfigFlyway().driverClassName());
    }

    private void assertTenantConfigIsCached(String tenantId) {
        var cachedConfig = TenantConfigProvider.getCachedTenantConfig(tenantId);
        assertNotNull(cachedConfig);
        assertTenantConfigProvider(cachedConfig);
    }
}
