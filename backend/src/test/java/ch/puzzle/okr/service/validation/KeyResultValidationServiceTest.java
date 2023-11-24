package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
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
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class KeyResultValidationServiceTest {
    @MockBean
    KeyResultPersistenceService keyResultPersistenceService = Mockito.mock(KeyResultPersistenceService.class);

    private final User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
            .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
    private final Quarter quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
    private final Team team = Team.Builder.builder().withId(1L).withName("Team1").build();
    private final Objective objective = Objective.Builder.builder().withId(1L).withTitle("Objective 1")
            .withCreatedBy(user).withTeam(team).withQuarter(quarter).withDescription("This is our description")
            .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(user)
            .withCreatedOn(LocalDateTime.MAX).build();
    private final KeyResult keyResultMetric = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0)
            .withUnit(Unit.NUMBER).withId(5L).withTitle("Keyresult Metric").withObjective(objective).withOwner(user)
            .build();
    private final KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withCommitZone("Ein Baum")
            .withTargetZone("Zwei Bäume").withTitle("Keyresult Ordinal").withObjective(objective).withOwner(user)
            .build();
    private final KeyResult fullKeyResult = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0)
            .withUnit(Unit.FTE).withId(null).withTitle("Keyresult Metric").withObjective(objective).withOwner(user)
            .withCreatedOn(LocalDateTime.MIN).withModifiedOn(LocalDateTime.MAX).withDescription("Description")
            .withCreatedBy(user).build();

    @BeforeEach
    void setUp() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(keyResultMetric);
        when(keyResultPersistenceService.getModelName()).thenReturn("KeyResult");
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", keyResultPersistenceService.getModelName(), 2L)))
                        .when(keyResultPersistenceService).findById(2L);
    }

    @Spy
    @InjectMocks
    private KeyResultValidationService validator;

    private static Stream<Arguments> nameValidationArguments() {
        return Stream.of(
                arguments(StringUtils.repeat('1', 251),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "KeyResult", "2", "250")))),
                arguments(StringUtils.repeat('1', 1),
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "KeyResult", "2", "250")))),
                arguments("",
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "KeyResult", "2", "250")),
                                new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "KeyResult")))),
                arguments(" ",
                        List.of(new ErrorDto("ATTRIBUTE_SIZE_BETWEEN", List.of("title", "KeyResult", "2", "250")),
                                new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "KeyResult")))),
                arguments("         ", List.of(new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "KeyResult")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("title", "KeyResult")),
                        new ErrorDto("ATTRIBUTE_NOT_BLANK", List.of("title", "KeyResult")))));
    }

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidKeyResultId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfKeyResultIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "KeyResult")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenKeyResultIsValid() {
        validator.validateOnCreate(fullKeyResult);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(fullKeyResult);
        verify(validator, times(1)).validate(fullKeyResult);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("KeyResult")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultMetric));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "KeyResult")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenTitleIsInvalid(String title, List<ErrorDto> errors) {
        KeyResult keyResult = KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(5.0)
                .withUnit(Unit.FTE).withId(null).withTitle(title).withOwner(user).withObjective(objective)
                .withCreatedBy(user).withCreatedOn(LocalDateTime.MIN).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResult));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        KeyResult keyResultInvalid = KeyResultMetric.Builder.builder().withId(null).withTitle("Title").build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("owner", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("stretchGoal", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("objective", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("baseline", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("unit", "KeyResult")));
        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldBeSuccessfulWhenKeyResultIsValid() {
        Long id = 5L;
        KeyResult keyResult = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0)
                .withUnit(Unit.NUMBER).withId(id).withTitle("Keyresult Metric").withObjective(objective).withOwner(user)
                .withCreatedOn(LocalDateTime.MIN).withModifiedOn(LocalDateTime.MAX).withDescription("Description")
                .withCreatedBy(user).build();
        when(keyResultPersistenceService.findById(id)).thenReturn(keyResult);

        validator.validateOnUpdate(id, keyResult);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(keyResult);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(keyResult.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(keyResult.getId(), keyResult.getId());
        verify(validator, times(1)).validate(keyResult);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertEquals("MODEL_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("MODEL_NULL", List.of("KeyResult"))), exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, keyResultOrdinal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(keyResultOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "KeyResult"))), exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, keyResultMetric));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(keyResultMetric);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(keyResultMetric.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(1L, keyResultMetric.getId());

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertEquals("ATTRIBUTE_CHANGED", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", "1", "5"))), exception.getErrors());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenTitleIsInvalid(String title, List<ErrorDto> errors) {
        Long id = 3L;
        KeyResult keyResult = KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(5.0)
                .withUnit(Unit.FTE).withId(id).withTitle(title).withOwner(user).withObjective(objective)
                .withCreatedBy(user).withCreatedOn(LocalDateTime.MIN).build();
        when(keyResultPersistenceService.findById(id)).thenReturn(keyResult);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, keyResult));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(errors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(errors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        Long id = 11L;
        KeyResult keyResultInvalid = KeyResultMetric.Builder.builder().withId(id).withTitle("Title")
                .withObjective(objective).build();
        when(keyResultPersistenceService.findById(id)).thenReturn(keyResultInvalid);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, keyResultInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("baseline", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("stretchGoal", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("unit", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "KeyResult")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("owner", "KeyResult")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidKeyResultId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfKeyResultIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "KeyResult")));
        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

}
