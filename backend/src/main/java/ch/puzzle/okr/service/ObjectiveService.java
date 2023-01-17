package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final TeamRepository teamRepository;
    private final KeyResultService keyResultService;

    public ObjectiveService(ObjectiveRepository objectiveRepository, KeyResultRepository keyResultRepository,
            TeamRepository teamRepository, @Lazy KeyResultService keyResultService) {
        this.objectiveRepository = objectiveRepository;
        this.keyResultRepository = keyResultRepository;
        this.teamRepository = teamRepository;
        this.keyResultService = keyResultService;
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
        if (objective.getProgress() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give a progress");
        }
        objective.setProgress(null);
        this.checkObjective(objective);
        return objectiveRepository.save(objective);
    }

    public Objective updateObjective(Objective objective) {
        Objective existingObjective = this.getObjective(objective.getId());
        objective.setQuarter(existingObjective.getQuarter());
        objective.setProgress(existingObjective.getProgress());
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

    public List<Objective> getObjectiveByTeamIdAndQuarterId(Long teamId, Long quarterId) {
        return quarterId == null ? objectiveRepository.findByTeamId(teamId)
                : objectiveRepository.findByQuarterIdAndTeamId(quarterId, teamId);
    }

    @Transactional
    public void deleteKeyObjectiveById(Long id) {
        List<KeyResult> keyResults = this.keyResultRepository.findByObjective(this.getObjective(id));
        for (KeyResult keyResult : keyResults) {
            this.keyResultService.deleteKeyResultById(keyResult.getId());
        }
        this.objectiveRepository.deleteById(id);
    }
}
