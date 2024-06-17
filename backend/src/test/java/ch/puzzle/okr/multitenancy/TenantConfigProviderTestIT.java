package ch.puzzle.okr.multitenancy;

import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.test.context.TestPropertySource;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.when;

@SpringIntegrationTest
@SpringBootConfiguration
@TestPropertySource(properties = { "okr.tenant-ids=pitc,acme", })
public class TenantConfigProviderTestIT {

    private static final String JWK_SET_URI = "_jwkSetUri";
    private static final String FRONTEND_CLIENT_ISSUER_URL = "_frontendClientIssuerUrl";
    private static final String FRONTEND_CLIENT_ID = "_frontendClientId";
    private static final String DATASOURCE_URL = "_datasourceUrl";
    private static final String DATASOURCE_NAME = "_datasourceName";
    private static final String DATASOURCE_PASSWORD = "_datasourcePassword";
    private static final String DATASOURCE_SCHEMA = "_datasourceSchema";
    private static final String DRIVER_CLASS_NAME = "driverClassName";
    private static final String CHAMPION_EMAILS_1 = "a@pitc.ch";
    private static final String CHAMPION_EMAILS_2 = "b@pitc.ch";

    @Value("${okr.tenant-ids}")
    private String[] tenantIds;

    @Mock
    private Environment env;

    private static final String PITC = "pitc";
    private static final String ACME = "acme";

    @BeforeEach
    void setUp() {
        setupPropertiesForTenantWithId(PITC);
        setupPropertiesForTenantWithId(ACME);
    }

    private void setupPropertiesForTenantWithId(String id) {
        when(env.getProperty(fmt("okr.tenants.{0}.security.oauth2.resourceserver.jwt.jwk-set-uri", id))).thenReturn(id + JWK_SET_URI);
        when(env.getProperty(fmt("okr.tenants.{0}.security.oauth2.frontend.issuer-url", id))).thenReturn(id + FRONTEND_CLIENT_ISSUER_URL);
        when(env.getProperty(fmt("okr.tenants.{0}.security.oauth2.frontend.client-id", id))).thenReturn(id + FRONTEND_CLIENT_ID);
        when(env.getProperty(fmt("okr.tenants.{0}.user.champion.emails", id), "")).thenReturn(CHAMPION_EMAILS_1 + "," + CHAMPION_EMAILS_2); // with default value
        when(env.getProperty(fmt("okr.tenants.{0}.datasource.url", id))).thenReturn(id + DATASOURCE_URL);
        when(env.getProperty(fmt("okr.tenants.{0}.datasource.username", id))).thenReturn(id + DATASOURCE_NAME);
        when(env.getProperty(fmt("okr.tenants.{0}.datasource.password", id))).thenReturn(id + DATASOURCE_PASSWORD);
        when(env.getProperty(fmt("okr.tenants.{0}.datasource.schema", id))).thenReturn(id + DATASOURCE_SCHEMA);
        when(env.getProperty("okr.datasource.driver-class-name")).thenReturn(DRIVER_CLASS_NAME);
    }

    private String fmt(String template, String tenantId) {
        return MessageFormat.format(template, tenantId);
    }

    @DisplayName("getTenantConfigs returns all TenantConfigs as List")
    @Test
    public void testGetTenantConfigs() {
        TenantConfigProvider configProvider = new TenantConfigProvider(tenantIds, env);
        List<TenantConfigProvider.TenantConfig> tenantConfigs = configProvider.getTenantConfigs();
        for (TenantConfigProvider.TenantConfig config : tenantConfigs) {
            assertTenantConfigProvider(config);
        }
    }

    @DisplayName("getTenantConfigById returns for an existing TenantId the TenantConfig as Optional")
    @Test
    void testGetTenantConfigByIdForExistingTenantId() {
        TenantConfigProvider configProvider = new TenantConfigProvider(tenantIds, env);
        Optional<TenantConfigProvider.TenantConfig> config = configProvider.getTenantConfigById(PITC);
        Assertions.assertTrue(config.isPresent());
        assertTenantConfigProvider(config.get());
    }

    @DisplayName("getTenantConfigById returns for a non existing TenantId an empty Optional")
    @Test
    void testGetTenantConfigByIdForNonExistingTenantId() {
        TenantConfigProvider configProvider = new TenantConfigProvider(tenantIds, env);
        Optional<TenantConfigProvider.TenantConfig> config = configProvider.getTenantConfigById("PITC-London");
        Assertions.assertTrue(config.isEmpty());
    }

    @DisplayName("getJwkSetUri returns for an existing TenantId the JwkSetUri as Optional")
    @Test
    void testGetJwkSetUriForExistingTenantId() {
        TenantConfigProvider configProvider = new TenantConfigProvider(tenantIds, env);
        Optional<String> jwkSetUri = configProvider.getJwkSetUri(PITC);
        Assertions.assertTrue(jwkSetUri.isPresent());
        Assertions.assertEquals(PITC + JWK_SET_URI, jwkSetUri.get());
    }

    @DisplayName("getJwkSetUri returns for a non existing TenantId an empty Optional")
    @Test
    void testGetJwkSetUriForNonExistingTenantId() {
        TenantConfigProvider configProvider = new TenantConfigProvider(tenantIds, env);
        Optional<String> jwkSetUri = configProvider.getJwkSetUri("PITC-London");
        Assertions.assertTrue(jwkSetUri.isEmpty());
    }

    private static void assertTenantConfigProvider(TenantConfigProvider.TenantConfig tenantConfig) {
        String tenantId = tenantConfig.tenantId();
        Assertions.assertEquals(tenantId, tenantConfig.tenantId());
        Assertions.assertEquals(tenantId + JWK_SET_URI, tenantConfig.jwkSetUri());
        Assertions.assertEquals(tenantId + FRONTEND_CLIENT_ISSUER_URL, tenantConfig.issuerUrl());
        Assertions.assertEquals(tenantId + FRONTEND_CLIENT_ID, tenantConfig.clientId());
        Assertions.assertArrayEquals(new String[] { CHAMPION_EMAILS_1, CHAMPION_EMAILS_2 },
                tenantConfig.okrChampionEmails());
        Assertions.assertEquals(tenantId + DATASOURCE_URL, tenantConfig.dataSourceConfig().url());
        Assertions.assertEquals(tenantId + DATASOURCE_NAME, tenantConfig.dataSourceConfig().name());
        Assertions.assertEquals(tenantId + DATASOURCE_PASSWORD, tenantConfig.dataSourceConfig().password());
        Assertions.assertEquals(tenantId + DATASOURCE_SCHEMA, tenantConfig.dataSourceConfig().schema());
        Assertions.assertEquals(DRIVER_CLASS_NAME, tenantConfig.dataSourceConfig().driverClassName());
    }

}
