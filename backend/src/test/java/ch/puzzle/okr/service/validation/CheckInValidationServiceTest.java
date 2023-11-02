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
class CheckInValidationServiceTest {
    @MockBean
    CheckInPersistenceService checkInPersistenceService = Mockito.mock(CheckInPersistenceService.class);

    private final User user = User.Builder.builder().withId(1L).withFirstname("Ruedi").withLastname("Grochde")
            .withUsername("rgrochde").withEmail("grochde@puzzle.ch").build();
    private final Team team = Team.Builder.builder().withId(1L).withName("Team4").build();
    private final Quarter quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
    private final Objective objective = Objective.Builder.builder().withId(1L).withTitle("Objective 1")
            .withCreatedBy(user).withTeam(team).withQuarter(quarter).withDescription("This is our description")
            .withModifiedOn(LocalDateTime.MAX).withState(State.DRAFT).withModifiedBy(user)
            .withCreatedOn(LocalDateTime.MAX).build();
    private final KeyResult keyResultMetric = KeyResultMetric.Builder.builder().withBaseline(13D).withStretchGoal(25D)
            .withUnit(Unit.NUMBER).withId(8L).withTitle("Keyresult Metric").withObjective(objective).withOwner(user)
            .build();
    private final KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withCommitZone("Commit Zone")
            .withTargetZone("Target Zone").withTitle("Keyresult Ordinal").withObjective(objective).withOwner(user)
            .build();
    private final CheckIn checkInMetric = CheckInMetric.Builder.builder().withValue(45D).withChangeInfo("ChangeInfo")
            .withInitiatives("Initiatives").withConfidence(10).withKeyResult(keyResultMetric)
            .withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX).withCreatedBy(user).build();
    private final CheckIn checkInOrdinal = CheckInMetric.Builder.builder().withValue(27D).withId(1L)
            .withChangeInfo("ChangeInfoMetric").withInitiatives("InitiativesMetric").withConfidence(8)
            .withKeyResult(keyResultOrdinal).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
            .withCreatedBy(user).build();
    private final CheckIn fullCheckIn = CheckInOrdinal.Builder.builder().withZone(Zone.STRETCH).withId(1L)
            .withChangeInfo("ChangeInfoMetric").withInitiatives("InitiativesMetric").withConfidence(8)
            .withKeyResult(keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
            .withCreatedBy(user).build();
    @Spy
    @InjectMocks
    private CheckInValidationService validator;

    private static Stream<Arguments> confidenceValidationArguments() {
        return Stream.of(arguments(-1, List.of("Attribute confidence has a min value of 1")),
                arguments(11, List.of("Attribute confidence has a max value of 10")),
                arguments(null, List.of("Confidence must not be null")));
    }

    @BeforeEach
    void setUp() {
        // when(checkInPersistenceService.findById(1L)).thenReturn(keyResultMetric);
        when(checkInPersistenceService.getModelName()).thenReturn("CheckIn");
    }

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidCheckInId() {
        validator.validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfCheckInIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnGet(null));
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenCheckInIsValid() {
        when(checkInPersistenceService.findById(checkInMetric.getId())).thenReturn(checkInMetric);

        validator.validateOnCreate(checkInMetric);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInMetric);
        verify(validator, times(1)).throwExceptionWhenIdIsNotNull(checkInMetric.getId());
        verify(validator, times(1)).validate(checkInMetric);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals("Given model CheckIn is null", exception.getReason());
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(fullCheckIn));

        assertEquals("Model CheckIn cannot have id while create. Found id 1", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("confidenceValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenConfidenceIsInvalid(Integer confidence, List<String> errors) {
        CheckIn checkIn = CheckInMetric.Builder.builder().withValue(40.9).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(confidence).withCreatedBy(user)
                .withKeyResult(keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .build();

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(checkIn));

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
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        CheckIn checkInInvalid = CheckInMetric.Builder.builder().withId(null).withChangeInfo("ChangeInfo").build();
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnCreate(checkInInvalid));

        assertThat(exception.getReason().strip()).contains("Confidence must not be null");
        assertThat(exception.getReason().strip()).contains("KeyResult must not be null");
        assertThat(exception.getReason().strip()).contains("CreatedBy must not be null");
        assertThat(exception.getReason().strip()).contains("CreatedOn must not be null");
        assertThat(exception.getReason().strip()).contains("Value must not be null");
    }

    @Test
    void validateOnUpdateShouldBeSuccessfulWhenCheckInIsValid() {
        when(checkInPersistenceService.findById(checkInOrdinal.getId())).thenReturn(checkInOrdinal);

        validator.validateOnUpdate(checkInOrdinal.getId(), checkInOrdinal);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(checkInOrdinal.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(checkInOrdinal.getId(), checkInOrdinal.getId());
        verify(validator, times(1)).validate(checkInOrdinal);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals("Given model CheckIn is null", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(null, checkInOrdinal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsHasChanged() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, checkInOrdinal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(2L);
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(2L, checkInOrdinal.getId());
        assertEquals("Id 2 has changed to 1 during update", exception.getReason());
    }

    @ParameterizedTest
    @MethodSource("confidenceValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenConfidenceIsInvalid(Integer confidence, List<String> errors) {
        Long id = 2L;
        CheckIn checkIn = CheckInMetric.Builder.builder().withValue(40.9).withId(id).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(confidence).withCreatedBy(user)
                .withKeyResult(keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .build();
        when(checkInPersistenceService.findById(id)).thenReturn(checkIn);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(id, checkIn));

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
    void validateOnUpdateShouldThrowExceptionWhenCheckInsOfKeyResultIsEmpty() {
        Long id = 2L;
        CheckIn checkIn = CheckInMetric.Builder.builder().withValue(40.9).withId(id).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(2).withCreatedBy(user).withKeyResult(keyResultMetric)
                .withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX).build();
        CheckIn savedCheckIn = CheckInMetric.Builder.builder().withId(id).withChangeInfo("")
                .withInitiatives("Initiatives").withCreatedBy(user)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(13L).build()).build();
        when(checkInPersistenceService.findById(id)).thenReturn(savedCheckIn);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(id, checkIn));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkIn);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(checkIn.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(checkIn.getId(), checkIn.getId());
        assertEquals("Not allowed change the association to the key result (id = 2)", exception.getReason());

    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        Long id = 11L;
        CheckIn checkInInvalid = CheckInMetric.Builder.builder().withId(id).withChangeInfo("ChangeInfo")
                .withKeyResult(KeyResultMetric.Builder.builder().withId(13L).build()).build();
        when(checkInPersistenceService.findById(id)).thenReturn(checkInInvalid);
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnUpdate(id, checkInInvalid));

        assertThat(exception.getReason().strip()).contains("Confidence must not be null");
        assertThat(exception.getReason().strip()).contains("CreatedBy must not be null");
        assertThat(exception.getReason().strip()).contains("CreatedOn must not be null");
        assertThat(exception.getReason().strip()).contains("Value must not be null");
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidKeyResultId() {
        validator.validateOnDelete(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
        verify(validator, times(1)).doesEntityExist(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfKeyResultIdIsNull() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> validator.validateOnDelete(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals("Id is null", exception.getReason());
    }

}