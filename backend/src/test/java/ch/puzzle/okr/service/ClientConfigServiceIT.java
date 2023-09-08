package ch.puzzle.okr.service;

import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringIntegrationTest
public class ClientConfigServiceIT {

    @Autowired
    private ClientConfigService clientConfigService;

    @Autowired
    private org.springframework.core.env.Environment environment;

    @Test
    void saveKeyResult_ShouldSaveNewKeyResult() {
        HashMap<String, String> configMap = clientConfigService.getConfigBasedOnActiveEnv(environment);

        assertEquals("prod", configMap.get("activeProfile"));
        assertEquals("http://localhost:8000", configMap.get("issuer"));
    }

}
