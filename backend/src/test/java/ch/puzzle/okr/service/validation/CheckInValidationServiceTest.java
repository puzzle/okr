package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
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
import org.assertj.core.api.Assertions;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

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
        return Stream.of(
                arguments(-1, List.of(new ErrorDto("ATTRIBUTE_MIN_VALUE", List.of("confidence", "CheckIn", "1")))),
                arguments(11, List.of(new ErrorDto("ATTRIBUTE_MAX_VALUE", List.of("confidence", "CheckIn", "10")))),
                arguments(null, List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("confidence", "CheckIn")))));
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
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "CheckIn")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
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
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("CheckIn")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(fullCheckIn));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "CheckIn")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("confidenceValidationArguments")
    void validateOnCreateShouldThrowExceptionWhenConfidenceIsInvalid(Integer confidence,
            List<ErrorDto> expectedErrors) {
        CheckIn checkIn = CheckInMetric.Builder.builder().withValue(40.9).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(confidence).withCreatedBy(user)
                .withKeyResult(keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(checkIn));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAttrsAreMissing() {
        CheckIn checkInInvalid = CheckInMetric.Builder.builder().withId(null).withChangeInfo("ChangeInfo").build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(checkInInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("confidence", "CheckIn")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("keyResult", "CheckIn")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "CheckIn")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "CheckIn")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("valueMetric", "CheckIn")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
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
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("MODEL_NULL", List.of("CheckIn")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, checkInOrdinal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "CheckIn")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsHasChanged() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, checkInOrdinal));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkInOrdinal);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(2L);
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(2L, checkInOrdinal.getId());

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_CHANGED", List.of("ID", "2", "1")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @ParameterizedTest
    @MethodSource("confidenceValidationArguments")
    void validateOnUpdateShouldThrowExceptionWhenConfidenceIsInvalid(Integer confidence,
            List<ErrorDto> expectedErrors) {
        Long id = 2L;
        CheckIn checkIn = CheckInMetric.Builder.builder().withValue(40.9).withId(id).withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives").withConfidence(confidence).withCreatedBy(user)
                .withKeyResult(keyResultMetric).withCreatedOn(LocalDateTime.MAX).withModifiedOn(LocalDateTime.MAX)
                .build();
        when(checkInPersistenceService.findById(id)).thenReturn(checkIn);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, checkIn));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
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

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, checkIn));
        verify(validator, times(1)).throwExceptionWhenModelIsNull(checkIn);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(checkIn.getId());
        verify(validator, times(1)).throwExceptionWhenIdHasChanged(checkIn.getId(), checkIn.getId());

        List<ErrorDto> expectedErrors = List
                .of(new ErrorDto("ATTRIBUTE_CANNOT_CHANGE", List.of("KeyResult", "Check-in")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));

    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAttrsAreMissing() {
        Long id = 11L;
        CheckIn checkInInvalid = CheckInMetric.Builder.builder().withId(id).withChangeInfo("ChangeInfo")
                .withKeyResult(KeyResultMetric.Builder.builder().withId(13L).build()).build();
        when(checkInPersistenceService.findById(id)).thenReturn(checkInInvalid);
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(id, checkInInvalid));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("confidence", "CheckIn")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdBy", "CheckIn")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("createdOn", "CheckIn")),
                new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("valueMetric", "CheckIn")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidKeyResultId() {
        validator.validateOnDelete(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
        verify(validator, times(1)).doesEntityExist(1L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfKeyResultIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnDelete(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "CheckIn")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        Assertions.assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

}