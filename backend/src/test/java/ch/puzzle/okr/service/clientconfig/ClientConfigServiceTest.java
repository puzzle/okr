package ch.puzzle.okr.service.clientconfig;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import ch.puzzle.okr.multitenancy.customization.TenantClientCustomization;
import ch.puzzle.okr.multitenancy.customization.TenantClientCustomizationProvider;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.Optional;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

public class ClientConfigServiceTest {

    @ParameterizedTest(name = "Should be successful on getConfigBasedOnActiveEnv() when tenant is configured properly with tenant {0} and hostName {1}")
    @CsvSource({ "pitc,pitc.ork.ch", "acme,acme-ork.ch" })
    void getConfigBasedOnActiveEnvShouldBeSuccessfulWhenTenantIsConfiguredProperly(String tenant, String hostName) {
        // arrange
        TenantConfigProvider.TenantConfig tenantConfig = getTenantConfig(tenant);
        TenantClientCustomization tenantCustomization = getTenantClientCustomization(tenant);
        ClientConfigService service = getClientConfig(tenantConfig, tenantCustomization, tenant);

        // act
        ClientConfigDto configBasedOnActiveEnv = service.getConfigBasedOnActiveEnv(hostName);

        // assert
        assertClientConfigDto(configBasedOnActiveEnv, tenant);
    }

    @ParameterizedTest(name = "Should throw exception on getConfigBasedOnActiveEnv() when client customization is not found for tenant {0}, hostName {1} and subDomain {2}")
    @CsvSource({ "pitc,pitc.okr.ch,pitc", "acme,acme-okr.ch,acme-okr" })
    void getConfigBasedOnActiveEnvShouldThrowExceptionIfClientCustomizationIsNotFound(String tenant, String hostName,
                                                                                      String subDomain) {
        // arrange
        TenantConfigProvider.TenantConfig tenantConfig = getTenantConfig(tenant);
        ClientConfigService service = getClientConfig(tenantConfig, tenant);

        // act + assert
        EntityNotFoundException entityNotFoundException = //
                assertThrows(EntityNotFoundException.class, () -> service.getConfigBasedOnActiveEnv(hostName));

        String expectedErrorMessage = "Could not find tenant client customization for subDomain:" + subDomain;
        assertEquals(expectedErrorMessage, entityNotFoundException.getMessage());
    }

    @ParameterizedTest(name = "Should throw exception getConfigBasedOnActiveEnv when client config is not found for tenant {0}, hostname {1} and subDomain {2} ")
    @CsvSource({ "pitc,pitc.okr.ch,pitc", "acme,acme-okr.ch,acme-okr" })
    void getConfigBasedOnActiveEnvShouldThrowExceptionIfClientConfigIsNotFound(String tenant, String hostname,
                                                                               String subDomain) {
        // arrange
        TenantClientCustomization tenantCustomization = getTenantClientCustomization(tenant);
        ClientConfigService service = getClientConfig(tenantCustomization, tenant);

        // act + assert
        EntityNotFoundException entityNotFoundException = //
                assertThrows(EntityNotFoundException.class, () -> service.getConfigBasedOnActiveEnv(hostname));

        String expectedErrorMessage = "Could not find tenant config for subDomain:" + subDomain;
        assertEquals(expectedErrorMessage, entityNotFoundException.getMessage());
    }

    private ClientConfigService getClientConfig(TenantConfigProvider.TenantConfig tenantConfig,
                                                TenantClientCustomization tenantClientCustomization, String tenantId) {
        return mockClientConfigService(tenantConfig, tenantClientCustomization, tenantId);
    }

    private ClientConfigService getClientConfig(TenantClientCustomization tenantClientCustomization, String tenantId) {
        return mockClientConfigService(null, tenantClientCustomization, tenantId);
    }

    private ClientConfigService getClientConfig(TenantConfigProvider.TenantConfig tenantConfig, String tenantId) {
        return mockClientConfigService(tenantConfig, null, tenantId);
    }

    private ClientConfigService mockClientConfigService(TenantConfigProvider.TenantConfig tenantConfig,
                                                        TenantClientCustomization tenantCustomization,
                                                        String tenantId) {

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
                                                     new String[]{}, //
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
                                             prefix(tenantId) + "helpSiteUrl", //
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
