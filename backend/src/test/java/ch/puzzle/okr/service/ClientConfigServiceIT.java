package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.service.clientconfig.ClientConfigService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringIntegrationTest
class ClientConfigServiceIT {

    @Autowired
    private ClientConfigService clientConfigService;

    @Test
    void saveKeyResultShouldSaveNewKeyResult() {
        ClientConfigDto clientConfig = clientConfigService.getConfigBasedOnActiveEnv();

        assertEquals("prod", clientConfig.activeProfile());
        assertEquals("http://localhost:8544/realms/pitc", clientConfig.issuer());
    }

}
