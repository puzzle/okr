package ch.puzzle.okr.clientconfig;

import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;

@Service
public class ClientConfigService {

    public HashMap<String, String> getConfigBasedOnActiveEnv(org.springframework.core.env.Environment environment) {
        HashMap<String, String> env = new HashMap<>();
        if (Arrays.stream(environment.getActiveProfiles()).toList().contains("prod")) {
            env.put("issuer", "https://sso.puzzle.ch/auth/realms/pitc");
        } else if (Arrays.stream(environment.getActiveProfiles()).toList().contains("staging")) {
            env.put("issuer", "http://staging-okr-oidc-mock:8000");
        } else {
            env.put("issuer", "http://localhost:8000");
        }
        return env;
    }

}
