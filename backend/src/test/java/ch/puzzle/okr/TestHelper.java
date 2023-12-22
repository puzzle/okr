package ch.puzzle.okr;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;

public class TestHelper {
    private TestHelper() {
    }

    private static final String FIRSTNAME = "Bob";
    private static final String LASTNAME = "Kaufmann";
    private static final String EMAIL = "kaufmann@puzzle.ch";

    public static User defaultUser(Long id) {
        return User.Builder.builder().withId(id).withFirstname(FIRSTNAME).withLastname(LASTNAME).withEmail(EMAIL)
                .build();
    }

    public static AuthorizationUser defaultAuthorizationUser() {
        return mockAuthorizationUser(1L, FIRSTNAME, LASTNAME, EMAIL);
    }

    public static AuthorizationUser userWithoutWriteAllRole() {
        return mockAuthorizationUser(1L, FIRSTNAME, LASTNAME, EMAIL);
    }

    public static AuthorizationUser mockAuthorizationUser(User user) {
        return mockAuthorizationUser(user.getId(), user.getFirstname(), user.getLastname(), user.getEmail());
    }

    public static AuthorizationUser mockAuthorizationUser(Long id, String firstname, String lastname, String email) {
        return new AuthorizationUser(User.Builder.builder().withId(id).withFirstname(firstname).withLastname(lastname)
                .withEmail(email).build());
    }

    public static Jwt defaultJwtToken() {
        return mockJwtToken(FIRSTNAME, LASTNAME, EMAIL, List.of("org_gl"));
    }

    public static Jwt mockJwtToken(String firstname, String lastname, String email) {
        return mockJwtToken(firstname, lastname, email, List.of());
    }

    public static Jwt mockJwtToken(User user, List<String> roles) {
        return mockJwtToken(user.getFirstname(), user.getLastname(), user.getEmail(), roles);
    }

    public static Jwt mockJwtToken(String firstname, String lastname, String email, List<String> roles) {
        String exampleToken = "MockToken";

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        Map<String, Object> claims = new HashMap<>();
        claims.put("given_name", firstname);
        claims.put("family_name", lastname);
        claims.put("email", email);
        claims.put("exp", Instant.now().plusSeconds(3600).getEpochSecond()); // Expires in 1 hour
        if (!CollectionUtils.isEmpty(roles)) {
            Map<String, Collection<String>> realmAccess = new HashMap<>();
            realmAccess.put("roles", new ArrayList<>(roles));
            claims.put("pitc", realmAccess);
        }

        return new Jwt(exampleToken, Instant.now(), Instant.now().plusSeconds(3600), headers, claims);
    }

    public static List<String> getAllErrorKeys(List<ErrorDto> errors) {
        return errors.stream().map(ErrorDto::errorKey).toList();
    }
}
