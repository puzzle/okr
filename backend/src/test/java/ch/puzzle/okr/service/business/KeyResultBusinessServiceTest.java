package ch.puzzle.okr.service.business;

import static ch.puzzle.okr.test.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
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
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

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
    @Mock
    AlignmentBusinessService alignmentBusinessService;
    @Mock
    ObjectiveBusinessService objectiveBusinessService;
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

        metricKeyResult = KeyResultMetric.Builder
                .builder()
                .withBaseline(4.0)
                .withStretchGoal(7.0)
                .withId(5L)
                .withTitle("Keyresult Metric")
                .withObjective(objective)

                .withOwner(user)

                .withCreatedBy(user)
                .build();
        ordinalKeyResult = KeyResultOrdinal.Builder
                .builder()
                .withCommitZone("Baum")
                .withStretchZone("Wald")
                .withId(7L)
                .withTitle("Keyresult Ordinal")
                .withObjective(objective)
                .withOwner(user)
                .withCreatedBy(user)
                .build();

        checkIn1 = CheckInMetric.Builder
                .builder()
                .withId(1L)
                .withKeyResult(metricKeyResult)
                .withCreatedBy(user)
                .build();
        checkIn2 = CheckInOrdinal.Builder
                .builder()
                .withId(2L)
                .withKeyResult(ordinalKeyResult)
                .withCreatedBy(user)
                .build();
        checkIn3 = CheckInOrdinal.Builder
                .builder()
                .withId(3L)
                .withKeyResult(ordinalKeyResult)
                .withCreatedBy(user)
                .build();
        keyResults = List.of(metricKeyResult, ordinalKeyResult);
        checkIns = List.of(checkIn1, checkIn2, checkIn3);
        Action action = Action.Builder
                .builder()
                .withId(3L)
                .withAction("Neues Haus")
                .withPriority(1)
                .isChecked(true)
                .withKeyResult(metricKeyResult)
                .build();
        actions = List.of(action, action);
    }

    @DisplayName("Should return correct metric key-result")
    @Test
    void shouldGetMetricKeyResultById() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(metricKeyResult);
        KeyResult keyResult = keyResultBusinessService.getEntityById(1L);

        assertEquals("Keyresult Metric", keyResult.getTitle());
        assertEquals(5, keyResult.getId());
    }

    @DisplayName("Should return correct ordinal key-result")
    @Test
    void shouldGetOrdinalKeyResultById() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(ordinalKeyResult);
        KeyResult keyResult = keyResultBusinessService.getEntityById(1L);

        assertEquals("Keyresult Ordinal", keyResult.getTitle());
        assertEquals(7, keyResult.getId());
    }

    @DisplayName("Should throw exception when calling updateEntity() instead of updateEntities()")
    @Test
    void shouldThrowExceptionWhenDefaultMethodUsed() {
        Long id = metricKeyResult.getId();
        IllegalCallerException exception = assertThrows(IllegalCallerException.class,
                                                        () -> keyResultBusinessService
                                                                .updateEntity(id, metricKeyResult, authorizationUser));

        assertEquals("unsupported method 'updateEntity' use updateEntities() instead", exception.getMessage());
    }

    @DisplayName("Should edit metric key-result without changing type")
    @Test
    void shouldEditMetricKeyResultWithoutTypeChange() {
        KeyResult newKeyresult = spy(KeyResultMetric.Builder
                .builder()
                .withId(1L)
                .withTitle("Keyresult Metric update")
                .build());
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

    @DisplayName("Should edit ordinal key-result without changing type")
    @Test
    void shouldEditOrdinalKeyResultWithoutTypeChange() {
        KeyResult newKeyresult = spy(KeyResultOrdinal.Builder
                .builder()
                .withId(1L)
                .withTitle("Keyresult Ordinal update")
                .build());
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

    @DisplayName("Should edit metric key-result with type change")
    @Test
    void shouldEditMetricKeyResultWithTypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(KeyResultMetric.Builder
                .builder()
                .withId(1L)
                .withTitle("Keyresult Metric update")
                .build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.recreateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateEntities(newKeyresult.getId(), newKeyresult, actions);
        verify(keyResultPersistenceService, times(1)).recreateEntity(1L, newKeyresult);
        verify(checkInBusinessService, times(1)).getCheckInsByKeyResultId(1L);
        verify(actionBusinessService, times(1)).deleteEntitiesByKeyResultId(1L);
        verify(actionBusinessService, times(1)).createEntities(actions);
        verify(alignmentBusinessService, times(1)).updateKeyResultId(1L, newKeyresult);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @DisplayName("Should edit ordinal key-result with type change")
    @Test
    void shouldEditOrdinalKeyResultWithTypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(KeyResultOrdinal.Builder
                .builder()
                .withId(1L)
                .withTitle("Keyresult Ordinal update")
                .build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(metricKeyResult);
        Mockito.when(keyResultPersistenceService.recreateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);

        keyResultBusinessService.updateEntities(newKeyresult.getId(), newKeyresult, actions);
        verify(keyResultPersistenceService, times(1)).recreateEntity(1L, newKeyresult);
        verify(checkInBusinessService, times(1)).getCheckInsByKeyResultId(1L);
        verify(actionBusinessService, times(1)).deleteEntitiesByKeyResultId(1L);
        verify(actionBusinessService, times(1)).createEntities(actions);
        verify(alignmentBusinessService, times(1)).updateKeyResultId(1L, newKeyresult);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Ordinal update", newKeyresult.getTitle());
    }

    // No displayname because i dont understand the test
    @Test
    void shouldOnlyEditCoupleOfAttributesFromMetricKeyResultWhenATypeChangeAndCheckIns() {
        List<CheckIn> emptyList = checkIns;
        KeyResult newKeyresult = spy(KeyResultMetric.Builder
                .builder()
                .withId(1L)
                .withTitle("Keyresult Metric update")
                .build());
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

    // No displayname because i dont understand the test
    @Test
    void shouldOnlyEditCoupleOfAttributesFromOrdinalKeyResultWhenATypeChangeAndCheckIns() {
        List<CheckIn> emptyList = checkIns;
        KeyResult newKeyresult = spy(KeyResultOrdinal.Builder
                .builder()
                .withId(1L)
                .withTitle("Keyresult Ordinal update")
                .build());
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

    @DisplayName("Should save metric key-result")
    @Test
    void shouldSaveMetricKeyResult() {
        KeyResult newKeyresult = spy(KeyResultMetric.Builder
                .builder()
                .withBaseline(4.0)
                .withStretchGoal(8.0)
                .withId(1L)
                .withTitle("Keyresult Metric save")
                .withDescription("The description")
                .build());
        Mockito.when(keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult savedKeyResult = keyResultBusinessService.createEntity(newKeyresult, authorizationUser);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult Metric save", savedKeyResult.getTitle());
    }

    @DisplayName("Should save ordinal key-result")
    @Test
    void shouldSaveOrdinalKeyResult() {
        KeyResult newKeyresult = spy(KeyResultOrdinal.Builder
                .builder()
                .withCommitZone("Eine Pflanze")
                .withTargetZone("Ein Baum")
                .withId(1L)
                .withTitle("Keyresult ordinal save")
                .withDescription("The description")
                .build());
        Mockito.when(keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult savedKeyResult = keyResultBusinessService.createEntity(newKeyresult, authorizationUser);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult ordinal save", savedKeyResult.getTitle());
    }

    @DisplayName("Should be possible to save metric key-result without description")
    @Test
    void shouldBePossibleToSaveMetricKeyResultWithoutDescription() {
        KeyResult newKeyresult = spy(KeyResultMetric.Builder
                .builder()
                .withBaseline(4.0)
                .withStretchGoal(8.0)
                .withId(1L)
                .withTitle("Keyresult Metric save")
                .build());
        Mockito.when(keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult keyResult = keyResultBusinessService.createEntity(newKeyresult, authorizationUser);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult Metric save", keyResult.getTitle());
    }

    @DisplayName("Should be possible to save ordinal key-result without description")
    @Test
    void shouldBePossibleToSaveOrdinalKeyResultWithoutDescription() {
        KeyResult newKeyresult = spy(KeyResultOrdinal.Builder
                .builder()
                .withCommitZone("Eine Pflanze")
                .withTargetZone("Ein Baum")
                .withId(1L)
                .withTitle("Keyresult ordinal save")
                .build());
        Mockito.when(keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult keyResult = keyResultBusinessService.createEntity(newKeyresult, authorizationUser);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult ordinal save", keyResult.getTitle());
        assertNull(keyResult.getDescription());
    }

    @DisplayName("Should return all key-results by objective no matter the type")
    @Test
    void shouldGetAllKeyResultsByObjective() {
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(keyResults);

        List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(1L);

        assertEquals(2, keyResultList.size());
        assertEquals("Keyresult Metric", keyResultList.get(0).getTitle());
        assertEquals("Keyresult Ordinal", keyResultList.get(1).getTitle());
    }

    @DisplayName("Should return an empty list when no key-results are found by objective")
    @Test
    void shouldReturnEmptyListWhenNoKeyResultInObjective() {
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(Collections.emptyList());

        List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(1L);

        assertEquals(0, keyResultList.size());
    }

    @DisplayName("Should get all check-ins by key-result")
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

    @DisplayName("Should return an empty list when no check-ins are found by key-result")
    @Test
    void shouldReturnEmptyListWhenNoCheckInsInKeyResult() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(metricKeyResult);

        List<CheckIn> checkInList = keyResultBusinessService.getAllCheckInsByKeyResult(1L);

        assertEquals(0, checkInList.size());
    }

    @DisplayName("Should throw exception when getting check-ins from an non-existent key-result")
    @Test
    void shouldThrowExceptionWhenGettingCheckInsFromNonExistentKeyResult() {
        when(keyResultPersistenceService.findById(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultBusinessService.getAllCheckInsByKeyResult(1L));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("KeyResult with id 1 not found", exception.getReason());
    }

    @DisplayName("Should delete key-result and associated check-ins and actions")
    @Test
    void shouldDeleteKeyResultAndAssociatedCheckInsAndActions() {
        when(checkInBusinessService.getCheckInsByKeyResultId(1L)).thenReturn(checkIns);
        when(actionBusinessService.getActionsByKeyResultId(1L)).thenReturn(actions);

        keyResultBusinessService.deleteEntityById(1L);

        verify(checkInBusinessService, times(1)).deleteEntityById(1L);
        verify(actionBusinessService, times(2)).deleteEntityById(3L);
        verify(keyResultPersistenceService, times(1)).deleteById(1L);
    }

    @DisplayName("Should return false for imUsed on unused metric key-result")
    @Test
    void shouldReturnFalseForImUsedOnMetricKeyResult() {
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(Collections.emptyList());

        boolean returnValue = keyResultBusinessService.isImUsed(1L, metricKeyResult);

        assertFalse(returnValue);
    }

    @DisplayName("Should return false for imUsed on unused ordinal key-result")
    @Test
    void shouldReturnFalseForImUsedOnOrdinalKeyResult() {
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(Collections.emptyList());

        boolean returnValue = keyResultBusinessService.isImUsed(1L, ordinalKeyResult);

        assertFalse(returnValue);
    }

    @DisplayName("Should return false for isImUsed() on metric key-result with check-ins")
    @Test
    void shouldReturnFalseForImUsedOnMetricKeyResultWithCheckIns() {
        when(keyResultPersistenceService.findById(any())).thenReturn(metricKeyResult);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(checkIns);

        boolean returnValue = keyResultBusinessService.isImUsed(1L, metricKeyResult);

        assertFalse(returnValue);
    }

    @DisplayName("Should return true for isImUsed() on ordinal key-result type changing type with check-ins")
    @Test
    void shouldReturnTrueForImUsedOnKeyResultWithCheckInsAfterTypeChange() {
        when(keyResultPersistenceService.findById(any())).thenReturn(metricKeyResult);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(checkIns);

        boolean returnValue = keyResultBusinessService.isImUsed(1L, ordinalKeyResult);

        assertTrue(returnValue);
    }

    // To be refactored
    @DisplayName("Should duplicate metric key result")
    @Test
    void shouldDuplicateMetricKeyResult() {
        Objective objective = Objective.Builder
                .builder() //
                .withId(23L) //
                .withTitle("Objective 1") //
                .build();
        KeyResult keyResultMetric = KeyResultMetric.Builder.builder().withId(1L).withTitle("Metric").build();

        KeyResult newKeyResult = KeyResultMetric.Builder.builder().withId(2L).withTitle("Metric Copy").build();

        when(keyResultBusinessService.makeCopyOfKeyResultMetric(keyResultMetric, objective))
                .thenReturn(keyResultMetric);
        when(keyResultBusinessService.createEntity(any(KeyResult.class), any(AuthorizationUser.class)))
                .thenReturn(newKeyResult);

        objectiveBusinessService.duplicateKeyResult(authorizationUser, keyResultMetric, objective);

        verify(keyResultBusinessService, times(1)).makeCopyOfKeyResultMetric(keyResultMetric, objective);
        verify(keyResultBusinessService, times(1)).createEntity(any(KeyResult.class), any(AuthorizationUser.class));
        verify(actionBusinessService, times(1)).createDuplicates(keyResultMetric, newKeyResult);
    }

    @DisplayName("Should duplicate ordinal key result")
    @Test
    void shouldDuplicateOrdinalKeyResult() {
        Objective objective = Objective.Builder
                .builder() //
                .withId(23L) //
                .withTitle("Objective 1") //
                .build();
        KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withId(1L).withTitle("Ordinal").build();

        KeyResult newKeyResult = KeyResultOrdinal.Builder.builder().withId(2L).withTitle("Ordinal Copy").build();

        when(keyResultBusinessService.makeCopyOfKeyResultOrdinal(keyResultOrdinal, objective))
                .thenReturn(keyResultOrdinal);
        when(keyResultBusinessService.createEntity(any(KeyResult.class), any(AuthorizationUser.class)))
                .thenReturn(newKeyResult);

        objectiveBusinessService.duplicateKeyResult(authorizationUser, keyResultOrdinal, objective);

        verify(keyResultBusinessService, times(1)).makeCopyOfKeyResultOrdinal(keyResultOrdinal, objective);
        verify(keyResultBusinessService, times(1)).createEntity(any(KeyResult.class), any(AuthorizationUser.class));
        verify(actionBusinessService, times(1)).createDuplicates(keyResultOrdinal, newKeyResult);
    }
}
