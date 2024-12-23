package ch.puzzle.okr.service.validation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import ch.puzzle.okr.test.TestHelper;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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

@ExtendWith(MockitoExtension.class)
class UserValidationServiceTest {
    @MockBean
    UserPersistenceService userPersistenceService = Mockito.mock(UserPersistenceService.class);

    User userMinimal;
    User user;

    Jwt mockJwt = TestHelper.mockJwtToken("firstname", "lastname", "email@email.com");

    @BeforeEach
    void setUp() {
        user = User.Builder
                .builder()
                .withId(1L)
                .withFirstName("Bob")
                .withLastName("Kaufmann")
                .withEmail("kaufmann@puzzle.ch")
                .build();

        userMinimal = User.Builder
                .builder()
                .withFirstName("Max")
                .withLastName("Mustermann")
                .withEmail("max@mustermann.com")
                .build();

        when(userPersistenceService.findById(1L)).thenReturn(user);
        when(userPersistenceService.getModelName()).thenReturn("User");
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                                            String
                                                    .format("%s with id %s not found",
                                                            userPersistenceService.getModelName(),
                                                            2L)))
                .when(userPersistenceService)
                .findById(2L);
    }

    @Spy
    @InjectMocks
    private UserValidationService validator;

    private static Stream<Arguments> firstNameValidationArguments() {
        return Stream
                .of(arguments(StringUtils.repeat('1', 51),
                              List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("firstName", "User", "2", "50")))),
                    arguments(StringUtils.repeat('1', 1),
                              List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("firstName", "User", "2", "50")))),
                    arguments("",
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("firstName", "User")),
                                          new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("firstName", "User", "2", "50")))),
                    arguments(" ",
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("firstName", "User")),
                                          new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("firstName", "User", "2", "50")))),
                    arguments("         ", List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("firstName", "User")))),
                    arguments(null,
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("firstName", "User")),
                                          new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("firstName", "User")))));
    }

    private static Stream<Arguments> lastNameValidationArguments() {
        return Stream
                .of(arguments(StringUtils.repeat('1', 51),
                              List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("lastName", "User", "2", "50")))),
                    arguments(StringUtils.repeat('1', 1),
                              List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("lastName", "User", "2", "50")))),
                    arguments("",
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("lastName", "User")),
                                          new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("lastName", "User", "2", "50")))),
                    arguments(" ",
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("lastName", "User")),
                                          new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("lastName", "User", "2", "50")))),
                    arguments("         ", List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("lastName", "User")))),
                    arguments(null,
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("lastName", "User")),
                                          new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("lastName", "User")))));
    }

    private static Stream<Arguments> emailValidationArguments() {
        return Stream
                .of(arguments(("1".repeat(251)),
                              List
                                      .of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("email", "User", "2", "250")),
                                          new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")))),
                    arguments(("1"),
                              List
                                      .of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("email", "User", "2", "250")),
                                          new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")))),
                    arguments((""),
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("email", "User")),
                                          new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("email", "User", "2", "250")))),
                    arguments((" "),
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("email", "User")),
                                          new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")),
                                          new ErrorDto("ATTRIBUTE_SIZE_BETWEEN",
                                                       List.of("email", "User", "2", "250")))),
                    arguments(("       "),
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("email", "User")),
                                          new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")))),
                    arguments(null,
                              List
                                      .of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("email", "User")),
                                          new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("email", "User")))));
    }

    @DisplayName("Should be successful on validateOnGet() when user id is valid")
    @Test
    void validateOnGetShouldBeSuccessfulWhenValidUserId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @DisplayName("Should throw exception on validateOnGet() when id is null")
    @Test
    void validateOnGetShouldThrowExceptionIfUserIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnGet(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be successful on validateOnGetOrCreate() when user is valid")
    @Test
    void validateOnGetOrCreateShouldBeSuccessful() {
        validator.validateOnGetOrCreate(userMinimal);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(userMinimal);
        verify(validator, times(1)).validate(userMinimal);
    }

    @DisplayName("Should throw exception on validateOnGetOrCreate() when model is null")
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

    @DisplayName("Should be successful on validateOnCreate() when user is valid")
    @Test
    void validateOnCreateShouldBeSuccessfulWhenUserIsValid() {
        validator.validateOnCreate(userMinimal);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(userMinimal);
        verify(validator, times(1)).validate(userMinimal);
    }

    @DisplayName("Should throw exception on validateCreate() when model is null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateCreate() when id is not null")
    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(user));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest(name = "should throw exception on validateCreate() when model has invalid first name '{0}'")
    @MethodSource("firstNameValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenFirstnameIsInvalid(String name, List<ErrorDto> errors) {
        User user = User.Builder
                .builder()
                .withEmail("max@mail.com")
                .withFirstName(name)
                .withLastName("lastname")
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(user));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest(name = "should throw exception on validateCreate() when model has invalid last name '{0}'")
    @MethodSource("lastNameValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenLastnameIsInvalid(String name, List<ErrorDto> errors) {
        User user = User.Builder
                .builder()
                .withEmail("max@mail.com")
                .withFirstName("firstname")
                .withLastName(name)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(user));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest(name = "should throw exception on validateCreate() when model has invalid email '{0}'")
    @MethodSource("emailValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenEmailIsInvalid(String email, List<ErrorDto> errors) {
        User user = User.Builder
                .builder()
                .withEmail(email)
                .withFirstName("firstname")
                .withLastName("lastname").build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(user));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateCreate() when model is missing attributes")
    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        User userInvalid = User.Builder
                .builder()
                .withId(null)
                .withLastName("Lastname")
                .withFirstName("firstname")
                .withEmail("falseemail")
                .build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnCreate(userInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be successful on validateCreate() when user is valid")
    @Test
    void validateOnUpdateShouldBeSuccessfulWhenUserIsValid() {
        validator.validateOnUpdate(user.getId(), user);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(user);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(user.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(user.getId(), user.getId());
        verify(validator, times(1)).validate(user);
    }

    @DisplayName("Should be throw exception on validateOnUpdate() when model is null")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(1L, null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be throw exception on validateOnUpdate() when id is null")
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

    @DisplayName("Should be throw exception on validateOnUpdate() when id has changed")
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

    @ParameterizedTest(name = "should be throw exception on validateOnUpdate() when model has invalid first name {0}")
    @MethodSource("firstNameValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenFirstnameIsInvalid(String name, List<ErrorDto> errors) {
        User user = User.Builder
                .builder()
                .withId(3L)
                .withEmail("max@mail.com")
                .withFirstName(name)
                .withLastName("lastname")
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(3L, user));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest(name = "should be throw exception on validateOnUpdate() when model has invalid last name {0}")
    @MethodSource("lastNameValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenLastnameIsInvalid(String name, List<ErrorDto> errors) {
        User user = User.Builder
                .builder()
                .withId(3L)
                .withEmail("max@mail.com")
                .withFirstName("firstname")
                .withLastName(name)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(3L, user));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @ParameterizedTest(name = "should be throw exception on validateOnUpdate() when model has invalid email {0}")
    @MethodSource("emailValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenEmailIsInvalid(String email, List<ErrorDto> errors) {
        User user = User.Builder
                .builder()
                .withId(3L)
                .withEmail(email)
                .withFirstName("firstname")
                .withLastName("lastname")
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(3L, user));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @DisplayName("Should throw exception on validateOnUpdate() when model is missing attributes")
    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        User userInvalid = User.Builder
                .builder()
                .withId(3L)
                .withLastName("Lastname")
                .withFirstName("firstname")
                .withEmail("falseemail")
                .build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateOnUpdate(3L, userInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_VALID", List.of("email", "User")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should not throw error on validateAuthorisationToken() when token is valid")
    @Test
    void validateAuthorisationTokenShouldNotThrowError() {
        assertDoesNotThrow(() -> validator.validateAuthorisationToken(mockJwt));

        verify(validator).validateAuthorisationToken(mockJwt);
    }

    @DisplayName("Should throw error on validateAuthorisationToken() when token is null")
    @Test
    void validateAuthorisationTokenShouldThrowErrorWhenNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> validator.validateAuthorisationToken(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("TOKEN_NULL", List.of()));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should be successful on validateOnDelete() when model is valid")
    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidObjectiveId() {
        validator.validateOnDelete(1L);

        verify(validator, times(1)).validateOnDelete(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @DisplayName("Should throw exception on validateOnGet() when model is null")
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
