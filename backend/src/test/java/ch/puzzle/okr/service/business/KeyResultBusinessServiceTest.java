package ch.puzzle.okr.service.business;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.CheckInPersistenceService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KeyResultBusinessServiceTest {
    @MockBean
    KeyResultPersistenceService keyResultPersistenceService = Mockito.mock(KeyResultPersistenceService.class);
    @MockBean
    CheckInBusinessService checkInBusinessService = Mockito.mock(CheckInBusinessService.class);
    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);
    @MockBean
    UserBusinessService userBusinessService = Mockito.mock(UserBusinessService.class);
    @MockBean
    CheckInPersistenceService checkInPersistenceService = Mockito.mock(CheckInPersistenceService.class);
    @InjectMocks
    KeyResultValidationService validator = Mockito.mock(KeyResultValidationService.class);;
    List<KeyResult> keyResults;
    User user;
    Objective objective;
    KeyResult metricKeyResult;
    KeyResult ordinalKeyResult;
    CheckIn checkIn1;
    CheckIn checkIn2;
    CheckIn checkIn3;
    List<CheckIn> checkIns;
    Jwt jwtToken;
    @InjectMocks
    @Spy
    private KeyResultBusinessService keyResultBusinessService;

    @BeforeEach
    void setup() {
        this.user = User.Builder.builder().withId(1L).withEmail("newMail@tese.com").build();

        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();

        this.metricKeyResult = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0).withId(5L)
                .withTitle("Keyresult Metric").withObjective(this.objective).withOwner(this.user)
                .withCreatedBy(this.user).build();
        this.ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withStretchZone("Wald")
                .withId(7L).withTitle("Keyresult Ordinal").withObjective(this.objective).withOwner(this.user)
                .withCreatedBy(this.user).build();

        checkIn1 = CheckInMetric.Builder.builder().withId(1L).withKeyResult(this.metricKeyResult).withCreatedBy(user)
                .build();
        checkIn2 = CheckInOrdinal.Builder.builder().withId(2L).withKeyResult(this.ordinalKeyResult).withCreatedBy(user)
                .build();
        checkIn3 = CheckInOrdinal.Builder.builder().withId(3L).withKeyResult(this.ordinalKeyResult).withCreatedBy(user)
                .build();
        this.keyResults = List.of(this.metricKeyResult, this.ordinalKeyResult);
        this.checkIns = List.of(checkIn1, checkIn2, checkIn3);

        this.jwtToken = TestHelper.mockJwtToken("johnny", "Johnny", "Appleseed", "test@test.ch");
    }

    @Test
    void shouldGetMetricKeyResultById() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        KeyResult keyResult = keyResultBusinessService.getKeyResultById(1L);

        assertEquals("Keyresult Metric", keyResult.getTitle());
        assertEquals(5, keyResult.getId());
    }

    @Test
    void shouldGetOrdinalKeyResultById() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(this.ordinalKeyResult);
        KeyResult keyResult = keyResultBusinessService.getKeyResultById(1L);

        assertEquals("Keyresult Ordinal", keyResult.getTitle());
        assertEquals(7, keyResult.getId());
    }

    @Test
    void shouldEditMetricKeyResultWhenNoTypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Metric update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).updateEntity(newKeyresult);
        verify(checkInBusinessService, times(0)).getCheckInsByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @Test
    void shouldEditOrdinalKeyResultWhenNoTypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withId(1L).withTitle("Keyresult Ordinal update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).updateEntity(newKeyresult);
        verify(checkInBusinessService, times(0)).getCheckInsByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Ordinal update", newKeyresult.getTitle());
    }

    @Test
    void shouldEditMetricKeyResultWhenATypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Metric update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.recreateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).recreateEntity(1L, newKeyresult);
        verify(checkInBusinessService, times(1)).getCheckInsByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @Test
    void shouldEditOrdinalKeyResultWhenATypeChange() {
        List<CheckIn> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withId(1L).withTitle("Keyresult Ordinal update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        Mockito.when(keyResultPersistenceService.recreateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).recreateEntity(1L, newKeyresult);
        verify(checkInBusinessService, times(1)).getCheckInsByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Ordinal update", newKeyresult.getTitle());
    }

    @Test
    void shouldOnlyEditCoupleOfAttributesFromMetricKeyResultWhenATypeChangeAndCheckIns() {
        List<CheckIn> emptyList = this.checkIns;
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Metric update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).updateEntity(ordinalKeyResult);
        verify(keyResultPersistenceService, times(0)).updateEntity(newKeyresult);
        verify(checkInBusinessService, times(1)).getCheckInsByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @Test
    void shouldOnlyEditCoupleOfAttributesFromOrdinalKeyResultWhenATypeChangeAndCheckIns() {
        List<CheckIn> emptyList = this.checkIns;
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withId(1L).withTitle("Keyresult Ordinal update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any())).thenReturn(newKeyresult);
        Mockito.when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
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

        KeyResult savedKeyResult = keyResultBusinessService.createKeyResult(newKeyresult, jwtToken);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult Metric save", savedKeyResult.getTitle());
    }

    @Test
    void saveOrdinalKeyResult() {
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withCommitZone("Eine Pflanze").withTargetZone("Ein Baum").withId(1L)
                        .withTitle("Keyresult ordinal save").withDescription("The description").build());
        Mockito.when(keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult savedKeyResult = keyResultBusinessService.createKeyResult(newKeyresult, jwtToken);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult ordinal save", savedKeyResult.getTitle());
    }

    @Test
    void shouldBePossibleToSaveMetricKeyResultWithoutDescription() {
        KeyResult newKeyresult = spy(KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(8.0).withId(1L)
                .withTitle("Keyresult Metric save").build());
        Mockito.when(this.keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult keyResult = this.keyResultBusinessService.createKeyResult(newKeyresult, jwtToken);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult Metric save", keyResult.getTitle());
    }

    @Test
    void shouldBePossibleToSaveOrdinalKeyResultWithoutDescription() {
        KeyResult newKeyresult = spy(KeyResultOrdinal.Builder.builder().withCommitZone("Eine Pflanze")
                .withTargetZone("Ein Baum").withId(1L).withTitle("Keyresult ordinal save").build());
        Mockito.when(this.keyResultPersistenceService.save(any())).thenReturn(newKeyresult);
        Mockito.when(userBusinessService.getUserByAuthorisationToken(any())).thenReturn(user);
        doNothing().when(newKeyresult).setCreatedOn(any());

        KeyResult keyResult = this.keyResultBusinessService.createKeyResult(newKeyresult, jwtToken);
        verify(keyResultPersistenceService, times(1)).save(newKeyresult);
        assertEquals("Keyresult ordinal save", keyResult.getTitle());
        assertNull(keyResult.getDescription());
    }

    @Test
    void shouldGetAllKeyResultsByObjective() {
        when(objectivePersistenceService.findById(1L)).thenReturn(objective);
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(keyResults);

        List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(1L);

        assertEquals(2, keyResultList.size());
        assertEquals("Keyresult Metric", keyResultList.get(0).getTitle());
        assertEquals("Keyresult Ordinal", keyResultList.get(1).getTitle());
    }

    @Test
    void shouldReturnEmptyListWhenNoKeyResultInObjective() {
        when(objectivePersistenceService.findById(1L)).thenReturn(objective);
        when(keyResultPersistenceService.getKeyResultsByObjective(any())).thenReturn(Collections.emptyList());

        List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(1L);

        assertEquals(0, keyResultList.size());
    }

    @Test
    void shouldThrowExceptionWhenObjectiveDoesntExist() {
        when(objectivePersistenceService.findById(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective with id 1 not found"));
        assertThrows(ResponseStatusException.class, () -> keyResultBusinessService.getAllKeyResultsByObjective(1L));
    }

    @Test
    void shouldGetAllcheckInsByKeyResult() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        when(checkInPersistenceService.getCheckInsByKeyResultIdOrderByCheckInDateDesc(any())).thenReturn(checkIns);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(checkIns);

        List<CheckIn> checkInList = keyResultBusinessService.getAllCheckInsByKeyResult(1);

        assertEquals(3, checkInList.size());
        assertEquals(1, checkInList.get(0).getId());
        assertEquals("Keyresult Metric", checkInList.get(0).getKeyResult().getTitle());
        assertEquals("Objective 1", checkInList.get(0).getKeyResult().getObjective().getTitle());
        assertEquals("newMail@tese.com", checkInList.get(0).getCreatedBy().getEmail());
    }

    @Test
    void shouldReturnEmptyListWhenNocheckInsInKeyResult() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        when(checkInPersistenceService.getCheckInsByKeyResultIdOrderByCheckInDateDesc(any()))
                .thenReturn(Collections.emptyList());

        List<CheckIn> checkInList = keyResultBusinessService.getAllCheckInsByKeyResult(1);

        assertEquals(0, checkInList.size());
    }

    @Test
    void shouldThrowExceptionWhenGetcheckInsFromNonExistingKeyResult() {
        when(keyResultPersistenceService.findById(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultBusinessService.getAllCheckInsByKeyResult(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("KeyResult with id 1 not found", exception.getReason());
    }

    @Test
    void shouldDeleteKeyResultAndAssociatedCheckIns() {
        when(this.keyResultPersistenceService.findById(anyLong())).thenReturn(metricKeyResult);
        when(this.checkInPersistenceService.getCheckInsByKeyResultIdOrderByCheckInDateDesc(any())).thenReturn(checkIns);
        when(checkInBusinessService.getCheckInsByKeyResultId(1L)).thenReturn(checkIns);

        this.keyResultBusinessService.deleteKeyResultById(1L);

        verify(this.checkInBusinessService, times(1)).deleteCheckIn(1L);
        verify(this.keyResultBusinessService, times(1)).deleteKeyResultById(1L);
    }

    @Test
    void shouldReturnImUsedProperly_False1() {
        when(keyResultPersistenceService.findById(any())).thenReturn(this.metricKeyResult);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(Collections.emptyList());

        boolean returnValue = this.keyResultBusinessService.isImUsed(1L, this.metricKeyResult);

        assertFalse(returnValue);
    }

    @Test
    void shouldReturnImUsedProperly_False2() {
        when(keyResultPersistenceService.findById(any())).thenReturn(this.metricKeyResult);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(Collections.emptyList());

        boolean returnValue = this.keyResultBusinessService.isImUsed(1L, this.ordinalKeyResult);

        assertFalse(returnValue);
    }

    @Test
    void shouldReturnImUsedProperly_False3() {
        when(keyResultPersistenceService.findById(any())).thenReturn(this.metricKeyResult);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(checkIns);

        boolean returnValue = this.keyResultBusinessService.isImUsed(1L, this.metricKeyResult);

        assertFalse(returnValue);
    }

    @Test
    void shouldReturnImUsedProperly_True1() {
        when(keyResultPersistenceService.findById(any())).thenReturn(this.metricKeyResult);
        when(checkInBusinessService.getCheckInsByKeyResultId(any())).thenReturn(checkIns);

        boolean returnValue = this.keyResultBusinessService.isImUsed(1L, this.ordinalKeyResult);

        assertTrue(returnValue);
    }
}
