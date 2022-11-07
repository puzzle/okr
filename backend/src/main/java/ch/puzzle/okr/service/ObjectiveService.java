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
public class ObjectiveService {
    ObjectiveRepository objectiveRepository;
    KeyResultRepository keyResultRepository;

    public ObjectiveService(ObjectiveRepository objectiveRepository, KeyResultRepository keyResultRepository) {
        this.objectiveRepository = objectiveRepository;
        this.keyResultRepository = keyResultRepository;
    }

    public List<Objective> getAllObjectives() {
        return (List<Objective>) objectiveRepository.findAll();
    }

    public Objective getObjective(Long id) {
        return objectiveRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Objective with id %d not found", id)));
    }

    public List<KeyResult> getAllKeyResultsByObjective(long objectiveId) {
        Objective objective = objectiveRepository.findById(objectiveId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Objective with id %d not found", objectiveId))
        );
        return keyResultRepository.findByObjective(objective);
    }

    public Objective saveObjective(Objective objective) {
        if(this.checkIfFalseObjective(objective)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed objective -> Attribut is invalid");
        }
        return objectiveRepository.save(objective);
    }

    public Objective updateObjective(Long id, Objective objective) {
        objective.setId(id);
        if(this.checkIfFalseObjective(objective)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed objective -> Attribut is invalid");
        }
        this.getObjective(id);
        return objectiveRepository.save(objective);
    }

    protected boolean checkIfFalseObjective(Objective objective) {
        return (objective.getTitle() == null || objective.getTitle().isBlank()) ||
                (objective.getDescription() == null || objective.getDescription().isBlank()) ||
                (objective.getProgress() == null) ||
                (objective.getCreatedOn() == null) ||
                (objective.getId() == null);
    }
}
