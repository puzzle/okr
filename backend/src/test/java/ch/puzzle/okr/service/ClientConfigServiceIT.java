package ch.puzzle.okr.service;

import ch.puzzle.okr.test.SpringIntegrationTest;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrowsExactly;

@SpringIntegrationTest
class ClientConfigServiceIT {

    @Autowired
    private ClientConfigService clientConfigService;

    @Test
    void getConfigBasedOnActiveEnv_validSubdomain_returnsCorrectTenantConfig() {
        Map<String, String> configMap = clientConfigService.getConfigBasedOnActiveEnv("pitc.okr.puzzle.ch");

        assertEquals("prod", configMap.get("activeProfile"));
        assertEquals("http://localhost:8544/realms/pitc", configMap.get("issuer"));
        assertEquals("pitc_okr_staging", configMap.get("clientId"));
    }

    @Test
    void getConfigBasedOnActiveEnv_validAlternativeSubdomain_returnsCorrectConfig() {
        Map<String, String> configMap = clientConfigService.getConfigBasedOnActiveEnv("acme.okr.puzzle.ch");

        assertEquals("prod", configMap.get("activeProfile"));
        assertEquals("http://localhost:8544/realms/acme", configMap.get("issuer"));
        assertEquals("acme_okr_staging", configMap.get("clientId"));
    }

    @Test
    void getConfigBasedOnActiveEnv_invalidSubdomain_throwsException() {
        assertThrowsExactly(EntityNotFoundException.class,
                () -> clientConfigService.getConfigBasedOnActiveEnv("foobar.okr.puzzle.ch"));
    }

}
