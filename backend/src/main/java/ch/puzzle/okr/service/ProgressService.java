package ch.puzzle.okr.service;

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
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ProgressService {
    private final KeyResultRepository keyResultRepository;
    private final MeasureRepository measureRepository;
    private final ObjectiveRepository objectiveRepository;

    public ProgressService(KeyResultRepository keyResultRepository, MeasureRepository measureRepository,
                           ObjectiveRepository objectiveRepository) {
        this.keyResultRepository = keyResultRepository;
        this.measureRepository = measureRepository;
        this.objectiveRepository = objectiveRepository;
    }

    public void updateObjectiveProgress(Long objectiveId) {
        Objective objective = this.objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        Double progress = this.getObjectiveProgressInPercent(objectiveId);
        objective.setProgress(progress);
        this.objectiveRepository.save(objective);
    }

    public Double getObjectiveProgressInPercent(Long objectiveId) {
        List<KeyResult> keyResultList = (List<KeyResult>) this.keyResultRepository.findAll();
        List<Measure> measureList = (List<Measure>) this.measureRepository.findAll();

        //Map KeyResults to their Progress
        Map<KeyResult, Integer> keyResultsProgress = keyResultList
                .stream()
                .filter(keyResult -> keyResult.getObjective().getId().equals(objectiveId))
                .collect(
                        Collectors.toMap(
                                keyResult -> keyResult,
                                keyResult -> measureList
                                        .stream()
                                        .filter(measure -> measure.getKeyResult().getId().equals(keyResult.getId()))
                                        .map(Measure::getValue)
                                        .reduce((first, second) -> second)
                                        .orElse(0))
                        );
        /*
        Calculate the progress for each keyResult in percent and return the average of
        the progress of all keyResults in percentage
         */
        return keyResultsProgress.keySet().stream()
                .map(keyResult -> keyResultsProgress.get(keyResult).doubleValue()
                        / keyResult.getTargetValue().doubleValue())
                .mapToDouble(values -> values * 100).average().orElse(0);
    }
}
