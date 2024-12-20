package ch.puzzle.okr.test;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.security.oauth2.jwt.Jwt;

public class TestHelper {
    private TestHelper() {
    }

    public static final String SCHEMA_PITC = "pitc";

    private static final String FIRSTNAME = "Bob";
    private static final String LASTNAME = "Kaufmann";
    private static final String EMAIL = "kaufmann@puzzle.ch";

    public static User defaultUser(Long id) {
        return User.Builder
                .builder()
                .withId(id)
                .withFirstname(FIRSTNAME)
                .withLastname(LASTNAME)
                .withEmail(EMAIL)
                .build();
    }

    public static User defaultOkrChampion(Long id) {
        var user = defaultUser(id);
        user.setOkrChampion(true);
        return user;
    }

    public static User defaultUserWithTeams(Long userId, List<Team> adminTeams, List<Team> memberTeams) {
        var user = defaultUser(userId);
        var adminUserTeams = adminTeams
                .stream()
                .map(t -> UserTeam.Builder.builder().withTeamAdmin(true).withTeam(t).withUser(user).build());
        var memberUserTeams = memberTeams
                .stream()
                .map(t -> UserTeam.Builder.builder().withTeam(t).withUser(user).build());
        user
                .setUserTeamList(Stream
                        .concat(adminUserTeams, memberUserTeams)
                        .collect(Collectors.toCollection(ArrayList::new)));
        return user;
    }

    public static Team defaultTeam(Long id) {
        return Team.Builder.builder().withId(id).withName("Test Team").build();
    }

    public static UserTeam defaultUserTeam(Long id, User user) {
        return UserTeam.Builder.builder().withId(id).withTeam(defaultTeam(1L)).withUser(user).build();
    }

    public static AuthorizationUser defaultAuthorizationUser() {
        return mockAuthorizationUser(1L, FIRSTNAME, LASTNAME, EMAIL, false);
    }

    public static AuthorizationUser mockAuthorizationUser(User user) {
        return mockAuthorizationUser(user
                .getId(), user.getFirstname(), user.getLastname(), user.getEmail(), user.isOkrChampion());
    }

    public static AuthorizationUser mockAuthorizationUser(Long id, String firstname, String lastname, String email,
                                                          boolean isOkrChampion) {
        User user = User.Builder
                .builder() //
                .withId(id) //
                .withFirstname(firstname) //
                .withLastname(lastname) //
                .withEmail(email) //
                .withOkrChampion(isOkrChampion) //
                .build();
        user.setUserTeamList(List.of(defaultUserTeam(1L, user)));
        return new AuthorizationUser(user);
    }

    public static Jwt defaultJwtToken() {
        return mockJwtToken(FIRSTNAME, LASTNAME, EMAIL);
    }

    public static Jwt mockJwtToken(User user) {
        return mockJwtToken(user.getFirstname(), user.getLastname(), user.getEmail());
    }

    public static Jwt mockJwtToken(String firstname, String lastname, String email) {
        String exampleToken = "MockToken";

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        Map<String, Object> claims = new HashMap<>();
        claims.put("given_name", firstname);
        claims.put("family_name", lastname);
        claims.put("email", email);
        claims.put("exp", Instant.now().plusSeconds(3600).getEpochSecond()); // Expires in 1 hour

        return new Jwt(exampleToken, Instant.now(), Instant.now().plusSeconds(3600), headers, claims);
    }

    public static List<String> getAllErrorKeys(List<ErrorDto> errors) {
        return errors.stream().map(ErrorDto::errorKey).toList();
    }

    public static JsonParser createJsonParser(String json) throws IOException {
        return new ObjectMapper().getFactory().createParser(json);
    }

    public static JsonNode getJsonNode(String json) throws IOException {
        return new ObjectMapper().readTree(json);
    }
}
