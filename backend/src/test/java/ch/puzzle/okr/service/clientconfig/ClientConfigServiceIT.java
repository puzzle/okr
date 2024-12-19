package ch.puzzle.okr.service.clientconfig;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.test.SpringIntegrationTest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringIntegrationTest
@SpringBootTest()
class ClientConfigServiceIT {

    @Autowired
    private ClientConfigService clientConfigService;

    @ParameterizedTest(name = "Should get correct config on getConfigBasedOnActiveEnv() based on the hostname {0}, active profile {1}, issuer {2} and client id {3}")
    @MethodSource("tenantConfigs")
    void getConfigBasedOnActiveEnvWithValidSubdomainReturnsCorrectTenantConfig(String hostname, String activeProfile,
            String issuer, String clientId) {

        // arrange + act
        ClientConfigDto clientConfig = clientConfigService.getConfigBasedOnActiveEnv(hostname);

        // assert
        assertEquals(activeProfile, clientConfig.activeProfile());
        assertEquals(issuer, clientConfig.issuer());
        assertEquals(clientId, clientConfig.clientId());
    }

    private static Stream<Arguments> tenantConfigs() {
        return Stream.of(
                Arguments.of("pitc.okr.puzzle.ch", "prod", "http://localhost:8544/realms/pitc", "pitc_okr_staging"),
                Arguments.of("acme.okr.puzzle.ch", "prod", "http://localhost:8544/realms/pitc", "acme_okr_staging"));
    }

    @DisplayName("Should throw exception on getConfigBasedOnActiveEnv() when subdomain is invalid")
    @Test
    void getConfigBasedOnActiveEnvWithInvalidSubdomainThrowsException() {
        assertThrowsExactly(EntityNotFoundException.class,
                () -> clientConfigService.getConfigBasedOnActiveEnv("foobar.okr.puzzle.ch"));
    }

    @DisplayName("Should return config with correct values on getConfigBasedOnActiveEnv()")
    @Test
    void getClientConfigWithOtherValuesReturnsCorrectValues() {
        // arrange + act
        ClientConfigDto clientConfig = clientConfigService.getConfigBasedOnActiveEnv("pitc.okr.puzzle.ch");

        // assert
        assertEquals("prod", clientConfig.activeProfile());
        assertEquals("http://localhost:8544/realms/pitc", clientConfig.issuer());
        assertEquals("assets/favicon.png", clientConfig.favicon());
        assertEquals("assets/images/okr-logo.svg", clientConfig.logo());
        assertEquals("assets/images/triangles-okr-header.svg", clientConfig.triangles());
        assertEquals("assets/images/puzzle-p.svg", clientConfig.backgroundLogo());
        assertEquals("Puzzle OKR", clientConfig.title());
        assertEquals("#1e5a96", clientConfig.customStyles().get("okr-topbar-background-color"));
    }

}
