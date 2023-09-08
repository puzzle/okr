package ch.puzzle.okr.service.business;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
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
    TestHelper testHelper = new TestHelper();
    @MockBean
    KeyResultPersistenceService keyResultPersistenceService = Mockito.mock(KeyResultPersistenceService.class);
    @MockBean
    MeasureBusinessService measureBusinessService = Mockito.mock(MeasureBusinessService.class);
    @MockBean
    ObjectivePersistenceService objectivePersistenceService = Mockito.mock(ObjectivePersistenceService.class);
    @MockBean
    UserBusinessService userBusinessService = Mockito.mock(UserBusinessService.class);
    @MockBean
    MeasurePersistenceService measurePersistenceService = Mockito.mock(MeasurePersistenceService.class);
    @InjectMocks
    KeyResultValidationService validator = Mockito.mock(KeyResultValidationService.class);;
    List<KeyResult> keyResults;
    User user;
    Objective objective;
    KeyResult metricKeyResult;
    KeyResult ordinalKeyResult;
    Measure measure1;
    Measure measure2;
    Measure measure3;
    List<Measure> measures;
    Jwt jwtToken;
    @InjectMocks
    @Spy
    private KeyResultBusinessService keyResultBusinessService;

    @BeforeEach
    void setup() {
        this.user = User.Builder.builder().withId(1L).withEmail("newMail@tese.com").build();

        this.objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();

        this.metricKeyResult = KeyResultMetric.Builder.builder().withBaseline(4.0).withStretchGoal(7.0).withId(5L)
                .withKeyResultType("metric").withTitle("Keyresult Metric").withObjective(this.objective)
                .withOwner(this.user).withCreatedBy(this.user).build();
        this.ordinalKeyResult = KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withStretchZone("Wald")
                .withKeyResultType("ordinal").withId(7L).withTitle("Keyresult Ordinal").withObjective(this.objective)
                .withOwner(this.user).withCreatedBy(this.user).build();

        measure1 = Measure.Builder.builder().withId(1L).withKeyResult(this.metricKeyResult).withCreatedBy(user).build();
        measure2 = Measure.Builder.builder().withId(2L).withKeyResult(this.ordinalKeyResult).withCreatedBy(user)
                .build();
        measure3 = Measure.Builder.builder().withId(3L).withKeyResult(this.ordinalKeyResult).withCreatedBy(user)
                .build();
        this.keyResults = List.of(this.metricKeyResult, this.ordinalKeyResult);
        this.measures = List.of(measure1, measure2, measure3);

        this.jwtToken = testHelper.mockJwtToken("johnny", "Johnny", "Appleseed", "test@test.ch");
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
        List<Measure> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Metric update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).updateEntity(1L, newKeyresult);
        verify(measureBusinessService, times(1)).getMeasuresByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @Test
    void shouldEditOrdinalKeyResultWhenNoTypeChange() {
        List<Measure> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withId(1L).withTitle("Keyresult Ordinal update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).updateEntity(1L, newKeyresult);
        verify(measureBusinessService, times(1)).getMeasuresByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Ordinal update", newKeyresult.getTitle());
    }

    @Test
    void shouldEditMetricKeyResultWhenATypeChange() {
        List<Measure> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Metric update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).updateEntity(1L, newKeyresult);
        verify(measureBusinessService, times(1)).getMeasuresByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @Test
    void shouldEditOrdinalKeyResultWhenATypeChange() {
        List<Measure> emptyList = Collections.emptyList();
        KeyResult newKeyresult = spy(
                KeyResultOrdinal.Builder.builder().withId(1L).withTitle("Keyresult Ordinal update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        Mockito.when(keyResultPersistenceService.updateEntity(any(), any())).thenReturn(newKeyresult);
        Mockito.when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).updateEntity(1L, newKeyresult);
        verify(measureBusinessService, times(1)).getMeasuresByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Ordinal update", newKeyresult.getTitle());
    }

    @Test
    void shouldOnlyEditCoupleOfAttributesFromMetricKeyResultWhenATypeChangeAndMeasures() {
        List<Measure> emptyList = this.measures;
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Metric update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.ordinalKeyResult);
        Mockito.when(keyResultPersistenceService.updateAbstractEntity(any())).thenReturn(newKeyresult);
        Mockito.when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).updateAbstractEntity(ordinalKeyResult);
        verify(keyResultPersistenceService, times(0)).updateEntity(1L, newKeyresult);
        verify(measureBusinessService, times(1)).getMeasuresByKeyResultId(1L);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult Metric update", newKeyresult.getTitle());
    }

    @Test
    void shouldOnlyEditCoupleOfAttributesFromOrdinalKeyResultWhenATypeChangeAndMeasures() {
        List<Measure> emptyList = this.measures;
        KeyResult newKeyresult = spy(
                KeyResultMetric.Builder.builder().withId(1L).withTitle("Keyresult Ordinal update").build());
        Mockito.when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        Mockito.when(keyResultPersistenceService.updateAbstractEntity(any())).thenReturn(newKeyresult);
        Mockito.when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(emptyList);
        doNothing().when(newKeyresult).setModifiedOn(any());

        keyResultBusinessService.updateKeyResult(newKeyresult.getId(), newKeyresult);
        verify(keyResultPersistenceService, times(1)).updateAbstractEntity(metricKeyResult);
        verify(keyResultPersistenceService, times(0)).updateEntity(1L, newKeyresult);
        verify(measureBusinessService, times(1)).getMeasuresByKeyResultId(1L);
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
    void shouldGetAllMeasuresByKeyResult() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        when(measurePersistenceService.getMeasuresByKeyResultIdOrderByMeasureDateDesc(any())).thenReturn(measures);
        when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(measures);

        List<Measure> measureList = keyResultBusinessService.getAllMeasuresByKeyResult(1);

        assertEquals(3, measureList.size());
        assertEquals(1, measureList.get(0).getId());
        assertEquals("Keyresult Metric", measureList.get(0).getKeyResult().getTitle());
        assertEquals("Objective 1", measureList.get(0).getKeyResult().getObjective().getTitle());
        assertEquals("newMail@tese.com", measureList.get(0).getCreatedBy().getEmail());
    }

    @Test
    void shouldReturnEmptyListWhenNoMeasuresInKeyResult() {
        when(keyResultPersistenceService.findById(1L)).thenReturn(this.metricKeyResult);
        when(measurePersistenceService.getMeasuresByKeyResultIdOrderByMeasureDateDesc(any()))
                .thenReturn(Collections.emptyList());

        List<Measure> measureList = keyResultBusinessService.getAllMeasuresByKeyResult(1);

        assertEquals(0, measureList.size());
    }

    @Test
    void shouldThrowExceptionWhenGetMeasuresFromNonExistingKeyResult() {
        when(keyResultPersistenceService.findById(1L))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> keyResultBusinessService.getAllMeasuresByKeyResult(1));
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("KeyResult with id 1 not found", exception.getReason());
    }

    @Test
    void shouldDeleteKeyResultAndAssociatedCheckIns() {
        when(this.keyResultPersistenceService.findById(anyLong())).thenReturn(metricKeyResult);
        when(this.measurePersistenceService.getMeasuresByKeyResultIdOrderByMeasureDateDesc(any())).thenReturn(measures);
        when(measureBusinessService.getMeasuresByKeyResultId(1L)).thenReturn(measures);

        this.keyResultBusinessService.deleteKeyResultById(1L);

        verify(this.measureBusinessService, times(1)).deleteMeasureById(1L);
        verify(this.keyResultBusinessService, times(1)).deleteKeyResultById(1L);
    }

    @Test
    void shouldReturnImUsedProperly_False1() {
        when(keyResultPersistenceService.findById(any())).thenReturn(this.metricKeyResult);
        when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(Collections.emptyList());

        boolean returnValue = this.keyResultBusinessService.isImUsed(1L, this.metricKeyResult);

        assertFalse(returnValue);
    }

    @Test
    void shouldReturnImUsedProperly_False2() {
        when(keyResultPersistenceService.findById(any())).thenReturn(this.metricKeyResult);
        when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(Collections.emptyList());

        boolean returnValue = this.keyResultBusinessService.isImUsed(1L, this.ordinalKeyResult);

        assertFalse(returnValue);
    }

    @Test
    void shouldReturnImUsedProperly_False3() {
        when(keyResultPersistenceService.findById(any())).thenReturn(this.metricKeyResult);
        when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(measures);

        boolean returnValue = this.keyResultBusinessService.isImUsed(1L, this.metricKeyResult);

        assertFalse(returnValue);
    }

    @Test
    void shouldReturnImUsedProperly_True1() {
        when(keyResultPersistenceService.findById(any())).thenReturn(this.metricKeyResult);
        when(measureBusinessService.getMeasuresByKeyResultId(any())).thenReturn(measures);

        boolean returnValue = this.keyResultBusinessService.isImUsed(1L, this.ordinalKeyResult);

        assertTrue(returnValue);
    }
}
