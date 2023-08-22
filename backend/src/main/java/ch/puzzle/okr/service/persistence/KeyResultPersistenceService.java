package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class KeyResultPersistenceService {

    private final KeyResultRepository keyResultRepository;

    public KeyResultPersistenceService(KeyResultRepository keyResultRepository) {
        this.keyResultRepository = keyResultRepository;
    }

    public KeyResult saveKeyResult(KeyResult keyResult) {
        return keyResultRepository.save(keyResult);
    }

    public KeyResult getKeyResultById(Long id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute key result id");
        }
        return keyResultRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Key result with id %d not found", id)));
    }

    public KeyResult updateKeyResult(KeyResult keyResult) {
        if (keyResultRepository.findById(keyResult.getId()).isPresent()) {
            return keyResultRepository.save(keyResult);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find key result with id %d", keyResult.getId()));
        }
    }

    public List<KeyResult> getKeyResultsByObjective(Objective objective) {
        return keyResultRepository.findByObjectiveOrderByModifiedOnDesc(objective);
    }

    public void deleteKeyResultById(Long id) {
        keyResultRepository.deleteById(id);
    }
}
