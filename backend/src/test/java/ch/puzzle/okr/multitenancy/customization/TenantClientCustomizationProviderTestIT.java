package ch.puzzle.okr.multitenancy.customization;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.test.SpringIntegrationTest;
import java.text.MessageFormat;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.springframework.core.env.Environment;

@SpringIntegrationTest
class TenantClientCustomizationProviderTestIT {
    private static final String LOGO = "logo";
    private static final String TRIANGLES = "triangles";
    private static final String BACKGROUND_LOGO = "background-logo";
    private static final String FAVICON = "favicon";
    private static final String TITLE = "title";
    private static final String HELP_SITE_URL = "help-site-url";

    private static final String CUSTOM_STYLE_NAME = "okr-topbar-background-color";
    private static final String CUSTOM_STYLE_VALUE = "css-custom-value";

    private final String[] tenantIds = { "pitc", "acme" };

    @Mock
    private Environment env;

    @BeforeEach
    void setUp() {
        for (String tenantId : tenantIds) {
            setupPropertiesForTenantWithId(tenantId);
        }
    }

    private void setupPropertiesForTenantWithId(String id) {
        mockProperty("okr.tenants.{0}.clientcustomization.logo", LOGO, id);
        mockProperty("okr.tenants.{0}.clientcustomization.triangles", TRIANGLES, id);
        mockProperty("okr.tenants.{0}.clientcustomization.background-logo", BACKGROUND_LOGO, id);
        mockProperty("okr.tenants.{0}.clientcustomization.favicon", FAVICON, id);
        mockProperty("okr.tenants.{0}.clientcustomization.title", TITLE, id);
        mockProperty("okr.tenants.{0}.clientcustomization.help-site-url", HELP_SITE_URL, id);
        mockProperty("okr.tenants.{0}.clientcustomization.customstyles.okr-topbar-background-color",
                     CUSTOM_STYLE_VALUE,
                     id);
    }

    private void mockProperty(String propertyName, String propertyValue, String tenantId) {
        when(env.getProperty(MessageFormat.format(propertyName, tenantId))) //
                .thenReturn(prefix(tenantId) + propertyValue);
    }

    private String prefix(String tenantId) {
        return tenantId + "_";
    }

    @ParameterizedTest(name = "getTenantClientCustomizationsById() should return TenantClientCustomization")
    @CsvSource({ "pitc", "acme" })
    void getTenantClientCustomizationsByIdShouldReturnTenantClientCustomization(String tenantId) {
        // arrange
        TenantClientCustomizationProvider provider = new TenantClientCustomizationProvider(tenantIds, env);

        // act
        Optional<TenantClientCustomization> customization = provider.getTenantClientCustomizationsById(tenantId);

        // assert
        assertTrue(customization.isPresent());
        assertTenantClientCustomization(customization.get(), tenantId);
    }

    private void assertTenantClientCustomization(TenantClientCustomization customization, String tenantId) {
        assertEquals(prefix(tenantId) + FAVICON, customization.favicon());
        assertEquals(prefix(tenantId) + LOGO, customization.logo());
        assertEquals(prefix(tenantId) + TRIANGLES, customization.triangles());
        assertEquals(prefix(tenantId) + BACKGROUND_LOGO, customization.backgroundLogo());
        assertEquals(prefix(tenantId) + TITLE, customization.title());
        assertEquals(prefix(tenantId) + HELP_SITE_URL, customization.helpSiteUrl());
        assertCustomStyles(customization, tenantId);
    }

    private void assertCustomStyles(TenantClientCustomization customization, String tenantId) {
        assertNotNull(customization.customStyles());
        assertFalse(customization.customStyles().isEmpty());

        String customStyleValue = customization.customStyles().get(CUSTOM_STYLE_NAME);
        assertEquals(prefix(tenantId) + CUSTOM_STYLE_VALUE, customStyleValue);
    }
}
