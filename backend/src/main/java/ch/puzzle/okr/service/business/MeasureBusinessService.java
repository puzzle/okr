package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class MeasureBusinessService {
    private final MeasurePersistenceService measurePersistenceService;
    private final ObjectiveBusinessService objectiveBusinessService;

    public MeasureBusinessService(MeasurePersistenceService measurePersistenceService,
            ObjectiveBusinessService objectiveBusinessService) {
        this.measurePersistenceService = measurePersistenceService;
        this.objectiveBusinessService = objectiveBusinessService;
    }

    public Measure saveMeasure(Measure measure) {
        if (measure.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Measure has already an Id");
        }
        checkMeasure(measure);
        Measure createdMeasure = measurePersistenceService.saveMeasure(measure);
        objectiveBusinessService.updateObjectiveProgress(createdMeasure.getKeyResult().getObjective().getId());
        return createdMeasure;
    }

    public Measure updateMeasure(Long id, Measure measure) {
        checkMeasure(measure);
        Measure createdMeasure = measurePersistenceService.updateMeasure(id, measure);
        objectiveBusinessService.updateObjectiveProgress(createdMeasure.getKeyResult().getObjective().getId());
        return createdMeasure;
    }

    private void checkMeasure(Measure measure) {
        if (measure.getKeyResult() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given keyresult does not exist");
        }
        if (measure.getValue() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given measure does not have a value");
        }
        if (measure.getChangeInfo().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given change value is blank");
        }
        if (measure.getCreatedBy() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is null");
        }
        if (measure.getCreatedOn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given creation date is null");
        }
        if (measure.getMeasureDate() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given measure date is null");
        }

        List<Measure> measureList = measurePersistenceService
                .getMeasuresByKeyResultIdAndMeasureDate(measure.getKeyResult().getId(), measure.getMeasureDate());
        if (!measureList.isEmpty()) {
            if (!Objects.equals(measureList.get(0).getId(), measure.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Only one Messung is allowed per day and Key Result!");
            }
        }
    }

    public List<Measure> getAllMeasures() {
        return measurePersistenceService.getAllMeasures();
    }

    public Measure getMeasureById(Long id) {
        return measurePersistenceService.getMeasureById(id);
    }

    public void deleteMeasureById(Long measureId) {
        measurePersistenceService.deleteMeasureById(measureId);
    }

    public Double getCurrentValue(KeyResult keyResult) {
        Measure measure = measurePersistenceService
                .getFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResult.getId());
        return measure != null ? measure.getValue() : null;
    }
}
