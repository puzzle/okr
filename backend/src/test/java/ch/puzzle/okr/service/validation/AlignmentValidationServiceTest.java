package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static ch.puzzle.okr.models.State.DRAFT;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static ch.puzzle.okr.TestConstants.*;

@ExtendWith(MockitoExtension.class)
class AlignmentValidationServiceTest {

    @Mock
    AlignmentPersistenceService alignmentPersistenceService;
    @Mock
    TeamPersistenceService teamPersistenceService;
    @Spy
    @InjectMocks
    private AlignmentValidationService validator;

    Team team1 = Team.Builder.builder().withId(1L).withName(TEAM_PUZZLE).build();
    Team team2 = Team.Builder.builder().withId(2L).withName("BBT").build();
    Objective objective1 = Objective.Builder.builder().withId(5L).withTitle("Objective 1").withTeam(team1)
            .withState(DRAFT).build();
    Objective objective2 = Objective.Builder.builder().withId(8L).withTitle("Objective 2").withTeam(team2)
            .withState(DRAFT).build();
    Objective objective3 = Objective.Builder.builder().withId(10L).withTitle("Objective 3").withState(DRAFT).build();
    KeyResult metricKeyResult = KeyResultMetric.Builder.builder().withId(5L).withTitle("KR Title 1").build();
    ObjectiveAlignment objectiveALignment = ObjectiveAlignment.Builder.builder().withId(1L)
            .withAlignedObjective(objective1).withTargetObjective(objective2).build();
    ObjectiveAlignment createAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective2)
            .withTargetObjective(objective1).build();
    KeyResultAlignment keyResultAlignment = KeyResultAlignment.Builder.builder().withId(6L)
            .withAlignedObjective(objective3).withTargetKeyResult(metricKeyResult).build();
    List<Long> emptyLongList = List.of();

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(alignmentPersistenceService.getModelName()).thenReturn("Alignment");
    }

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidActionId() {
        // act
        validator.validateOnGet(1L);

        // assert
        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfIdIsNull() {
        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        // assert
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Alignment"))), exception.getErrors());
    }

    @Test
    void validateOnCreateShouldBeSuccessfulWhenAlignmentIsValid() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(anyLong())).thenReturn(null);
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        when(teamPersistenceService.findById(2L)).thenReturn(team2);

        // act
        validator.validateOnCreate(createAlignment);

        // assert
        verify(validator, times(1)).throwExceptionWhenModelIsNull(createAlignment);
        verify(validator, times(1)).throwExceptionWhenIdIsNotNull(null);
        verify(alignmentPersistenceService, times(1)).findByAlignedObjectiveId(8L);
        verify(validator, times(1)).validate(createAlignment);
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("MODEL_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("MODEL_NULL", List.of("Alignment"))), exception.getErrors());
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        // arrange
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "Alignment")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAlignedObjectiveIsNull() {
        // arrange
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withTargetObjective(objective2)
                .build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("alignedObjectiveId")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenTargetObjectiveIsNull() {
        // arrange
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective2)
                .build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("targetObjectiveId", "8")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenTargetKeyResultIsNull() {
        // arrange
        KeyResultAlignment wrongKeyResultAlignment = KeyResultAlignment.Builder.builder()
                .withAlignedObjective(objective2).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("targetKeyResultId", "8")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(wrongKeyResultAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAlignedIdIsSameAsTargetId() {
        // arrange
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective2)
                .withTargetObjective(objective2).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_YOURSELF", List.of("targetObjectiveId", "8")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAlignmentIsInSameTeamObjective() {
        // arrange
        when(teamPersistenceService.findById(2L)).thenReturn(team2);
        Objective objective = objective1;
        objective.setTeam(team2);
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective)
                .withTargetObjective(objective2).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_IN_SAME_TEAM", List.of("teamId", "2")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAlignmentIsInSameTeamKeyResult() {
        // arrange
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        KeyResult keyResult = KeyResultMetric.Builder.builder().withId(3L).withTitle("KeyResult 1").withObjective(objective1).build();
        KeyResultAlignment keyResultAlignment1 = KeyResultAlignment.Builder.builder().withAlignedObjective(objective1).withTargetKeyResult(keyResult).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_IN_SAME_TEAM", List.of("teamId", "1")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultAlignment1));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAlignedObjectiveAlreadyExists() {
        // arrange
        when(alignmentPersistenceService.findByAlignedObjectiveId(anyLong())).thenReturn(objectiveALignment);
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        when(teamPersistenceService.findById(2L)).thenReturn(team2);
        ObjectiveAlignment createAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective1).withTargetObjective(objective2).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ALIGNMENT_ALREADY_EXISTS", List.of("alignedObjectiveId", "5")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(createAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldBeSuccessfulWhenAlignmentIsValid() {
        // arrange
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        when(teamPersistenceService.findById(2L)).thenReturn(team2);

        // act
        validator.validateOnUpdate(objectiveALignment.getId(), objectiveALignment);

        // assert
        verify(validator, times(1)).throwExceptionWhenModelIsNull(objectiveALignment);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(objectiveALignment.getId());
        verify(validator, times(1)).validate(objectiveALignment);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("MODEL_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("MODEL_NULL", List.of("Alignment"))), exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        // arrange
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().build();

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, objectiveAlignment));

        // assert
        verify(validator, times(1)).throwExceptionWhenModelIsNull(objectiveAlignment);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Alignment"))), exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAlignedObjectiveIsNull() {
        // arrange
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withId(3L)
                .withTargetObjective(objective2).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("alignedObjectiveId")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, objectiveAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenTargetObjectiveIsNull() {
        // arrange
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withId(3L)
                .withAlignedObjective(objective2).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("targetObjectiveId", "8")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, objectiveAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenTargetKeyResultIsNull() {
        // arrange
        KeyResultAlignment wrongKeyResultAlignment = KeyResultAlignment.Builder.builder().withId(3L)
                .withAlignedObjective(objective2).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("targetKeyResultId", "8")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, wrongKeyResultAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAlignedIdIsSameAsTargetId() {
        // arrange
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withId(3L)
                .withAlignedObjective(objective2).withTargetObjective(objective2).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_YOURSELF", List.of("targetObjectiveId", "8")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, objectiveAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAlignmentIsInSameTeamObjective() {
        // arrange
        when(teamPersistenceService.findById(2L)).thenReturn(team2);
        Objective objective = objective1;
        objective.setTeam(team2);
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withId(3L)
                .withAlignedObjective(objective).withTargetObjective(objective2).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_IN_SAME_TEAM", List.of("teamId", "2")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, objectiveAlignment));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAlignmentIsInSameTeamKeyResult() {
        // arrange
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        KeyResult keyResult = KeyResultMetric.Builder.builder().withId(3L).withTitle("KeyResult 1").withObjective(objective1).build();
        KeyResultAlignment keyResultAlignment1 = KeyResultAlignment.Builder.builder().withId(2L).withAlignedObjective(objective1).withTargetKeyResult(keyResult).build();
        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_IN_SAME_TEAM", List.of("teamId", "1")));

        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, keyResultAlignment1));

        // assert
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidAlignmentId() {
        // act
        validator.validateOnDelete(3L);

        // assert
        verify(validator, times(1)).throwExceptionWhenIdIsNull(3L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfAlignmentIdIsNull() {
        // act
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnDelete(null));

        // assert
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);
        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Alignment"))), exception.getErrors());
    }

    @Test
    void validateOnAlignmentGetShouldBeSuccessfulWhenQuarterIdAndTeamFilterSet() {
        validator.validateOnAlignmentGet(2L, List.of(4L, 5L));

        verify(validator, times(1)).validateOnAlignmentGet(2L, List.of(4L, 5L));
    }

    @Test
    void validateOnAlignmentGetShouldThrowExceptionWhenQuarterIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnAlignmentGet(null, emptyLongList));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NOT_SET", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NOT_SET", List.of("quarterId"))), exception.getErrors());
    }

    @Test
    void validateOnAlignmentGetShouldThrowExceptionWhenTeamFilterIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnAlignmentGet(2L, null));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NOT_SET", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NOT_SET", List.of("teamFilter"))), exception.getErrors());
    }

    @Test
    void validateOnAlignmentGetShouldThrowExceptionWhenTeamFilterIsEmpty() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnAlignmentGet(2L, emptyLongList));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NOT_SET", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NOT_SET", List.of("teamFilter"))), exception.getErrors());
    }
}
