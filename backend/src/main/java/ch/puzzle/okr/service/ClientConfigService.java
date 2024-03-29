package ch.puzzle.okr.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ClientConfigService {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;

    public Map<String, String> getConfigBasedOnActiveEnv() {
        HashMap<String, String> env = new HashMap<>();
        env.put("activeProfile", activeProfile);
        env.put("issuer", issuer);
        env.put("clientId", clientId);
        return env;
    }

}
