package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ObjectivePersistenceService {
    private final ObjectiveRepository objectiveRepository;

    public ObjectivePersistenceService(ObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    public List<Objective> getAllObjectives() {
        return (List<Objective>) objectiveRepository.findAll();
    }

    public Objective getObjectiveById(Long objectiveId) {
        if (objectiveId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute objective id");
        }
        return objectiveRepository.findById(objectiveId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        String.format("Objective with id %d not found", objectiveId)));
    }

    public List<Objective> getObjectivesByTeam(Long id) {
        return objectiveRepository.findByTeamIdOrderByTitleAsc(id);
    }

    public List<Objective> getObjectivesByTeamIdOrderByTitleAsc(Long teamId) {
        return objectiveRepository.findByTeamIdOrderByTitleAsc(teamId);
    }

    public Objective saveObjective(Objective objective) {
        return objectiveRepository.save(objective);
    }

    public Objective updateObjective(Objective objective) {
        return objectiveRepository.save(objective);
    }

    public List<Objective> getObjectiveByTeamIdAndQuarterId(Long teamId, Long quarterId) {
        return quarterId == null ? objectiveRepository.findByTeamIdOrderByTitleAsc(teamId)
                : objectiveRepository.findByQuarterIdAndTeamIdOrderByModifiedOnDesc(quarterId, teamId);
    }

    public void deleteObjectiveById(Long id) {
        objectiveRepository.deleteById(id);
    }
}
