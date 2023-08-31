package ch.puzzle.okr.clientconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class ClientConfigService {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public HashMap<String, String> getConfigBasedOnActiveEnv(org.springframework.core.env.Environment environment) {
        HashMap<String, String> env = new HashMap<>();
        env.put("activeProfile", activeProfile);
        env.put("issuer", issuer);
        return env;
    }

}
