package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.MeasureRepository;
import org.springframework.stereotype.Service;

@Service
public class GoalService {

    private final MeasureRepository measureRepository;

    public GoalService(MeasureRepository measureRepository) {
        this.measureRepository = measureRepository;
    }

    public Double getCurrentValue(KeyResult keyResult) {
        Measure measure = this.measureRepository
                .findFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResult.getId());
        return measure != null ? measure.getValue() : null;
    }
}
