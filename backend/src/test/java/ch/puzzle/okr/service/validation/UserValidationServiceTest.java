package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
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

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("username", "User", "2", "20")))),
                arguments(StringUtils.repeat('1', 1),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("username", "User", "2", "20")))),
                arguments("",
                        List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("username", "User")),
                                new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("username", "User", "2", "20")))),
                arguments(" ",
                        List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("username", "User")),
                                new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("username", "User", "2", "20")))),
                arguments("         ", List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("username", "User")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("username", "User")),
                        new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("username", "User")))));
    }

    private static Stream<Arguments> firstNameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 51),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("firstname", "User", "2", "50")))),
                arguments(StringUtils.repeat('1', 1),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("firstname", "User", "2", "50")))),
                arguments("",
                        List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("firstname", "User")),
                                new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("firstname", "User", "2", "50")))),
                arguments(" ",
                        List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("firstname", "User")),
                                new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("firstname", "User", "2", "50")))),
                arguments("         ", List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("firstname", "User")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("firstname", "User")),
                        new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("firstname", "User")))));
    }

    private static Stream<Arguments> lastNameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 51),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("lastname", "User", "2", "50")))),
                arguments(StringUtils.repeat('1', 1),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("lastname", "User", "2", "50")))),
                arguments("",
                        List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("lastname", "User")),
                                new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("lastname", "User", "2", "50")))),
                arguments(" ",
                        List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("lastname", "User")),
                                new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("lastname", "User", "2", "50")))),
                arguments("         ", List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("lastname", "User")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("lastname", "User")),
                        new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("lastname", "User")))));
    }

    private static Stream<Arguments> emailValidationArguments() {
        return Stream.of(
                arguments(("1".repeat(251)),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("email", "User", "2", "250")),
                                new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")))),
                arguments(("1"),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("email", "User", "2", "250")),
                                new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")))),
                arguments((""),
                        List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("email", "User")),
                                new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("email", "User", "2", "250")))),
                arguments((" "),
                        List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("email", "User")),
                                new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")),
                                new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("email", "User", "2", "250")))),
                arguments(("       "),
                        List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("email", "User")),
                                new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("email", "User")),
                        new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("email", "User")))));
    }

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidUserId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfUserIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnGetOrCreateShouldBeSuccessful() {
        validator.validateOnGetOrCreate(userMinimal);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(userMinimal);
        verify(validator, times(1)).validate(userMinimal);
    }

    @Test
    void validateOnGetOrCreateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGetOrCreate(null));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenUserIsValid() {
        validator.validateOnCreate(userMinimal);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(userMinimal);
        verify(validator, times(1)).validate(userMinimal);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(user));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("userNameValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenUsernameIsInvalid(String name, List<ErrorDto> errors) {
        User user2 = User.Builder.builder().withEmail("max@mail.com").withFirstname("firstname")
                .withLastname("lastname").withUsername(name).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(user2));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("firstNameValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenFirstnameIsInvalid(String name, List<ErrorDto> errors) {
        User user2 = User.Builder.builder().withEmail("max@mail.com").withFirstname(name).withLastname("lastname")
                .withUsername("username").build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(user2));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("lastNameValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenLastnameIsInvalid(String name, List<ErrorDto> errors) {
        User user2 = User.Builder.builder().withEmail("max@mail.com").withFirstname("firstname").withLastname(name)
                .withUsername("username").build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(user2));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("emailValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenEmailIsInvalid(String email, List<ErrorDto> errors) {
        User user2 = User.Builder.builder().withEmail(email).withFirstname("firstname").withLastname("lastname")
                .withUsername("username").build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(user2));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        User userInvalid = User.Builder.builder().withId(null).withUsername("Username").withLastname("Lastname")
                .withFirstname("firstname").withEmail("falseemail").build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(userInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldBeSuccessfulWhenUserIsValid() {
        validator.validateOnUpdate(user.getId(), user);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(user);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(user.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(user.getId(), user.getId());
        verify(validator, times(1)).validate(user);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, userMinimal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(userMinimal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(7L, user));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(user);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(user.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(7L, user.getId());

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", "7", "1")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("userNameValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenUsernameIsInvalid(String name, List<ErrorDto> errors) {
        User user2 = User.Builder.builder().withId(3L).withEmail("max@mail.com").withFirstname("firstname")
                .withLastname("lastname").withUsername(name).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, user2));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("firstNameValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenFirstnameIsInvalid(String name, List<ErrorDto> errors) {
        User user2 = User.Builder.builder().withId(3L).withEmail("max@mail.com").withFirstname(name)
                .withLastname("lastname").withUsername("username").build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, user2));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("lastNameValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenLastnameIsInvalid(String name, List<ErrorDto> errors) {
        User user2 = User.Builder.builder().withId(3L).withEmail("max@mail.com").withFirstname("firstname")
                .withLastname(name).withUsername("username").build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, user2));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("emailValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenEmailIsInvalid(String email, List<ErrorDto> errors) {
        User user2 = User.Builder.builder().withId(3L).withEmail(email).withFirstname("firstname")
                .withLastname("lastname").withUsername("username").build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, user2));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        User userInvalid = User.Builder.builder().withId(3L).withUsername("Username").withLastname("Lastname")
                .withFirstname("firstname").withEmail("falseemail").build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, userInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateAuthorisationTokenShouldNotThrowError() {
        assertDoesNotThrow(() -> validator.validateAuthorisationToken(mockJwt));

        verify(validator).validateAuthorisationToken(mockJwt);
    }

    @Test
    void validateAuthorisationTokenShouldThrowErrorWhenNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateAuthorisationToken(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("TOKEN_NULL", List.of()));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidObjectiveId() {
        validator.validateOnDelete(1L);

        verify(validator, times(1)).validateOnDelete(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfObjectiveIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

}
