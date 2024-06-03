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

@ExtendWith(MockitoExtension.class)
class AlignmentValidationServiceTest {

    @Mock
    AlignmentPersistenceService alignmentPersistenceService;
    @Mock
    TeamPersistenceService teamPersistenceService;
    @Spy
    @InjectMocks
    private AlignmentValidationService validator;

    Team team1 = Team.Builder.builder().withId(1L).withName("Puzzle ITC").build();
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

    @BeforeEach
    void setUp() {
        Mockito.lenient().when(alignmentPersistenceService.getModelName()).thenReturn("Alignment");
    }

    @Test
    void validateOnGetShouldBeSuccessfulWhenValidActionId() {
        validator.validateOnGet(1L);

        verify(validator, times(1)).validateOnGet(1L);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(1L);
    }

    @Test
    void validateOnGetShouldThrowExceptionIfIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnGet(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Alignment"))), exception.getErrors());
    }

    @Test
     void validateOnCreateShouldBeSuccessfulWhenAlignmentIsValid() {
         when(alignmentPersistenceService.findByAlignedObjectiveId(anyLong())).thenReturn(null);
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        when(teamPersistenceService.findById(2L)).thenReturn(team2);

         validator.validateOnCreate(createAlignment);

         verify(validator, times(1)).throwExceptionWhenModelIsNull(createAlignment);
         verify(validator, times(1)).throwExceptionWhenIdIsNotNull(null);
         verify(alignmentPersistenceService, times(1)).findByAlignedObjectiveId(8L);
         verify(validator, times(1)).validate(createAlignment);
     }

    @Test
    void validateOnCreateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(null));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("MODEL_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("MODEL_NULL", List.of("Alignment"))), exception.getErrors());
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenIdIsNotNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NOT_NULL", List.of("ID", "Alignment")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAlignedObjectiveIsNull() {
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withTargetObjective(objective2)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("alignedObjectiveId")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenTargetObjectiveIsNull() {
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective2)
                .build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("targetObjectiveId", "8")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenTargetKeyResultIsNull() {
        KeyResultAlignment wrongKeyResultAlignment = KeyResultAlignment.Builder.builder()
                .withAlignedObjective(objective2).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(wrongKeyResultAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("targetKeyResultId", "8")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAlignedIdIsSameAsTargetId() {
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective2)
                .withTargetObjective(objective2).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_YOURSELF", List.of("targetObjectiveId", "8")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAlignmentIsInSameTeamObjective() {
        when(teamPersistenceService.findById(2L)).thenReturn(team2);
        Objective objective = objective1;
        objective.setTeam(team2);
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective)
                .withTargetObjective(objective2).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(objectiveAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_IN_SAME_TEAM", List.of("teamId", "2")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnCreateShouldThrowExceptionWhenAlignmentIsInSameTeamKeyResult() {
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        KeyResult keyResult = KeyResultMetric.Builder.builder().withId(3L).withTitle("KeyResult 1").withObjective(objective1).build();
        KeyResultAlignment keyResultAlignment1 = KeyResultAlignment.Builder.builder().withAlignedObjective(objective1).withTargetKeyResult(keyResult).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnCreate(keyResultAlignment1));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_IN_SAME_TEAM", List.of("teamId", "1")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
     void validateOnCreateShouldThrowExceptionWhenAlignedObjectiveAlreadyExists() {
        when(alignmentPersistenceService.findByAlignedObjectiveId(anyLong())).thenReturn(objectiveALignment);
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        when(teamPersistenceService.findById(2L)).thenReturn(team2);

         ObjectiveAlignment createAlignment = ObjectiveAlignment.Builder.builder().withAlignedObjective(objective1).withTargetObjective(objective2).build();

         OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
         () -> validator.validateOnCreate(createAlignment));

         List<ErrorDto> expectedErrors = List.of(new ErrorDto("ALIGNMENT_ALREADY_EXISTS", List.of("alignedObjectiveId", "5")));

         assertEquals(BAD_REQUEST, exception.getStatusCode());
         assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
         assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
     }

    @Test
    void validateOnUpdateShouldBeSuccessfulWhenAlignmentIsValid() {
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        when(teamPersistenceService.findById(2L)).thenReturn(team2);

        validator.validateOnUpdate(objectiveALignment.getId(), objectiveALignment);

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objectiveALignment);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(objectiveALignment.getId());
        verify(validator, times(1)).validate(objectiveALignment);
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenModelIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(1L, null));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("MODEL_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("MODEL_NULL", List.of("Alignment"))), exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenIdIsNull() {
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().build();
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(null, objectiveAlignment));

        verify(validator, times(1)).throwExceptionWhenModelIsNull(objectiveAlignment);
        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Alignment"))), exception.getErrors());
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAlignedObjectiveIsNull() {
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withId(3L)
                .withTargetObjective(objective2).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, objectiveAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("alignedObjectiveId")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenTargetObjectiveIsNull() {
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withId(3L)
                .withAlignedObjective(objective2).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, objectiveAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("targetObjectiveId", "8")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenTargetKeyResultIsNull() {
        KeyResultAlignment wrongKeyResultAlignment = KeyResultAlignment.Builder.builder().withId(3L)
                .withAlignedObjective(objective2).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, wrongKeyResultAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("targetKeyResultId", "8")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAlignedIdIsSameAsTargetId() {
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withId(3L)
                .withAlignedObjective(objective2).withTargetObjective(objective2).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(3L, objectiveAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_YOURSELF", List.of("targetObjectiveId", "8")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAlignmentIsInSameTeamObjective() {
        when(teamPersistenceService.findById(2L)).thenReturn(team2);
        Objective objective = objective1;
        objective.setTeam(team2);
        ObjectiveAlignment objectiveAlignment = ObjectiveAlignment.Builder.builder().withId(3L)
                .withAlignedObjective(objective).withTargetObjective(objective2).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, objectiveAlignment));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_IN_SAME_TEAM", List.of("teamId", "2")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnUpdateShouldThrowExceptionWhenAlignmentIsInSameTeamKeyResult() {
        when(teamPersistenceService.findById(1L)).thenReturn(team1);
        KeyResult keyResult = KeyResultMetric.Builder.builder().withId(3L).withTitle("KeyResult 1").withObjective(objective1).build();
        KeyResultAlignment keyResultAlignment1 = KeyResultAlignment.Builder.builder().withId(2L).withAlignedObjective(objective1).withTargetKeyResult(keyResult).build();

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnUpdate(2L, keyResultAlignment1));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("NOT_LINK_IN_SAME_TEAM", List.of("teamId", "1")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void validateOnDeleteShouldBeSuccessfulWhenValidAlignmentId() {
        validator.validateOnDelete(3L);

        verify(validator, times(1)).throwExceptionWhenIdIsNull(3L);
    }

    @Test
    void validateOnDeleteShouldThrowExceptionIfAlignmentIdIsNull() {
        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> validator.validateOnDelete(null));

        verify(validator, times(1)).throwExceptionWhenIdIsNull(null);

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
        assertEquals(List.of(new ErrorDto("ATTRIBUTE_NULL", List.of("ID", "Alignment"))), exception.getErrors());
    }

}
