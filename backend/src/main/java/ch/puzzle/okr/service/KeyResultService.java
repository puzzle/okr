package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class KeyResultService {
    KeyResultRepository keyResultRepository;
    ObjectiveRepository objectiveRepository;

    public KeyResultService(KeyResultRepository keyResultRepository, ObjectiveRepository objectiveRepository) {
        this.keyResultRepository = keyResultRepository;
        this.objectiveRepository = objectiveRepository;
    }

    public List<KeyResult> getAllKeyResultsByObjective(long objectiveId) {
        Objective objective = objectiveRepository.findById(objectiveId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Objective with id %d not found", objectiveId))
        );
        return keyResultRepository.findByObjective(objective);
    }
}
