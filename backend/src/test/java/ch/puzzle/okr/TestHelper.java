package ch.puzzle.okr;

import org.springframework.security.oauth2.jwt.Jwt;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class TestHelper {
    private TestHelper() {

    }

    public static Jwt mockJwtToken(String username, String firstname, String lastname, String email) {
        String exampleToken = "MockToken";

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        Map<String, Object> claims = new HashMap<>();
        claims.put("preferred_username", username);
        claims.put("given_name", firstname);
        claims.put("family_name", lastname);
        claims.put("email", email);
        claims.put("exp", Instant.now().plusSeconds(3600).getEpochSecond()); // Expires in 1 hour

        return new Jwt(exampleToken, Instant.now(), Instant.now().plusSeconds(3600), headers, claims);
    }
}
