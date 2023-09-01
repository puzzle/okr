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

    KeyResult keyResultMetric;
    KeyResult keyResultOrdinal;
    KeyResult fullKeyResult;
    User user;
    Quarter quarter;
    Team team;
    Objective objective;

    @BeforeEach
    void setUp() {
        this.user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
                .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
        this.team = Team.Builder.builder().withId(1L).withName("Team1").build();
        this.quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();

        this.objective = Objective.Builder.builder().withId(1L).withTitle("Objective 1").withCreatedBy(user)
                .withTeam(team).withQuarter(quarter).withDescription("This is our description")
                .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(user)
                .withCreatedOn(LocalDateTime.MAX).build();

        this.keyResultMetric = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0).withUnit("ECTS")
                .withId(5L).withTitle("Keyresult Metric").withObjective(this.objective).withOwner(this.user).build();

        this.keyResultOrdinal = KeyResultOrdinal.Builder.builder().withCommitZone("Ein Baum")
                .withTargetZone("Zwei Bäume").withTitle("Keyresult Ordinal").withObjective(this.objective)
                .withOwner(this.user).build();

        this.fullKeyResult = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0).withUnit("ECTS")
                .withId(null).withTitle("Keyresult Metric").withObjective(this.objective).withOwner(this.user)
                .withCreatedOn(LocalDateTime.MIN).withModifiedOn(LocalDateTime.MAX).withDescription("Description")
                .withCreatedBy(this.user).build();

        when(keyResultPersistenceService.findById(1L)).thenReturn(this.keyResultMetric);
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
                        .of("Attribute title must have a length between 2 and 250 characters when saving objective")),
                arguments(StringUtils.repeat('1', 1), List
                        .of("Attribute title must have a length between 2 and 250 characters when saving objective")),
                arguments("", List.of("Missing attribute title when saving objective",
                        "Attribute title must have a length between 2 and 250 characters when saving objective")),
                arguments(" ", List.of("Missing attribute title when saving objective",
                        "Attribute title must have a length between 2 and 250 characters when saving objective")),
                arguments("         ", List.of("Missing attribute title when saving objective")),
                arguments(null, List.of("Missing attribute title when saving objective",
                        "Attribute title can not be null when saving objective")));
    }

    @Test
    void validateOnGet_ShouldBeSuccessfulWhenValidKeyResultId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGet_ShouldThrowExceptionIfKeyResultIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldBeSuccessfulWhenKeyResultIsValid() {
        validator.validateOnCreate(this.fullKeyResult);

        verify(validator, times(1)).throwExceptionIfModelIsNull(this.fullKeyResult);
        verify(validator, times(1)).validate(this.fullKeyResult);
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model KeyResult is null", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenIdIsNotNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(this.keyResultMetric));

        assertEquals("Model KeyResult cannot have id while create. Found id 5", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnCreate_ShouldThrowExceptionWhenTitleIsInvalid(String title, List<String> errors) {
        KeyResult keyResult = KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(5.0).withUnit("ECTS")
                .withId(null).withTitle(title).withOwner(this.user).withObjective(this.objective)
                .withCreatedBy(this.user).withCreatedOn(LocalDateTime.MIN).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(keyResult));

        String[] exceptionParts = exception.getReason().split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = errors.get(i);
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            System.out.println(exceptionParts[i]);
            assertThat(errors.contains(errorArray[i]));
        }
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenAttrsAreMissing() {
        KeyResult keyResultInvalid = KeyResultMetric.Builder.builder().withId(null).withTitle("Title").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultInvalid));

        String errorBaseline = "Baseline must not be null.";
        String errorStretchGoal = "StretchGoal must not be null.";
        String errorUnit = "Unit must not be null.";
        String errorCreatedBy = "CreatedBy must not be null.";
        String errorOwner = "Owner must not be null.";

        assertThat(exception.getReason().strip()).contains(errorBaseline);
        assertThat(exception.getReason().strip()).contains(errorStretchGoal);
        assertThat(exception.getReason().strip()).contains(errorUnit);
        assertThat(exception.getReason().strip()).contains(errorCreatedBy);
        assertThat(exception.getReason().strip()).contains(errorOwner);
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals("Given model KeyResult is null", exception.getReason());
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, this.keyResultOrdinal));

        verify(validator, times(1)).throwExceptionIfModelIsNull(this.keyResultOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("nameValidationArguments")
    void validateOnUpdate_ShouldThrowExceptionWhenTitleIsInvalid(String title, List<String> errors) {
        KeyResult keyResult = KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(5.0).withUnit("ECTS")
                .withId(2L).withTitle(title).withOwner(this.user).withObjective(this.objective).withCreatedBy(this.user)
                .withCreatedOn(LocalDateTime.MIN).build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, keyResult));

        String[] exceptionParts = exception.getReason().split("\\.");
        String[] errorArray = new String[errors.size()];

        for (int i = 0; i < errors.size(); i++) {
            errorArray[i] = errors.get(i);
        }

        for (int i = 0; i < exceptionParts.length; i++) {
            assertThat(errors.contains(errorArray[i]));
        }
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenAttrsAreMissing() {
        KeyResult keyResultInvalid = KeyResultMetric.Builder.builder().withId(11L).withTitle("Title").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(5L, keyResultInvalid));

        String errorBaseline = "Baseline must not be null.";
        String errorStretchGoal = "StretchGoal must not be null.";
        String errorUnit = "Unit must not be null.";
        String errorCreatedBy = "CreatedBy must not be null.";
        String errorOwner = "Owner must not be null.";

        assertThat(exception.getReason().strip()).contains(errorBaseline);
        assertThat(exception.getReason().strip()).contains(errorStretchGoal);
        assertThat(exception.getReason().strip()).contains(errorUnit);
        assertThat(exception.getReason().strip()).contains(errorCreatedBy);
        assertThat(exception.getReason().strip()).contains(errorOwner);
    }

    @Test
    void validateOnDelete_ShouldBeSuccessfulWhenValidKeyResultId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnDelete_ShouldThrowExceptionIfKeyResultIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

}
