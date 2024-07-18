package ch.puzzle.okr.service.clientconfig;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import ch.puzzle.okr.multitenancy.customization.TenantClientCustomization;
import ch.puzzle.okr.multitenancy.customization.TenantClientCustomizationProvider;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.HashMap;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@Disabled
public class ClientConfigServiceTest {

    @DisplayName("getConfigBasedOnActiveEnv() should be successful when tenant is configured")
    @ParameterizedTest
    @CsvSource({ "pitc,pitc.ork.ch", "acme,acme-ork.ch" })
    void getConfigBasedOnActiveEnvShouldBeSuccessfulWhenTenantIsConfigured(String tenant, String hostname) {
        // arrange
        TenantClientCustomization tenantCustomization = getTenantClientCustomization(tenant);
        TenantConfigProvider.TenantConfig tenantConfig = getTenantConfig(tenant);
        ClientConfigService service = getClientConfigServiceWithConfiguredTenantId(tenantCustomization, tenantConfig,
                tenant);

        // act
        ClientConfigDto configBasedOnActiveEnv = service.getConfigBasedOnActiveEnv(hostname);

        // assert
        assertClientConfigDto(configBasedOnActiveEnv, tenant);
    }

    @DisplayName("getConfigBasedOnActiveEnv() should throw exception when tenant is not configured")
    @ParameterizedTest
    @CsvSource({ "ohneconfig,ohneconfig.ork.ch" })
    void getConfigBasedOnActiveEnvShouldThrowExceptionWhenTenantIsNotConfigured(String tenantWithoutConfig,
            String hostname) {
        // arrange
        TenantClientCustomization tenantCustomization = getTenantClientCustomization(tenantWithoutConfig);
        ClientConfigService service = getClientConfigServiceWhichHasNotConfiguredTenantId(tenantCustomization,
                tenantWithoutConfig);

        // act + assert
        EntityNotFoundException entityNotFoundException = //
                assertThrows(EntityNotFoundException.class, () -> service.getConfigBasedOnActiveEnv(hostname));

        String expectedErrorMessage = "Could not find tenant config for subdomain:" + tenantWithoutConfig;
        assertEquals(expectedErrorMessage, entityNotFoundException.getMessage());
    }

    private ClientConfigService getClientConfigServiceWithConfiguredTenantId(TenantClientCustomization properties,
            TenantConfigProvider.TenantConfig tenantConfig, String tenantId) {
        return mockClientConfigService(properties, tenantId, tenantConfig);
    }

    private ClientConfigService getClientConfigServiceWhichHasNotConfiguredTenantId(
            TenantClientCustomization properties, String tenantId) {
        return mockClientConfigService(properties, tenantId, null);
    }

    private ClientConfigService mockClientConfigService(TenantClientCustomization tenantCustomization, String tenantId,
            TenantConfigProvider.TenantConfig tenantConfig) {

        TenantClientCustomizationProvider tenantCustomizationProvider = mock(TenantClientCustomizationProvider.class);
        when(tenantCustomizationProvider.getTenantClientCustomizationsById(tenantId)) //
                .thenReturn(Optional.ofNullable(tenantCustomization));

        TenantConfigProvider tenantConfigProvider = mock(TenantConfigProvider.class);
        when(tenantConfigProvider.getTenantConfigById(tenantId)) //
                .thenReturn(Optional.ofNullable(tenantConfig));

        return new ClientConfigService(tenantCustomizationProvider, tenantConfigProvider);
    }

    private TenantConfigProvider.TenantConfig getTenantConfig(String tenantId) {
        return new TenantConfigProvider.TenantConfig( //
                prefix(tenantId) + "tenantId", //
                new String[] {}, //
                prefix(tenantId) + "jwkSetUri", //
                prefix(tenantId) + "issuerUrl", //
                prefix(tenantId) + "clientId", //
                null);
    }

    private TenantClientCustomization getTenantClientCustomization(String tenantId) {
        return new TenantClientCustomization( //
                prefix(tenantId) + "favicon", //
                prefix(tenantId) + "logo", //
                prefix(tenantId) + "triangles", //
                prefix(tenantId) + "backgroundLogo", //
                prefix(tenantId) + "title", //
                new HashMap<>());
    }

    private void assertClientConfigDto(ClientConfigDto clientConfigDto, String tenant) {
        assertNotNull(clientConfigDto);
        assertEquals(prefix(tenant) + "issuerUrl", clientConfigDto.issuer());
        assertEquals(prefix(tenant) + "clientId", clientConfigDto.clientId());
        assertEquals(prefix(tenant) + "favicon", clientConfigDto.favicon());
        assertEquals(prefix(tenant) + "logo", clientConfigDto.logo());
        assertEquals(prefix(tenant) + "triangles", clientConfigDto.triangles());
        assertEquals(prefix(tenant) + "backgroundLogo", clientConfigDto.backgroundLogo());
        assertEquals(prefix(tenant) + "title", clientConfigDto.title());
    }

    private String prefix(String tenantId) {
        return tenantId + "_";
    }
}
