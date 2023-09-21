package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.BDDMockito;
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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CheckInValidationServiceTest {
    @MockBean
    CheckInPersistenceService checkInPersistenceService = Mockito.mock(CheckInPersistenceService.class);
    /* Test data */
    User user;
    Team team;
    Objective objective;
    Quarter quarter;
    KeyResult keyResultMetric;
    KeyResult keyResultOrdinal;
    CheckIn checkInMetric;
    CheckIn checkInOrdinal;
    CheckIn fullCheckIn;
    @Spy
    @InjectMocks
    private CheckInValidationService validator;

    private static Stream<Arguments> confidenceValidationArguments() {
        return Stream.of(arguments(-1, List.of("Attribute confidence has a min value of 1")),
                arguments(11, List.of("Missing attribute title when saving objective")),
                arguments(null, List.of("Confidence must not be null")));
    }

    @BeforeEach
    void setUp() {
        this.user = User.Builder.builder().withId(1L).withFirstname("Ruedi").withLastname("Grochde")
                .withUsername("rgrochde").withEmail("grochde@puzzle.ch").build();
        this.team = Team.Builder.builder().withId(1L).withName("Team4").build();
        this.quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();

        this.objective = Objective.Builder.builder().withId(1L).withTitle("Objective 1").withCreatedBy(user)
                .withTeam(team).withQuarter(quarter).withDescription("This is our description")
                .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(user)
                .withCreatedOn(LocalDateTime.MAX).build();

        this.keyResultMetric = KeyResultMetric.Builder.builder().withBaseline(13D).withStretchGoal(25D)
                .withUnit("MEMBERS").withId(8L).withTitle("Keyresult Metric").withObjective(this.objective)
                .withOwner(this.user).build();

        this.keyResultOrdinal = KeyResultOrdinal.Builder.builder().withCommitZone("Commit Zone")
                .withTargetZone("Target Zone").withTitle("Keyresult Ordinal").withObjective(this.objective)
                .withOwner(this.user).build();

        this.fullCheckIn = CheckInMetric.Builder.builder().withValue(45D).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(10).withKeyResult(this.keyResultMetric)
                .withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX).withCreatedBy(user).build();

        this.checkInMetric = CheckInMetric.Builder.builder().withValue(27D).withId(1L)
                .withChangeInfo("ChangeInfoMetric").withInitiatives("InitiativesMetric").withConfidence(8)
                .withKeyResult(this.keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .withCreatedBy(user).build();

        this.checkInOrdinal = CheckInOrdinal.Builder.builder().withZone(Zone.STRETCH).withId(1L)
                .withChangeInfo("ChangeInfoMetric").withInitiatives("InitiativesMetric").withConfidence(8)
                .withKeyResult(this.keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .withCreatedBy(user).build();

        /* Mock essential methods */
        when(checkInPersistenceService.getModelName()).thenReturn("CheckIn");
    }

    @Test
    void validateOnGet_ShouldBeSuccessfulWhenValidCheckInId() {
        validator.validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGet_ShouldThrowExceptionIfCheckInIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void validateOnCreate_ShouldBeSuccessfulWhenCheckInIsValid() {
        validator.validateOnCreate(this.fullCheckIn);

        verify(validator, times(1)).throwExceptionIfModelIsNull(this.fullCheckIn);
        verify(validator, times(1)).throwExceptionWhenIdIsNotNull(this.fullCheckIn.getId());
        verify(validator, times(1)).validate(this.fullCheckIn);
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model CheckIn is null", exception.getReason());
    }

    @Test
    void validateOnCreate_ShouldThrowExceptionWhenIdIsNotNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(this.checkInMetric));

        assertEquals("Model CheckIn cannot have id while create. Found id 1", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("confidenceValidationArguments")
    void validateOnCreate_ShouldThrowExceptionWhenConfidenceIsInvalid(Integer confidence, List<String> errors) {
        CheckIn checkIn = CheckInMetric.Builder.builder().withValue(40.9).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(confidence).withCreatedBy(this.user)
                .withKeyResult(this.keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(checkIn));

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
        CheckIn checkInInvalid = CheckInMetric.Builder.builder().withId(null).withChangeInfo("ChangeInfo").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(checkInInvalid));

        String errorConfidence = "Confidence must not be null";
        String errorKeyResult = "KeyResult must not be null";
        String errorCreatedBy = "CreatedBy must not be null";
        String errorCreatedOn = "CreatedOn must not be null";
        String errorValue = "Value must not be null";

        assertThat(exception.getReason().strip()).contains(errorConfidence);
        assertThat(exception.getReason().strip()).contains(errorKeyResult);
        assertThat(exception.getReason().strip()).contains(errorCreatedBy);
        assertThat(exception.getReason().strip()).contains(errorCreatedOn);
        assertThat(exception.getReason().strip()).contains(errorValue);
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals("Given model CheckIn is null", exception.getReason());
    }

    @Test
    void validateOnUpdate_ShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, this.checkInOrdinal));

        verify(validator, times(1)).throwExceptionIfModelIsNull(this.checkInOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("confidenceValidationArguments")
    void validateOnUpdate_ShouldThrowExceptionWhenConfidenceIsInvalid(Integer confidence, List<String> errors) {
        CheckIn checkIn = CheckInMetric.Builder.builder().withValue(40.9).withId(2L).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(confidence).withCreatedBy(this.user)
                .withKeyResult(this.keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, checkIn));

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
    void validateOnUpdate_ShouldThrowExceptionWhenAttrsAreMissing() {
        CheckIn checkInInvalid = CheckInMetric.Builder.builder().withId(11L).withChangeInfo("ChangeInfo")
                .withKeyResult(KeyResultMetric.Builder.builder().withId(13L).build()).build();
        BDDMockito.when(checkInPersistenceService.getCheckInsByKeyResultIdOrderByCheckInDateDesc(13L))
                .thenReturn(List.of(checkInInvalid));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(11L, checkInInvalid));

        String errorConfidence = "Confidence must not be null";
        String errorCreatedBy = "CreatedBy must not be null";
        String errorCreatedOn = "CreatedOn must not be null";
        String errorValue = "Value must not be null";

        assertThat(exception.getReason().strip()).contains(errorConfidence);
        assertThat(exception.getReason().strip()).contains(errorCreatedBy);
        assertThat(exception.getReason().strip()).contains(errorCreatedOn);
        assertThat(exception.getReason().strip()).contains(errorValue);
    }

    @Test
    void validateOnDelete_ShouldBeSuccessfulWhenValidKeyResultId() {
        validator.validateOnDelete(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
        verify(validator, times(1)).doesEntityExist(1L);
    }

    @Test
    void validateOnDelete_ShouldThrowExceptionIfKeyResultIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnDelete(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

}