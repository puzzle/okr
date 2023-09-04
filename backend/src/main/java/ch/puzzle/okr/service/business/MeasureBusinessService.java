package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.MeasurePersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class MeasureBusinessService {
    private final MeasurePersistenceService measurePersistenceService;

    public MeasureBusinessService(MeasurePersistenceService measurePersistenceService) {
        this.measurePersistenceService = measurePersistenceService;
    }

    public Measure saveMeasure(Measure measure) {
        if (measure.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Measure has already an Id");
        }
        checkMeasure(measure);
        Measure createdMeasure = measurePersistenceService.saveMeasure(measure);
        return createdMeasure;
    }

    public Measure updateMeasure(Long id, Measure measure) {
        checkMeasure(measure);
        Measure createdMeasure = measurePersistenceService.updateMeasure(id, measure);
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

    public List<Measure> getMeasuresByKeyResultId(Long keyResultId) {
        return measurePersistenceService.getMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResultId);
    }

    public Measure getFirstMeasureByKeyResult(Long id) {
        return measurePersistenceService.findFirstMeasureByKeyResultId(id);
    }

    public List<Measure> getAllMeasures() {
        return measurePersistenceService.getAllMeasures();
    }

    public Measure getMeasureById(Long id) {
        return measurePersistenceService.getMeasureById(id);
    }

    public Double getCurrentValue(KeyResult keyResult) {
        Measure measure = measurePersistenceService.findFirstMeasureByKeyResultId(keyResult.getId());
        return measure != null ? measure.getValue() : null;
    }
}
