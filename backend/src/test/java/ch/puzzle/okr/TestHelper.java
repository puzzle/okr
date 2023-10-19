package ch.puzzle.okr;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationReadRole;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.authorization.AuthorizationWriteRole;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.util.CollectionUtils;

import java.time.Instant;
import java.util.*;

import static ch.puzzle.okr.models.authorization.AuthorizationReadRole.READ_ALL_DRAFT;
import static ch.puzzle.okr.models.authorization.AuthorizationReadRole.READ_ALL_PUBLISHED;
import static ch.puzzle.okr.models.authorization.AuthorizationWriteRole.WRITE_ALL;

public class TestHelper {
    private TestHelper() {
    }

    public static User defaultUser(Long id) {
        return User.Builder.builder().withId(id).withFirstname("Bob").withLastname("Kaufmann").withUsername("bkaufmann")
                .withEmail("kaufmann@puzzle.ch").build();
    }

    public static AuthorizationUser defaultAuthorizationUser() {
        return mockAuthorizationUser(1L, "bkaufmann", "Bob", "Kaufmann", "kaufmann@puzzle.ch", List.of(5L), 5L,
                List.of(READ_ALL_PUBLISHED, READ_ALL_DRAFT), List.of(WRITE_ALL));
    }

    public static AuthorizationUser mockAuthorizationUser(User user, List<Long> teamIds, Long firstLevelTeamId,
            List<AuthorizationReadRole> readRoles, List<AuthorizationWriteRole> writeRoles) {
        return mockAuthorizationUser(user.getId(), user.getUsername(), user.getFirstname(), user.getLastname(),
                user.getEmail(), teamIds, firstLevelTeamId, readRoles, writeRoles);
    }

    public static AuthorizationUser mockAuthorizationUser(Long id, String username, String firstname, String lastname,
            String email, List<Long> teamIds, Long firstLevelTeamId, List<AuthorizationReadRole> readRoles,
            List<AuthorizationWriteRole> writeRoles) {
        return new AuthorizationUser(User.Builder.builder().withId(id).withUsername(username).withFirstname(firstname)
                .withLastname(lastname).withEmail(email).build(), teamIds, firstLevelTeamId, readRoles, writeRoles);
    }

    public static Jwt defaultJwtToken() {
        return mockJwtToken("bkaufmann", "Bob", "Kaufmann", "kaufmann@puzzle.ch", List.of("org_gl"));
    }

    public static Jwt mockJwtToken(String username, String firstname, String lastname, String email) {
        return mockJwtToken(username, firstname, lastname, email, List.of());
    }

    public static Jwt mockJwtToken(User user, List<String> roles) {
        return mockJwtToken(user.getUsername(), user.getFirstname(), user.getLastname(), user.getEmail(), roles);
    }

    public static Jwt mockJwtToken(String username, String firstname, String lastname, String email,
            List<String> roles) {
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
        if (!CollectionUtils.isEmpty(roles)) {
            Map<String, Collection<String>> realmAccess = new HashMap<>();
            realmAccess.put("roles", new ArrayList<>(roles));
            claims.put("pict", realmAccess);
        }

        return new Jwt(exampleToken, Instant.now(), Instant.now().plusSeconds(3600), headers, claims);
    }
}
