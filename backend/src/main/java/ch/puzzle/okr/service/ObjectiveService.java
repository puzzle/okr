package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.MeasureRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final TeamRepository teamRepository;
    private final KeyResultService keyResultService;
    private final MeasureRepository measureRepository;

    public ObjectiveService(ObjectiveRepository objectiveRepository, KeyResultRepository keyResultRepository,
            TeamRepository teamRepository, @Lazy KeyResultService keyResultService,
            MeasureRepository measureRepository) {
        this.objectiveRepository = objectiveRepository;
        this.keyResultRepository = keyResultRepository;
        this.teamRepository = teamRepository;
        this.keyResultService = keyResultService;
        this.measureRepository = measureRepository;
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
            return objectiveRepository.findByTeamIdOrderByTitleAsc(id);
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

    public boolean quarterIsImmutable(Objective objective) {
        boolean quarterHasChanged = !this.getObjective(objective.getId()).getQuarter().getId()
                .equals(objective.getQuarter().getId());

        boolean hasMeasures = keyResultRepository.findByObjectiveId(objective.getId()).stream()
                .anyMatch(keyResult -> measureRepository.findLastMeasuresOfKeyresults(keyResult.getId()) != null);

        return quarterHasChanged && hasMeasures;
    }

    public Objective updateObjective(Objective objective) {
        Objective existingObjective = this.getObjective(objective.getId());
        objective.setProgress(existingObjective.getProgress());
        if (quarterIsImmutable(objective)) {
            objective.setQuarter(existingObjective.getQuarter());
            LocalDateTime modifiedOn = objective.getModifiedOn();
            objective.setModifiedOn(existingObjective.getModifiedOn());
            if (!existingObjective.equals(objective)) {
                objective.setModifiedOn(modifiedOn);
            }
        }
        this.checkObjective(objective);
        return this.objectiveRepository.save(objective);
    }

    private void checkObjective(Objective objective) {
        if (objective.getTitle() == null || objective.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Missing attribute title when creating objective");
        } else if (objective.getModifiedOn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to generate attribute createdOn when creating objective");
        }
    }

    public List<Objective> getObjectiveByTeamIdAndQuarterId(Long teamId, Long quarterId) {
        return quarterId == null ? objectiveRepository.findByTeamIdOrderByTitleAsc(teamId)
                : objectiveRepository.findByQuarterIdAndTeamIdOrderByModifiedOnDesc(quarterId, teamId);
    }

    @Transactional
    public void deleteObjectiveById(Long id) {
        List<KeyResult> keyResults = this.keyResultRepository
                .findByObjectiveOrderByModifiedOnDesc(this.getObjective(id));
        for (KeyResult keyResult : keyResults) {
            this.keyResultService.deleteKeyResultById(keyResult.getId());
        }
        this.objectiveRepository.deleteById(id);
    }
}
