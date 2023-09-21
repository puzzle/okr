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

    public Map<String, String> getConfigBasedOnActiveEnv() {
        HashMap<String, String> env = new HashMap<>();
        env.put("activeProfile", activeProfile);
        env.put("issuer", issuer);
        // TODO: implement correct scopes
        return env;
    }

}
