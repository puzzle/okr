package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final TeamRepository teamRepository;

    public ObjectiveService(ObjectiveRepository objectiveRepository, KeyResultRepository keyResultRepository,
            TeamRepository teamRepository) {
        this.objectiveRepository = objectiveRepository;
        this.keyResultRepository = keyResultRepository;
        this.teamRepository = teamRepository;
    }

    public List<Objective> getAllObjectives() {
        return (List<Objective>) objectiveRepository.findAll();
    }

    public Objective getObjective(Long id) {
        return objectiveRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Objective with id %d not found", id)));
    }

    public List<Objective> getObjectivesByTeam(Long id) {
        if (teamRepository.findById(id).isPresent()) {
            return objectiveRepository.findByTeamId(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    String.format("Could not find team with id %d", id));
        }
    }

    public Objective saveObjective(Objective objective) {
        if (objective.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id");
        }
        this.checkObjective(objective);
        return objectiveRepository.save(objective);
    }

    public Objective updateObjective(Long id, Objective objective) {
        if (objective.getProgress() != null) {
            List<KeyResult> keyResultList = (List<KeyResult>) this.keyResultRepository.findAll();
            if (keyResultList.stream()
                    .anyMatch(keyResult -> keyResult.getObjective().getId().equals(objective.getId()))) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Can't set the progress of an objective if you have already defined keyresults!");
            }
        }
        this.getObjective(id);
        this.checkObjective(objective);
        return this.objectiveRepository.save(objective);
    }

    private void checkObjective(Objective objective) {
        if (objective.getTitle() == null || objective.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Missing attribute title when creating objective");
        } else if (objective.getDescription() == null || objective.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Missing attribute description when creating objective");
        } else if (objective.getCreatedOn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to generate attribute createdOn when creating objective");
        }
    }
}
