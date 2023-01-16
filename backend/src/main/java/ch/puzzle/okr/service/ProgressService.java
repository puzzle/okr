package ch.puzzle.okr.service;

import ch.puzzle.okr.helper.KeyResultMeasureValue;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ProgressService {
    private final ObjectiveService objectiveService;
    private final ObjectiveRepository objectiveRepository;

    public ProgressService(ObjectiveService objectiveService, ObjectiveRepository objectiveRepository) {
        this.objectiveService = objectiveService;
        this.objectiveRepository = objectiveRepository;
    }

    public void updateObjectiveProgress(Long objectiveId) {
        Objective objective = this.objectiveService.getObjective(objectiveId);
        Long progress = this
                .calculateObjectiveProgress(this.objectiveRepository.getCalculationValuesForProgress(objectiveId));
        objective.setProgress(progress);
        this.objectiveRepository.save(objective);
    }

    public long calculateObjectiveProgress(List<KeyResultMeasureValue> keyResultMeasureValues) {
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

    private double calculateKeyResultProgress(KeyResultMeasureValue keyResultMeasureValue) {
        if (keyResultMeasureValue.getBasisValue() > keyResultMeasureValue.getTargetValue()) {
            return calculate(keyResultMeasureValue.getBasisValue().doubleValue(),
                    keyResultMeasureValue.getTargetValue().doubleValue(),
                    keyResultMeasureValue.getValue().doubleValue());
        }
        return calculate(keyResultMeasureValue.getTargetValue().doubleValue(),
                keyResultMeasureValue.getBasisValue().doubleValue(), keyResultMeasureValue.getValue().doubleValue());
    }

    private double calculate(double targetValue, double basisValue, double value) {
        return basisValue == value ? 0 : (100 / ((targetValue - basisValue) / (value - basisValue)));
    }
}
