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
        if (objective.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id");
        }
        this.checkObjective(objective);
        return objectiveRepository.save(objective);
    }

    public Objective updateObjective(Long id, Objective objective) {
        this.getObjective(id);
        this.checkObjective(objective);
        return this.objectiveRepository.save(objective);
    }

    private void checkObjective(Objective objective) {
        if (objective.getTitle() == null || objective.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute title when creating objective");
        } else if (objective.getDescription() == null || objective.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute description when creating objective");
        } else if (objective.getProgress() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute progress when creating objective");
        } else if (objective.getCreatedOn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed to generate attribute createdOn when creating objective");
        }
    }
}
