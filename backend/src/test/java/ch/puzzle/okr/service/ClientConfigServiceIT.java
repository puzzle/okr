package ch.puzzle.okr.service;

import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringIntegrationTest
public class ClientConfigServiceIT {

    @Autowired
    private ClientConfigService clientConfigService;

    @Test
    void saveKeyResult_ShouldSaveNewKeyResult() {
        Map<String, String> configMap = clientConfigService.getConfigBasedOnActiveEnv();

        assertEquals("prod", configMap.get("activeProfile"));
        assertEquals("https://idp-mock-okr.ocp-internal.cloudscale.puzzle.ch/realms/pitc", configMap.get("issuer"));
    }

}
