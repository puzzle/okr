package ch.puzzle.okr.service;

import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class KeyResultService {

    private final KeyResultRepository keyResultRepository;
    private final QuarterRepository quarterRepository;
    private final UserRepository userRepository;
    private final ObjectiveRepository objectiveRepository;
    private final MeasureRepository measureRepository;

    private final ProgressService progressService;

    public KeyResultService(KeyResultRepository keyResultRepository, QuarterRepository quarterRepository,
                            UserRepository userRepository, ObjectiveRepository objectiveRepository,
                            MeasureRepository measureRepository, ProgressService progressService) {
        this.keyResultRepository = keyResultRepository;
        this.quarterRepository = quarterRepository;
        this.userRepository = userRepository;
        this.objectiveRepository = objectiveRepository;
        this.measureRepository = measureRepository;
        this.progressService = progressService;
    }

    public List<KeyResult> getAllKeyResults() {
        return (List<KeyResult>) this.keyResultRepository.findAll();
    }

    public KeyResult createKeyResult(KeyResult keyResult) {
        KeyResult createdKeyResult = this.keyResultRepository.save(keyResult);
        this.progressService.updateObjectiveProgress(createdKeyResult.getObjective().getId());
        return createdKeyResult;
    }

    public KeyResult getKeyResultById(long id) {
        return this.keyResultRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Keyresult with id %d not found", id))
        );
    }

    public KeyResult updateKeyResult(KeyResult keyResult) {
        if (keyResultRepository.findById(keyResult.getId()).isPresent()) {
            KeyResult createdKeyResult = this.keyResultRepository.save(keyResult);
            this.progressService.updateObjectiveProgress(createdKeyResult.getObjective().getId());
            return createdKeyResult;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find keyresult with id %d", keyResult.getId()));
        }
    }

    public Quarter getQuarterById(long id) {
        return this.quarterRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find quarter with id %d", id)));
    }

    public User getOwnerById(long id) {
        return this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Owner with id %d not found", id))
        );
    }

    public Objective getObjectivebyId(long id) {
        return this.objectiveRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Objective with id %d not found", id))
        );
    }

    public List<Measure> getAllMeasuresByKeyResult(long keyResultId) {
        KeyResult keyResult = keyResultRepository.findById(keyResultId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("KeyResult with id %d not found", keyResultId))
        );
        return measureRepository.findByKeyResult(keyResult);
    }
}
