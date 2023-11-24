package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ExtendWith(MockitoExtension.class)
class KeyResultBusinessServiceTest {
    private static final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    @Mock
    KeyResultPersistenceService keyResultPersistenceService;
    @Mock
    CheckInBusinessService checkInBusinessService;
    @Mock
    KeyResultValidationService validator;
    @Mock
    ActionBusinessService actionBusinessService;
    @InjectMocks
    private KeyResultBusinessService keyResultBusinessService;
    List<KeyResult> keyResults;
    User user;
    Objective objective;
    KeyResult metricKeyResult;
    KeyResult ordinalKeyResult;
    CheckIn checkIn1;
    CheckIn checkIn2;
    CheckIn checkIn3;
    List<CheckIn> checkIns;
    List<Action> actions;

    @BeforeEach
    void setup() {
        user = User.Builder.builder().withId(1L).withEmail("newMail@tese.com").build();

        objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();

        metricKeyResult = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0).withId(5L)
                .withTitle("Keyresult Metric").withObjective(objective).withOwner(user).withCreatedBy(user).build();
        ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withStretchZone("Wald").withId(7L)
                .withTitle("Keyresult Ordinal").withObjective(objective).withOwner(user).withCreatedBy(user).build();

        checkIn1 = CheckInMetric.Builder.builder().withId(1L).withKeyResult(metricKeyResult).withCreatedBy(user)
                .build();
        checkIn2 = CheckInOrdinal.Builder.builder().withId(2L).withKeyResult(ordinalKeyResult).withCreatedBy(user)
                .build();
        checkIn3 = CheckInOrdinal.Builder.builder().withId(3L).withKeyResult(ordinalKeyResult).withCreatedBy(user)
                .build();
        keyResults = List.of(metricKeyResult, ordinalKeyResult);
        checkIns = List.of(checkIn1, checkIn2, checkIn3);
        Action action = Action.Builder.builder().withId(3L).withAction("Neues Haus").withPriority(1).withIsChecked(true)
                .withKeyResult(metricKeyResult).build();
        actions = List.of(action, action);
    }

    @Test
    void shouldGetMetricKeyResultById() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(metricKeyResult);
        KeyResult keyResult = keyResultBusinessService.getEntityById(1L);

        assertEquals("Keyresult Metric", keyResult.getTitle());
        assertEquals(5, keyResult.getId());
    }

    @Test
    void shouldGetOrdinalKeyResultById() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(ordinalKeyResult);
        KeyResult keyResult = keyResultBusinessService.getEntityById(1L);

        assertEquals("Keyresult Ordinal", keyResult.getTitle());
        assertEquals(7, keyResult.getId());
    }

    @Test
    void shouldThrowExceptionWhenDefaultMethodUsed() {
        IllegalCallerException exception = assertThrows(IllegalCallerException.class, () -> keyResultBusinessService
                .updateEntity(metricKeyResult.getId(), metricKeyResult, authorizationUser));

        assertEquals("unsupported method 'updateEntity' use updateEntities() instead", exception.getMessage());
    }

    @Test
    void shouldEditMetricKeyResultWhenNoTypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Metric update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(metricKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateEntities(newKeyresult.getId(), newKeyresult, List.of());
        verify(keyResultPersistenceService, times(1)).updateEntity(newKeyresult);
        verify(checkInBusinessService, times(0)).getCheckInsByKeyResultId(1L);
        verify(actionBusinessService, times(0)).getActionsByKeyResultId(any());
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @Test
    void shouldEditOrdinalKeyResultWhenNoTypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withId(1L).withTitle("Keyresult Ordinal update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateEntities(newKeyresult.getId(), newKeyresult, List.of());
        verify(keyResultPersistenceService, times(1)).updateEntity(newKeyresult);
        verify(checkInBusinessService, times(0)).getCheckInsByKeyResultId(1L);
        verify(actionBusinessService, times(0)).getActionsByKeyResultId(any());
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Ordinal update", newKeyresult.getTitle());
    }

    @Test
    void shouldEditMetricKeyResultWhenATypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Metric update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.recreateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateEntities(newKeyresult.getId(), newKeyresult, actions);
        verify(keyResultPersistenceService, times(1)).recreateEntity(1L, newKeyresult);
        verify(checkInBusinessService, times(1)).getCheckInsByKeyResultId(1L);
        verify(actionBusinessService, times(1)).deleteEntitiesByKeyResultId(1L);
        verify(actionBusinessService, times(1)).createEntities(actions);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @Test
    void shouldEditOrdinalKeyResultWhenATypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withId(1L).withTitle("Keyresult Ordinal update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(metricKeyResult);
        Mockito.when(keyResultPersistenceService.recreateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);

        keyResultBusinessService.updateEntities(newKeyresult.getId(), newKeyresult, actions);
        verify(keyResultPersistenceService, times(1)).recreateEntity(1L, newKeyresult);
        verify(checkInBusinessService, times(1)).getCheckInsByKeyResultId(1L);
        verify(actionBusinessService, times(1)).deleteEntitiesByKeyResultId(1L);
        verify(actionBusinessService, times(1)).createEntities(actions);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Ordinal update", newKeyresult.getTitle());
    }

    @Test
    void shouldOnlyEditCoupleOfAttributesFromMetricKeyResultWhenATypeChangeAndCheckIns() {
        List<CheckIn> emptyList = checkIns;
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Metric update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);

        keyResultBusinessService.updateEntities(newKeyresult.getId(), newKeyresult, List.of());
        verify(keyResultPersistenceService, times(1)).updateEntity(ordinalKeyResult);
        verify(keyResultPersistenceService, times(0)).updateEntity(newKeyresult);
        verify(checkInBusinessService, times(1)).getCheckInsByKeyResultId(1L);
        verify(actionBusinessService, times(0)).getActionsByKeyResultId(any());
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @Test
    void shouldOnlyEditCoupleOfAttributesFromOrdinalKeyResultWhenATypeChangeAndCheckIns() {
        List<CheckIn> emptyList = checkIns;
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withId(1L).withTitle("Keyresult Ordinal update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(metricKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateEntities(newKeyresult.getId(), newKeyresult, List.of());
        verify(keyResultPersistenceService, times(1)).updateEntity(metricKeyResult);
        verify(keyResultPersistenceService, times(0)).updateEntity(newKeyresult);
        verify(checkInBusinessService, times(1)).getCheckInsByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Ordinal update", newKeyresult.getTitle());
    }

    @Test
    void saveMetricKeyResult() {
        KeyResult newKeyresult = spy(KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(8.0).withId(1L)
                .withTitle("Keyresult Metric save").withDescription("The description").build());
        Mockito.when(keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult savedKeyResult = keyResultBusinessService.createEntity(newKeyresult, authorizationUser);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult Metric save", savedKeyResult.getTitle());
    }

    @Test
    void saveOrdinalKeyResult() {
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withCommitZone("Eine Pflanze").withTargetZone("Ein Baum").withId(1L)
                        .withTitle("Keyresult ordinal save").withDescription("The description").build());
        Mockito.when(keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult savedKeyResult = keyResultBusinessService.createEntity(newKeyresult, authorizationUser);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult ordinal save", savedKeyResult.getTitle());
    }

    @Test
    void shouldBePossibleToSaveMetricKeyResultWithoutDescription() {
        KeyResult newKeyresult = spy(KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(8.0).withId(1L)
                .withTitle("Keyresult Metric save").build());
        Mockito.when(keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult keyResult = keyResultBusinessService.createEntity(newKeyresult, authorizationUser);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult Metric save", keyResult.getTitle());
    }

    @Test
    void shouldBePossibleToSaveOrdinalKeyResultWithoutDescription() {
        KeyResult newKeyresult = spy(KeyResultOrdinal.Builder.builder().withCommitZone("Eine Pflanze")
                .withTargetZone("Ein Baum").withId(1L).withTitle("Keyresult ordinal save").build());
        Mockito.when(keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult keyResult = keyResultBusinessService.createEntity(newKeyresult, authorizationUser);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult ordinal save", keyResult.getTitle());
        assertNull(keyResult.getDescription());
    }

    @Test
    void shouldGetAllKeyResultsByObjective() {
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(keyResults);

        List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(1L);

        assertEquals(2, keyResultList.size());
        assertEquals("Keyresult Metric", keyResultList.get(0).getTitle());
        assertEquals("Keyresult Ordinal", keyResultList.get(1).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoKeyResultInObjective() {
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(Collections.emptyList());

        List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(1L);

        assertEquals(0, keyResultList.size());
    }

    @Test
    void shouldGetAllCheckInsByKeyResult() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(metricKeyResult);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(checkIns);

        List<CheckIn> checkInList = keyResultBusinessService.getAllCheckInsByKeyResult(1L);

        assertEquals(3, checkInList.size());
        assertEquals(1, checkInList.get(0).getId());
        assertEquals("Keyresult Metric", checkInList.get(0).getKeyResult().getTitle());
        assertEquals("Objective 1", checkInList.get(0).getKeyResult().getObjective().getTitle());
        assertEquals("newMail@tese.com", checkInList.get(0).getCreatedBy().getEmail());
    }

    @Test
    void shouldReturnEmptyListWhenNoCheckInsInKeyResult() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(metricKeyResult);

        List<CheckIn> checkInList = keyResultBusinessService.getAllCheckInsByKeyResult(1L);

        assertEquals(0, checkInList.size());
    }

    @Test
    void shouldThrowExceptionWhenGetcheckInsFromNonExistingKeyResult() {
        when(keyResultPersistenceService.findById(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultBusinessService.getAllCheckInsByKeyResult(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("KeyResult with id 1 not found", exception.getReason());
    }

    @Test
    void shouldDeleteKeyResultAndAssociatedCheckInsAndActions() {
        when(checkInBusinessService.getCheckInsByKeyResultId(1L)).thenReturn(checkIns);
        when(actionBusinessService.getActionsByKeyResultId(1L)).thenReturn(actions);

        keyResultBusinessService.deleteEntityById(1L);

        verify(checkInBusinessService, times(1)).deleteEntityById(1L);
        verify(actionBusinessService, times(2)).deleteEntityById(3L);
        verify(keyResultPersistenceService, times(1)).deleteById(1L);
    }

    @Test
    void shouldReturnImUsedProperlyFalse1() {
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(Collections.emptyList());

        boolean returnValue = keyResultBusinessService.isImUsed(1L, metricKeyResult);

        assertFalse(returnValue);
    }

    @Test
    void shouldReturnImUsedProperlyFalse2() {
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(Collections.emptyList());

        boolean returnValue = keyResultBusinessService.isImUsed(1L, ordinalKeyResult);

        assertFalse(returnValue);
    }

    @Test
    void shouldReturnImUsedProperlyFalse3() {
        when(keyResultPersistenceService.findById(any())).thenReturn(metricKeyResult);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(checkIns);

        boolean returnValue = keyResultBusinessService.isImUsed(1L, metricKeyResult);

        assertFalse(returnValue);
    }

    @Test
    void shouldReturnImUsedProperlyTrue1() {
        when(keyResultPersistenceService.findById(any())).thenReturn(metricKeyResult);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(checkIns);

        boolean returnValue = keyResultBusinessService.isImUsed(1L, ordinalKeyResult);

        assertTrue(returnValue);
    }
}
