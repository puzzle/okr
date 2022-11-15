package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProgressService {
    private final KeyResultRepository keyResultRepository;
    private final MeasureRepository measureRepository;

    public ProgressService(KeyResultRepository keyResultRepository, MeasureRepository measureRepository) {
        this.keyResultRepository = keyResultRepository;
        this.measureRepository = measureRepository;
    }

    public Long updateObjectiveProgressValue(Long objectiveId) {
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
        Double percentValue = keyResultsProgress.keySet().stream()
                .map(keyResult -> keyResultsProgress.get(keyResult).doubleValue()
                        / keyResult.getTargetValue().doubleValue())
                .mapToDouble(values -> values * 100).average().orElse(0);
        return percentValue.longValue();
    }
}
