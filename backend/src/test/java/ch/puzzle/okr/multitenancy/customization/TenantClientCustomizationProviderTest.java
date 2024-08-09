package ch.puzzle.okr.multitenancy.customization;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TenantClientCustomizationProviderTest {

    @DisplayName("extractCssNameFromPropertyName() should return css name for valid property name")
    @ParameterizedTest
    @CsvSource({ "pitc", "acme" })
    void extractCssNameFromPropertyNameShouldReturnCssNameForValidPropertyName(String tenantId) {
        // arrange
        String propertyNameWithTenant = "okr.tenants." + tenantId
                + ".clientcustomization.customstyles.my-css-property-name";
        TenantClientCustomizationProvider provider = new TenantClientCustomizationProvider(new String[] {}, null);

        // act
        String cssName = provider.extractCssNameFromPropertyName(propertyNameWithTenant, tenantId);

        // assert
        assertEquals("my-css-property-name", cssName);
    }

    @DisplayName("extractCssNameFromPropertyName() should throw IllegalArgumentException for invalid property name")
    @Test
    void extractCssNameFromPropertyNameShouldThrowIllegalArgumentExceptionForInvalidPropertyName() {
        // arrange
        String propertyNameWithoutTenant = "okr.tenants.clientcustomization.customstyles.my-css-property-name";
        TenantClientCustomizationProvider provider = new TenantClientCustomizationProvider(new String[] {}, null);

        // act + assert
        assertThrows(IllegalArgumentException.class,
                () -> provider.extractCssNameFromPropertyName(propertyNameWithoutTenant, "pitc"));
    }
}
