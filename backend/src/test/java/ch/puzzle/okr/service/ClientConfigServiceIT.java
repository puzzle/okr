package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.service.clientconfig.ClientConfigService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringIntegrationTest
@SpringBootTest(properties = { "okr.clientcustomization.customstyles.okr-topbar-background-color=#affe00",
        "okr.clientcustomization.customstyles.okr-other-css-style=rgba(50,60,70,0.5)", })
class ClientConfigServiceIT {

    @Autowired
    private ClientConfigService clientConfigService;

    @Test
    void getConfigBasedOnActiveEnv_validSubdomain_returnsCorrectTenantConfig() {
        ClientConfigDto clientConfig = clientConfigService.getConfigBasedOnActiveEnv("pitc.okr.puzzle.ch");

        assertEquals("prod", clientConfig.activeProfile());
        assertEquals("http://localhost:8544/realms/pitc", clientConfig.issuer());
        assertEquals("pitc_okr_staging", clientConfig.clientId());
    }

    @Test
    void getConfigBasedOnActiveEnv_validAlternativeSubdomain_returnsCorrectConfig() {
        ClientConfigDto clientConfig =  clientConfigService.getConfigBasedOnActiveEnv("acme.okr.puzzle.ch");

        assertEquals("prod", clientConfig.activeProfile());
        assertEquals("http://localhost:8544/realms/pitc", clientConfig.issuer());
        assertEquals("acme_okr_staging", clientConfig.clientId());
    }

    @Test
    void getConfigBasedOnActiveEnv_invalidSubdomain_throwsException() {
        assertThrowsExactly(EntityNotFoundException.class,
                () -> clientConfigService.getConfigBasedOnActiveEnv("foobar.okr.puzzle.ch"));
    }

    @Test
    void getClientConfig_withOtherValues_returnsRightValues() {
        ClientConfigDto clientConfig = clientConfigService.getConfigBasedOnActiveEnv("foobar.okr.puzzle.ch");

        assertEquals("prod", clientConfig.activeProfile());
        assertEquals("http://localhost:8544/realms/pitc", clientConfig.issuer());
        assertEquals("assets/favicon.png", clientConfig.favicon());
        assertEquals("assets/images/okr-logo.svg", clientConfig.logo());
        assertEquals("#affe00", clientConfig.customStyles().get("okr-topbar-background-color"));
        assertEquals("rgba(50,60,70,0.5)", clientConfig.customStyles().get("okr-other-css-style"));
    }



}
