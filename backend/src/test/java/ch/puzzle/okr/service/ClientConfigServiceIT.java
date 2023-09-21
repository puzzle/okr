package ch.puzzle.okr.service;

import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;

import java.util.HashMap;
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
        assertEquals("http://localhost:8000", configMap.get("issuer"));
    }

}
