package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static ch.puzzle.okr.test.AssertionHelper.assertOkrResponseStatusException;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CheckInValidationServiceTest {
    @MockBean
    CheckInPersistenceService checkInPersistenceService = Mockito.mock(CheckInPersistenceService.class);

    private final User user = User.Builder.builder().withId(1L).withFirstName("Ruedi").withLastName("Grochde")
            .withEmail("grochde@puzzle.ch").build();
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
        return Stream.of(
                arguments(-1, List.of(new ErrorDto("ATTRIBUTE_MIN_VALUE", List.of("confidence", "CheckIn", "0")))),
                arguments(11, List.of(new ErrorDto("ATTRIBUTE_MAX_VALUE", List.of("confidence", "CheckIn", "10")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("confidence", "CheckIn")))));
    }

    @BeforeEach
    void setUp() {
        when(checkInPersistenceService.getModelName()).thenReturn("CheckIn");
    }

    @DisplayName("Should be successful on validateOnGet() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnGetWhenValidCheckInId() {
        validator.validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @DisplayName("Should throw exception on validateOnGet() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnGetIfCheckInIdIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "CheckIn")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should be successful on validateOnCreate() when check in is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnCreateWhenCheckInIsValid() {
        when(checkInPersistenceService.findById(checkInMetric.getId())).thenReturn(checkInMetric);

        validator.validateOnCreate(checkInMetric);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInMetric);
        verify(validator, times(1)).throwExceptionWhenIdIsNotNull(checkInMetric.getId());
        verify(validator, times(1)).validate(checkInMetric);
    }

    @DisplayName("Should throw exception on validateOnCreate() when model is null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenModelIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("CheckIn")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnCreate() when id is not null")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenIdIsNotNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(fullCheckIn));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "CheckIn")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @ParameterizedTest(name = "should throw exception on validateOnCreate() when confidence has invalid value {0}")
    @MethodSource("confidenceValidationArguments")
    void shouldThrowExceptionOnValidateOnCreateWhenConfidenceIsInvalid(Integer confidence,
            List<ErrorDto> expectedErrors) {

        // arrange
        CheckIn checkIn = CheckInMetric.Builder.builder() //
                .withValue(40.9) //
                .withChangeInfo("ChangeInfo") //
                .withInitiatives("Initiatives") //
                .withConfidence(confidence) //
                .withCreatedBy(user) //
                .withKeyResult(keyResultMetric) //
                .withCreatedOn(LocalDateTime.MAX) //
                .withModifiedOn(LocalDateTime.MAX) //
                .build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(checkIn));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnCreate() when attributes are missing")
    @Test
    void shouldThrowExceptionOnValidateOnCreateWhenAttrsAreMissing() {
        // arrange
        CheckIn checkInInvalid = CheckInMetric.Builder.builder() //
                .withId(null) //
                .withChangeInfo("ChangeInfo").build();

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(checkInInvalid));

        List<ErrorDto> expectedErrors = List.of( //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("confidence", "CheckIn")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("keyResult", "CheckIn")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "CheckIn")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "CheckIn")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("valueMetric", "CheckIn")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should be successful on validateOnUpdate()")
    @Test
    void validateOnUpdateShouldBeSuccessfulWhenCheckInIsValid() {
        when(checkInPersistenceService.findById(checkInOrdinal.getId())).thenReturn(checkInOrdinal);

        validator.validateOnUpdate(checkInOrdinal.getId(), checkInOrdinal);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(checkInOrdinal.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(checkInOrdinal.getId(), checkInOrdinal.getId());
        verify(validator, times(1)).validate(checkInOrdinal);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when model is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenModelIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("CheckIn")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, checkInOrdinal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "CheckIn")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when id has changed")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenIdIsHasChanged() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, checkInOrdinal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(2L);
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(2L, checkInOrdinal.getId());

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", "2", "1")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @ParameterizedTest(name = "should throw exception on validateOnUpdate() when confidence has invalid value {0}")
    @MethodSource("confidenceValidationArguments")
    void shouldThrowExceptionOnValidateOnUpdateWhenConfidenceIsInvalid(Integer confidence,
            List<ErrorDto> expectedErrors) {

        // arrange
        Long id = 2L;
        CheckIn checkIn = CheckInMetric.Builder.builder().withValue(40.9).withId(id).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(confidence).withCreatedBy(user)
                .withKeyResult(keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .build();
        when(checkInPersistenceService.findById(id)).thenReturn(checkIn);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, checkIn));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when check ins are empty")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenCheckInsOfKeyResultIsEmpty() {
        // arrange
        Long id = 2L;
        CheckIn checkIn = CheckInMetric.Builder.builder().withValue(40.9).withId(id).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(2).withCreatedBy(user).withKeyResult(keyResultMetric)
                .withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX).build();
        CheckIn savedCheckIn = CheckInMetric.Builder.builder().withId(id).withChangeInfo("")
                .withInitiatives("Initiatives").withCreatedBy(user)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(13L).build()).build();
        when(checkInPersistenceService.findById(id)).thenReturn(savedCheckIn);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, checkIn));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkIn);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(checkIn.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(checkIn.getId(), checkIn.getId());

        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_CANNOT_CHANGE", List.of("KeyResult", "Check-in")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should throw exception on validateOnUpdate() when attributes are missing")
    @Test
    void shouldThrowExceptionOnValidateOnUpdateWhenAttrsAreMissing() {
        // arrange
        Long id = 11L;
        CheckIn checkInInvalid = CheckInMetric.Builder.builder() //
                .withId(id) //
                .withChangeInfo("ChangeInfo") //
                .withKeyResult(KeyResultMetric.Builder.builder().withId(13L).build()) //
                .build();
        when(checkInPersistenceService.findById(id)).thenReturn(checkInInvalid);

        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, checkInInvalid));

        List<ErrorDto> expectedErrors = List.of( //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("confidence", "CheckIn")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "CheckIn")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "CheckIn")), //
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("valueMetric", "CheckIn")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

    @DisplayName("Should be successful on validateOnDelete() when id is valid")
    @Test
    void shouldBeSuccessfulOnValidateOnDeleteWhenValidKeyResultId() {
        validator.validateOnDelete(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
        verify(validator, times(1)).doesEntityExist(1L);
    }

    @DisplayName("Should throw exception on validateOnDelete() when id is null")
    @Test
    void shouldThrowExceptionOnValidateOnDeleteWhenKeyResultIdIsNull() {
        // act + assert
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnDelete(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "CheckIn")));
        assertOkrResponseStatusException(exception, expectedErrors);
    }

}