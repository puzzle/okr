package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.mapper.KeyResultMeasureMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;

@Service
public class KeyResultService {

    private final KeyResultRepository keyResultRepository;
    private final QuarterRepository quarterRepository;
    private final UserRepository userRepository;
    private final ObjectiveRepository objectiveRepository;
    private final MeasureRepository measureRepository;
    private final KeyResultMeasureMapper keyResultMeasureMapper;
    private final ProgressService progressService;

    public KeyResultService(KeyResultRepository keyResultRepository, QuarterRepository quarterRepository,
            UserRepository userRepository, ObjectiveRepository objectiveRepository, MeasureRepository measureRepository,
            KeyResultMeasureMapper keyResultMeasureMapper, ProgressService progressService) {
        this.keyResultRepository = keyResultRepository;
        this.quarterRepository = quarterRepository;
        this.userRepository = userRepository;
        this.objectiveRepository = objectiveRepository;
        this.measureRepository = measureRepository;
        this.keyResultMeasureMapper = keyResultMeasureMapper;
        this.progressService = progressService;
    }

    public List<KeyResult> getAllKeyResults() {
        return (List<KeyResult>) this.keyResultRepository.findAll();
    }

    public KeyResult createKeyResult(KeyResult keyResult) {
        return this.keyResultRepository.save(keyResult);
    }

    public KeyResult getKeyResultById(long id) {
        return this.keyResultRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Keyresult with id %d not found", id)));
    }

    public KeyResult updateKeyResult(KeyResult keyResult) {
        if (keyResultRepository.findById(keyResult.getId()).isPresent()) {
            return this.keyResultRepository.save(keyResult);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find keyresult with id %d", keyResult.getId()));
        }
    }

    public User getOwnerById(long id) {
        return this.userRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Owner with id %d not found", id)));
    }

    public Objective getObjectivebyId(long id) {
        return this.objectiveRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Objective with id %d not found", id)));
    }

    public List<Measure> getAllMeasuresByKeyResult(long keyResultId) {
        KeyResult keyResult = keyResultRepository.findById(keyResultId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("KeyResult with id %d not found", keyResultId)));
        return measureRepository.findByKeyResult(keyResult);
    }

    public List<Measure> getLastMeasures(Long objectiveId) {
        return measureRepository.findLastMeasuresOfKeyresults(objectiveId);
    }

    public List<KeyResult> getAllKeyResultsByObjective(long objectiveId) {
        Objective objective = objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Objective with id %d not found", objectiveId)));
        return keyResultRepository.findByObjective(objective);
    }

    public List<KeyResultMeasureDto> getAllKeyResultsByObjectiveWithMeasure(Long id) {
        List<Measure> measureList = getLastMeasures(id);
        return getAllKeyResultsByObjective(id).stream()
                .map(i -> keyResultMeasureMapper.toDto(i, measureList.stream()
                        .filter(j -> Objects.equals(j.getKeyResult().getId(), i.getId())).findFirst().orElse(null)))
                .toList();
    }

    public void deleteKeyResultById(Long id) {
        List<Measure> measures = getAllMeasuresByKeyResult(id);
        Long objectiveId = getKeyResultById(id).getObjective().getId();
        for (Measure measure : measures) {
            measureRepository.deleteById(measure.getId());
        }
        keyResultRepository.deleteById(id);
        progressService.updateObjectiveProgress(objectiveId);
    }
}
