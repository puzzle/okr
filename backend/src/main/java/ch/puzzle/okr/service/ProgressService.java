package ch.puzzle.okr.service;

import ch.puzzle.okr.helper.KeyResultMeasureValue;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
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

    public ProgressService(ObjectiveService objectiveService, ObjectiveRepository objectiveRepository,
            KeyResultRepository keyResultRepository) {
        this.objectiveService = objectiveService;
        this.objectiveRepository = objectiveRepository;
        this.keyResultRepository = keyResultRepository;
    }

    public void updateObjectiveProgress(Long objectiveId) {
        Objective objective = this.objectiveService.getObjective(objectiveId);
        Long progress = this
                .calculateObjectiveProgress(this.objectiveRepository.getCalculationValuesForProgress(objectiveId));
        if (progress == null && !this.keyResultRepository.findByObjective(objective).isEmpty()) {
            progress = 0L;
        }
        objective.setProgress(progress);
        this.objectiveRepository.save(objective);
    }

    public Long updateKeyResultProgress(Long keyResultId) {
        KeyResultMeasureValue keyResultMeasureValue = this.keyResultRepository.getProgressValuesKeyResult(keyResultId);
        if (keyResultMeasureValue != null) {
            return (long) Math.floor(returnCheckedProgress(keyResultMeasureValue));
        }
        return null;
    }

    public Long calculateObjectiveProgress(List<KeyResultMeasureValue> keyResultMeasureValues) {
        if (keyResultMeasureValues.isEmpty()) {
            return null;
        }
        return (long) Math.floor(keyResultMeasureValues.stream().mapToDouble(this::returnCheckedProgress).average()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "Progress calculation failed!")));
    }

    public double returnCheckedProgress(KeyResultMeasureValue keyResultMeasureValue) {
        double percentValue = this.calculateKeyResultProgress(keyResultMeasureValue);
        if (percentValue > 100) {
            return 100;
        } else if (percentValue < 0) {
            return 0;
        }
        return percentValue;
    }

    protected double calculateKeyResultProgress(KeyResultMeasureValue keyResultMeasureValue) {
        if (keyResultMeasureValue.getBasisValue() > keyResultMeasureValue.getTargetValue()) {
            return turnedCalculation(keyResultMeasureValue.getTargetValue().doubleValue(),
                    keyResultMeasureValue.getBasisValue().doubleValue(),
                    keyResultMeasureValue.getValue().doubleValue());
        }
        return calculate(keyResultMeasureValue.getTargetValue().doubleValue(),
                keyResultMeasureValue.getBasisValue().doubleValue(), keyResultMeasureValue.getValue().doubleValue());
    }

    public double turnedCalculation(double targetValue, double basisValue, double value) {
        return basisValue == value ? 0 : (100 / ((basisValue - targetValue) / (basisValue - value)));
    }

    protected double calculate(double targetValue, double basisValue, double value) {
        return basisValue == value ? 0 : (100 / ((targetValue - basisValue) / (value - basisValue)));
    }
}
