package ch.puzzle.okr.test;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.dto.UserDto;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import org.springframework.security.oauth2.jwt.Jwt;

public class TestHelper {
    private TestHelper() {
    }

    public static final String SCHEMA_PITC = "pitc";

    private static final String FIRST_NAME = "Bob";
    private static final String LAST_NAME = "Kaufmann";
    private static final String EMAIL = "kaufmann@puzzle.ch";

    public static User defaultUser(Long id) {
        return User.Builder
                .builder()
                .withId(id)
                .withFirstName(FIRST_NAME)
                .withLastName(LAST_NAME)
                .withEmail(EMAIL)
                .build();
    }

    public static User userWithCustomName(String firstName, String lastName) {
        return User.Builder
                .builder()
                .withFirstName(firstName)
                .withLastName(lastName)
                .withEmail(firstName+"."+lastName+"@puzzle.ch")
                .build();
    }

    public static User defaultOkrChampion(Long id) {
        var user = defaultUser(id);
        user.setOkrChampion(true);
        return user;
    }

    public static User glUser() {
        return glUser(61L);
    }

    public static User bbtUser() {
        return User.Builder
                .builder()
                .withId(71L)
                .withFirstName("Ashleigh")
                .withLastName("Russell")
                .withEmail("bbt@bbt.com")
                .build();
    }

    public static User glUser(Long id) {
        return User.Builder
                .builder()
                .withId(id)
                .withFirstName("Jaya")
                .withLastName("Norris")
                .withEmail("gl@gl.com")
                .build();
    }

    public static User invalidUser() {
        return User.Builder
                .builder()
                .withId(-1L)
                .withFirstName("Invalid")
                .withLastName("User")
                .withEmail("invalid@user.ch")
                .build();
    }

    public static UserDto glUserDto() {
        return new UserDto(61L, 1, "Jaya", "Norris", "gl@gl.com", List.of(), false);
    }

    public static User defaultUserWithTeams(Long userId, List<Team> adminTeams, List<Team> memberTeams) {
        var user = defaultUser(userId);
        var adminUserTeams = adminTeams
                .stream()
                .map(t -> UserTeam.Builder.builder().isTeamAdmin(true).withTeam(t).withUser(user).build());
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

    public static AuthorizationUser okrChampionAuthorizationUser() {
        return mockAuthorizationUser(1L, FIRST_NAME, LAST_NAME, EMAIL, true);
    }

    public static AuthorizationUser defaultAuthorizationUser() {
        return mockAuthorizationUser(1L, FIRST_NAME, LAST_NAME, EMAIL, false);
    }

    public static AuthorizationUser mockAuthorizationUser(User user) {
        return mockAuthorizationUser(user
                .getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.isOkrChampion());
    }

    public static AuthorizationUser mockAuthorizationUser(Long id, String firstName, String lastName, String email,
                                                          boolean isOkrChampion) {
        User user = User.Builder
                .builder() //
                .withId(id) //
                .withFirstName(firstName) //
                .withLastName(lastName) //
                .withEmail(email) //
                .isOkrChampion(isOkrChampion) //
                .build();
        user.setUserTeamList(List.of(defaultUserTeam(1L, user)));
        return new AuthorizationUser(user);
    }

    public static Jwt bbtJwtToken() {
        return mockJwtToken(bbtUser());
    }

    public static Jwt glJwtToken() {
        return mockJwtToken(glUser());
    }

    public static Jwt mockJwtToken(User user) {
        return mockJwtToken(user.getFirstName(), user.getLastName(), user.getEmail());
    }

    public static Jwt mockJwtToken(String firstName, String lastName, String email) {
        String exampleToken = "MockToken";

        Map<String, Object> headers = new HashMap<>();
        headers.put("alg", "HS256");
        headers.put("typ", "JWT");

        Map<String, Object> claims = new HashMap<>();
        claims.put("given_name", firstName);
        claims.put("family_name", lastName);
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

    public static final Unit NUMBER_UNIT = Unit.Builder.builder().unitName("NUMBER").build();
    public static final Unit FTE_UNIT = Unit.Builder.builder().unitName("FTE").build();
    public static final Unit CHF_UNIT = Unit.Builder.builder().unitName("CHF").build();
    public static final Unit EUR_UNIT = Unit.Builder.builder().unitName("EUR").build();
    public static final Unit PERCENT_UNIT = Unit.Builder.builder().unitName("PERCENT").build();
}
