package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {
    @MockBean
    UserPersistenceService userPersistenceService = Mockito.mock(UserPersistenceService.class);

    User user1;
    User userMinimal;
    User user;

    Jwt mockJwt = TestHelper.mockJwtToken("username", "firstname", "lastname", "email@email.com");

    @BeforeEach
    void setUp() {
        user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann").withUsername("bkaufmann")
                .withEmail("kaufmann@puzzle.ch").build();

        userMinimal = User.Builder.builder().withFirstname("Max").withLastname("Mustermann")
                .withEmail("max@mustermann.com").withUsername("mustermann").build();

        when(userPersistenceService.findById(1L)).thenReturn(user);
        when(userPersistenceService.getModelName()).thenReturn("User");
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", userPersistenceService.getModelName(), 2L)))
                        .when(userPersistenceService).findById(2L);
    }

    @Spy
    @InjectMocks
    private UserValidationService validator;

    private static Stream<Arguments> userNameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 21),
                        List.of("Attribute username must have size between 2 and 20 characters when saving user")),
                arguments(StringUtils.repeat('1', 1),
                        List.of("Attribute username must have size between 2 and 20 characters when saving user")),
                arguments("",
                        List.of("Missing attribute username when saving user",
                                "Attribute username must have size between 2 and 20 characters when saving user")),
                arguments(" ",
                        List.of("Missing attribute username when saving user",
                                "Attribute username must have size between 2 and 20 characters when saving user")),
                arguments("         ", List.of("Missing attribute username when saving user")),
                arguments(null, List.of("Missing attribute username when saving user",
                        "Attribute username can not be null when saving user")));
    }

    private static Stream<Arguments> firstNameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 51),
                        List.of("Attribute firstname must have size between 2 and 50 characters when saving user")),
                arguments(StringUtils.repeat('1', 1),
                        List.of("Attribute firstname must have size between 2 and 50 characters when saving user")),
                arguments("",
                        List.of("Missing attribute firstname when saving user",
                                "Attribute firstname must have size between 2 and 50 characters when saving user")),
                arguments(" ",
                        List.of("Missing attribute firstname when saving user",
                                "Attribute firstname must have size between 2 and 50 characters when saving user")),
                arguments("         ", List.of("Missing attribute firstname when saving user")),
                arguments(null, List.of("Missing attribute firstname when saving user",
                        "Attribute firstname can not be null when saving user")));
    }

    private static Stream<Arguments> lastNameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 51),
                        List.of("Attribute lastname must have size between 2 and 50 characters when saving user")),
                arguments(StringUtils.repeat('1', 1),
                        List.of("Attribute lastname must have size between 2 and 50 characters when saving user")),
                arguments("",
                        List.of("Missing attribute lastname when saving user",
                                "Attribute lastname must have size between 2 and 50 characters when saving user")),
                arguments(" ",
                        List.of("Missing attribute lastname when saving user",
                                "Attribute lastname must have size between 2 and 50 characters when saving user")),
                arguments("         ", List.of("Missing attribute lastname when saving user")),
                arguments(null, List.of("Missing attribute lastname when saving user",
                        "Attribute lastname can not be null when saving user")));
    }

    private static Stream<Arguments> emailValidationArguments() {
        return Stream.of(
                arguments(("1".repeat(251)),
                        List.of("Attribute email must have size between 2 and 250 characters when saving user",
                                "Attribute email should be valid when saving user")),
                arguments(("1"),
                        List.of("Attribute email should be valid when saving user",
                                "Attribute email must have size between 2 and 250 characters when saving user")),
                arguments((""),
                        List.of("Missing attribute email when saving user",
                                "Attribute email must have size between 2 and 250 characters when saving user")),
                arguments((" "),
                        List.of("Missing attribute email when saving user",
                                "Attribute email should be valid when saving user",
                                "Attribute email must have size between 2 and 250 characters when saving user")),
                arguments(("       "),
                        List.of("Missing attribute email when saving user",
                                "Attribute email should be valid when saving user")),
                arguments(null, List.of("Attribute email can not be null when saving user",
                        "Missing attribute email when saving user")));
    }

    @Test
    void validateOnGet_ShouldBeSuccessfulWhenValidUserId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGet_ShouldThrowExceptionIfUserIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldBeSuccessfulWhenUserIsValid() {
        validator.validateOnCreate(userMinimal);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(userMinimal);
        verify(validator, times(1)).validate(userMinimal);
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model User is null", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenIdIsNotNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(user));

        assertEquals("Model User cannot have id while create. Found id 1", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("userNameValidationArguments")
    void validateOnCreate_ShouldThrowExceptionWhenUsernameIsInvalid(String name, List<String> errors) {
        User user2 = User.Builder.builder().withEmail("max@mail.com").withFirstname("firstname")
                .withLastname("lastname").withUsername(name).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(user2));

        String[] exceptionParts = Objects.requireNonNull(exception.getReason()).split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @ParameterizedTest
    @MethodSource("firstNameValidationArguments")
    void validateOnCreate_ShouldThrowExceptionWhenFirstnameIsInvalid(String name, List<String> errors) {
        User user2 = User.Builder.builder().withEmail("max@mail.com").withFirstname(name).withLastname("lastname")
                .withUsername("username").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(user2));

        String[] exceptionParts = Objects.requireNonNull(exception.getReason()).split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @ParameterizedTest
    @MethodSource("lastNameValidationArguments")
    void validateOnCreate_ShouldThrowExceptionWhenLastnameIsInvalid(String name, List<String> errors) {
        User user2 = User.Builder.builder().withEmail("max@mail.com").withFirstname("firstname").withLastname(name)
                .withUsername("username").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(user2));

        String[] exceptionParts = Objects.requireNonNull(exception.getReason()).split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @ParameterizedTest
    @MethodSource("emailValidationArguments")
    void validateOnCreate_ShouldThrowExceptionWhenEmailIsInvalid(String email, List<String> errors) {
        User user2 = User.Builder.builder().withEmail(email).withFirstname("firstname").withLastname("lastname")
                .withUsername("username").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(user2));

        String[] exceptionParts = Objects.requireNonNull(exception.getReason()).split("\\.");
        System.out.println(Arrays.toString(Arrays.stream(exceptionParts).toArray()));
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
            System.out.println(errorArray[i].strip());
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenAttrsAreMissing() {
        User userInvalid = User.Builder.builder().withId(null).withUsername("Username").withLastname("Lastname")
                .withFirstname("firstname").withEmail("falseemail").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(userInvalid));

        assertThat(exception.getReason().strip()).contains("Attribute email should be valid when saving user");
    }

    @Test
    void validateOnUpdate_ShouldBeSuccessfulWhenUserIsValid() {
        validator.validateOnUpdate(user.getId(), user);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(user);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(user.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(user.getId(), user.getId());
        verify(validator, times(1)).validate(user);
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals("Given model User is null", exception.getReason());
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, userMinimal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(userMinimal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenIdHasChanged() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(7L, user));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(user);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(user.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(7L, user.getId());
        assertEquals("Id 7 has changed to 1 during update", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("userNameValidationArguments")
    void validateOnUpdate_ShouldThrowExceptionWhenUsernameIsInvalid(String name, List<String> errors) {
        User user2 = User.Builder.builder().withId(3L).withEmail("max@mail.com").withFirstname("firstname")
                .withLastname("lastname").withUsername(name).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, user2));

        String[] exceptionParts = Objects.requireNonNull(exception.getReason()).split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @ParameterizedTest
    @MethodSource("firstNameValidationArguments")
    void validateOnUpdate_ShouldThrowExceptionWhenFirstnameIsInvalid(String name, List<String> errors) {
        User user2 = User.Builder.builder().withId(3L).withEmail("max@mail.com").withFirstname(name)
                .withLastname("lastname").withUsername("username").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, user2));

        String[] exceptionParts = Objects.requireNonNull(exception.getReason()).split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @ParameterizedTest
    @MethodSource("lastNameValidationArguments")
    void validateOnUpdate_ShouldThrowExceptionWhenLastnameIsInvalid(String name, List<String> errors) {
        User user2 = User.Builder.builder().withId(3L).withEmail("max@mail.com").withFirstname("firstname")
                .withLastname(name).withUsername("username").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, user2));

        String[] exceptionParts = Objects.requireNonNull(exception.getReason()).split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @ParameterizedTest
    @MethodSource("emailValidationArguments")
    void validateOnUpdate_ShouldThrowExceptionWhenEmailIsInvalid(String email, List<String> errors) {
        User user2 = User.Builder.builder().withId(3L).withEmail(email).withFirstname("firstname")
                .withLastname("lastname").withUsername("username").build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, user2));

        String[] exceptionParts = Objects.requireNonNull(exception.getReason()).split("\\.");
        System.out.println(Arrays.toString(Arrays.stream(exceptionParts).toArray()));
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
            System.out.println(errorArray[i].strip());
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenAttrsAreMissing() {
        User userInvalid = User.Builder.builder().withId(3L).withUsername("Username").withLastname("Lastname")
                .withFirstname("firstname").withEmail("falseemail").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, userInvalid));

        assertThat(exception.getReason().strip()).contains("Attribute email should be valid when saving user");
    }

    @Test
    void validateAuthorisationToken_ShouldNotThrowError() {
        assertDoesNotThrow(() -> validator.validateAuthorisationToken(mockJwt));

        verify(validator).validateAuthorisationToken(mockJwt);
    }

    @Test
    void validateAuthorisationToken_ShouldThrowErrorWhenNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateAuthorisationToken(null));

        assertEquals("Received token is null", exception.getReason());
    }

    @Test
    void validateOnDelete_ShouldBeSuccessfulWhenValidObjectiveId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDelete_ShouldThrowExceptionIfObjectiveIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

}
