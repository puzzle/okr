package ch.puzzle.okr.service;

import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProgressService {
    private final ObjectiveService objectiveService;
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final MeasureRepository measureRepository;

    public ProgressService(ObjectiveService objectiveService, ObjectiveRepository objectiveRepository,
            KeyResultRepository keyResultRepository, MeasureRepository measureRepository) {
        this.objectiveService = objectiveService;
        this.objectiveRepository = objectiveRepository;
        this.keyResultRepository = keyResultRepository;
        this.measureRepository = measureRepository;
    }

    public void updateObjectiveProgress(Long objectiveId) {
        List<KeyResult> keyResultList = this.keyResultRepository.findByObjectiveId(objectiveId);
        double objectiveProgress = keyResultList.stream().mapToDouble(this::calculateKeyResultProgress).average()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Progress calculation failed!"));

        Objective objective = this.objectiveService.getObjective(objectiveId);
        objective.setProgress((long) Math.floor(objectiveProgress));
        this.objectiveRepository.save(objective);
    }

    public Long calculateKeyResultProgress(KeyResult keyResult) {
        if (keyResult.getExpectedEvolution().equals(ExpectedEvolution.MIN)
                || keyResult.getExpectedEvolution().equals(ExpectedEvolution.MAX)) {
            return (long) Math.floor(calculateKeyResultProgressForMinMax(keyResult));

        } else {
            return (long) Math.floor(calculateKeyResultProgressForNoMinMax(keyResult));
        }
    }

    protected double calculateKeyResultProgressForMinMax(KeyResult keyResult) {
        Double targetValue = keyResult.getTargetValue();
        List<MeasureRepository.MeasureValue> measureValueList = this.measureRepository.findMeasuresByKeyResultId(keyResult.getId());

        if(measureValueList.isEmpty()){
            return 0D;
        }
        return Math.floor(measureValueList.stream()
                .mapToDouble(measureValue -> {
                    switch (keyResult.getExpectedEvolution()) {
                    case MAX -> {
                        return measureValue != null && measureValue.getValue() <= targetValue ? 100D : 0D;
                    }
                    case MIN -> {
                        return measureValue != null && measureValue.getValue() >= targetValue ? 100D : 0D;
                    }
                    default -> throw new IllegalArgumentException(
                            "This class does only calculate progress for min or max expected evolutions!");
                    }
                }).average().orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Progress calculation failed!")));
    }

    protected double calculateKeyResultProgressForNoMinMax(KeyResult keyResult) {

        if (keyResult.getExpectedEvolution().equals(ExpectedEvolution.MIN)
                || keyResult.getExpectedEvolution().equals(ExpectedEvolution.MAX)) {
            throw new IllegalArgumentException(
                    "This class doesn't calculate progress for min or max expected evolutions!");
        }

        Measure lastMeasure = this.measureRepository
                .findFirstMeasuresByKeyResultIdOrderByMeasureDateDesc(keyResult.getId());
        if (lastMeasure == null) {
            return 0D;
        }

        double basisValue = keyResult.getBasisValue();
        double targetValue = keyResult.getTargetValue();
        double progress;

        double value = lastMeasure.getValue() == null ? basisValue : lastMeasure.getValue();

        if (basisValue > targetValue) {
            progress = basisValue == value ? 0 : (100 / ((basisValue - targetValue) / (basisValue - value)));
        } else {
            progress = Math.ceil(basisValue == value ? 0 : (100 / ((targetValue - basisValue) / (value - basisValue))));
        }

        if (progress > 100) {
            return 100D;
        }
        if (progress < 0) {
            return 0D;
        }
        return progress;
    }
}
