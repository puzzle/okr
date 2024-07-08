package ch.puzzle.okr.service.clientconfig;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ClientConfigServiceTest {

    @DisplayName("getConfigBasedOnActiveEnv() should be successful when tenant is configured")
    @ParameterizedTest
    @MethodSource("tenantConfiguration")
    void getConfigBasedOnActiveEnvShouldBeSuccessfulWhenTenantIsConfigured(String tenant, String hostname) {
        // arrange
        ClientCustomizationProperties properties = createPropertiesForTenant(tenant);
        TenantConfigProvider.TenantConfig tenantConfig = getTenantConfig(tenant);
        ClientConfigService service = getClientConfigServiceWithConfiguredTenantId(properties, tenant, tenantConfig);

        // act
        ClientConfigDto configBasedOnActiveEnv = service.getConfigBasedOnActiveEnv(hostname);

        // assert
        assertClientConfigDto(configBasedOnActiveEnv, tenant);
    }

    private void assertClientConfigDto(ClientConfigDto clientConfigDto, String tenant) {
        assertNotNull(clientConfigDto);
        assertEquals(tenant + "_issuerUrl", clientConfigDto.issuer());
        assertEquals(tenant + "_clientId", clientConfigDto.clientId());
        assertEquals(tenant + "_favicon", clientConfigDto.favicon());
        assertEquals(tenant + "_logo", clientConfigDto.logo());
        assertEquals(tenant + "_title", clientConfigDto.title());
    }

    @DisplayName("getConfigBasedOnActiveEnv() should throw exception when tenant is not configured")
    @Test
    void getConfigBasedOnActiveEnvShouldThrowExceptionWhenTenantIsNotConfigured() {
        // arrange
        String tenantWithoutConfig = "ohneconfig";
        String hostname = "ohneconfig.ork.ch";

        ClientCustomizationProperties properties = createPropertiesForTenant(tenantWithoutConfig);
        ClientConfigService service = getClientConfigServiceWhichHasNotConfiguredTenantId(properties,
                tenantWithoutConfig);

        // act + assert
        EntityNotFoundException entityNotFoundException = assertThrows(EntityNotFoundException.class, () -> {
            service.getConfigBasedOnActiveEnv(hostname);
        });

        String expectedErrorMessage = "Could not find tenant for subdomain:" + tenantWithoutConfig;
        assertEquals(expectedErrorMessage, entityNotFoundException.getMessage());
    }

    private ClientConfigService getClientConfigServiceWithConfiguredTenantId(ClientCustomizationProperties properties,
            String tenantId, TenantConfigProvider.TenantConfig tenantConfig) {
        return configureClientConfigService(properties, tenantId, tenantConfig);
    }

    private ClientConfigService getClientConfigServiceWhichHasNotConfiguredTenantId(
            ClientCustomizationProperties properties, String tenantId) {
        return configureClientConfigService(properties, tenantId, null);
    }

    private ClientConfigService configureClientConfigService(ClientCustomizationProperties properties, String tenantId,
            TenantConfigProvider.TenantConfig tenantConfig) {
        TenantConfigProvider tenantConfigProvider = mock(TenantConfigProvider.class);
        ClientConfigService service = new ClientConfigService(properties, tenantConfigProvider);
        when(tenantConfigProvider.getTenantConfigById(tenantId)).thenReturn(Optional.ofNullable(tenantConfig));
        return service;
    }

    private static Stream<Arguments> tenantConfiguration() {
        return Stream.of(Arguments.of("pitc", "pitc.ork.ch"), Arguments.of("acme", "acme-ork.ch"));
    }

    private static TenantConfigProvider.TenantConfig getTenantConfig(String tenant) {
        return new TenantConfigProvider.TenantConfig(tenant + "_tenantId", new String[] {}, tenant + "_jwkSetUri",
                tenant + "_issuerUrl", tenant + "_clientId", null);
    }

    private static ClientCustomizationProperties createPropertiesForTenant(String tenant) {
        return new ClientCustomizationProperties() {
            @Override
            public String getFavicon() {
                return tenant + "_favicon";
            }

            @Override
            public String getLogo() {
                return tenant + "_logo";
            }

            @Override
            public HashMap<String, String> getCustomStyles() {
                return new HashMap<>();
            }

            @Override
            public String getTitle() {
                return tenant + "_title";
            }
        };
    }
}
