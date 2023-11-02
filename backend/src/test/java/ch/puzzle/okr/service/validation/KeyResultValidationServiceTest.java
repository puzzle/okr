package ch.puzzle.okr.service.validation;

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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

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
                arguments(StringUtils.repeat('1', 251), List
                        .of("Attribute title must have a length between 2 and 250 characters when saving key result")),
                arguments(StringUtils.repeat('1', 1), List
                        .of("Attribute title must have a length between 2 and 250 characters when saving key result")),
                arguments("", List.of("Title can not be blank",
                        "Attribute title must have a length between 2 and 250 characters when saving key result")),
                arguments(" ", List.of("Title can not be blank",
                        "Attribute title must have a length between 2 and 250 characters when saving key result")),
                arguments("         ", List.of("Title can not be blank")),
                arguments(null, List.of("Title can not be blank", "Title can not be null")));
    }

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidKeyResultId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfKeyResultIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenKeyResultIsValid() {
        validator.validateOnCreate(fullKeyResult);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(fullKeyResult);
        verify(validator, times(1)).validate(fullKeyResult);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model KeyResult is null", exception.getReason());
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultMetric));

        assertEquals("Model KeyResult cannot have id while create. Found id 5", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenTitleIsInvalid(String title, List<String> errors) {
        KeyResult keyResult = KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(5.0)
                .withUnit(Unit.FTE).withId(null).withTitle(title).withOwner(user).withObjective(objective)
                .withCreatedBy(user).withCreatedOn(LocalDateTime.MIN).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(keyResult));

        String[] exceptionParts = exception.getReason().split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            System.out.println(exceptionParts[i].strip());
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            System.out.println(exceptionParts[i]);
            assert (errors.contains(errorArray[i]));
        }
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        KeyResult keyResultInvalid = KeyResultMetric.Builder.builder().withId(null).withTitle("Title").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultInvalid));

        assertThat(exception.getReason().strip()).contains("Baseline must not be null.");
        assertThat(exception.getReason().strip()).contains("StretchGoal must not be null.");
        assertThat(exception.getReason().strip()).contains("Unit must not be null.");
        assertThat(exception.getReason().strip()).contains("CreatedBy must not be null.");
        assertThat(exception.getReason().strip()).contains("Owner must not be null.");
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
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals("Given model KeyResult is null", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, keyResultOrdinal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(keyResultOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdHasChanged() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, keyResultMetric));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(keyResultMetric);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(keyResultMetric.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(1L, keyResultMetric.getId());
        assertEquals("Id 1 has changed to 5 during update", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenTitleIsInvalid(String title, List<String> errors) {
        Long id = 3L;
        KeyResult keyResult = KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(5.0)
                .withUnit(Unit.FTE).withId(id).withTitle(title).withOwner(user).withObjective(objective)
                .withCreatedBy(user).withCreatedOn(LocalDateTime.MIN).build();
        when(keyResultPersistenceService.findById(id)).thenReturn(keyResult);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(id, keyResult));

        String[] exceptionParts = exception.getReason().split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = exceptionParts[i].strip();
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assert (errors.contains(errorArray[i]));
        }
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        Long id = 11L;
        KeyResult keyResultInvalid = KeyResultMetric.Builder.builder().withId(id).withTitle("Title")
                .withObjective(objective).build();
        when(keyResultPersistenceService.findById(id)).thenReturn(keyResultInvalid);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(id, keyResultInvalid));

        assertThat(exception.getReason().strip()).contains("Baseline must not be null.");
        assertThat(exception.getReason().strip()).contains("StretchGoal must not be null.");
        assertThat(exception.getReason().strip()).contains("Unit must not be null.");
        assertThat(exception.getReason().strip()).contains("CreatedBy must not be null.");
        assertThat(exception.getReason().strip()).contains("Owner must not be null.");
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidKeyResultId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfKeyResultIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

}
