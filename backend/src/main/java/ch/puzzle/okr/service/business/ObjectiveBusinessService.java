package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ObjectiveBusinessService {
    private final ObjectivePersistenceService objectivePersistenceService;
    private final KeyResultPersistenceService keyResultPersistenceService;
    private final TeamPersistenceService teamPersistenceService;
    private final KeyResultBusinessService keyResultBusinessService;
    private final ProgressBusinessService progressBusinessService;

    public ObjectiveBusinessService(ObjectivePersistenceService objectivePersistenceService,
            KeyResultPersistenceService keyResultPersistenceService, TeamPersistenceService teamPersistenceService,
            KeyResultBusinessService keyResultBusinessService, ProgressBusinessService progressBusinessService) {
        this.objectivePersistenceService = objectivePersistenceService;
        this.keyResultPersistenceService = keyResultPersistenceService;
        this.teamPersistenceService = teamPersistenceService;
        this.keyResultBusinessService = keyResultBusinessService;
        this.progressBusinessService = progressBusinessService;
    }

    public List<Objective> getAllObjectives() {
        return objectivePersistenceService.getAllObjectives();
    }

    public Objective getObjectiveById(Long id) {
        return objectivePersistenceService.getObjectiveById(id);
    }

    public List<Objective> getObjectivesByTeam(Long id) {
        teamPersistenceService.findById(id);
        return objectivePersistenceService.getObjectivesByTeam(id);
    }

    public List<Objective> getObjectivesByTeamIdOrderByTitleAsc(Long teamId) {
        return objectivePersistenceService.getObjectivesByTeamIdOrderByTitleAsc(teamId);
    }

    public Objective saveObjective(Objective objective) {
        if (objective.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id");
        }
        if (objective.getProgress() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give a progress");
        }
        objective.setProgress(null);
        checkObjective(objective);
        return objectivePersistenceService.saveObjective(objective);
    }

    public boolean isQuarterImmutable(Objective objective) {
        boolean quarterHasChanged = !Objects.equals(getObjectiveById(objective.getId()).getQuarter().getId(),
                objective.getQuarter().getId());

        boolean hasMeasures = !keyResultBusinessService.getLastMeasures(objective.getId()).isEmpty();

        return quarterHasChanged && hasMeasures;
    }

    public Objective updateObjective(Objective objective) {
        Objective existingObjective = getObjectiveById(objective.getId());
        objective.setProgress(existingObjective.getProgress());
        if (isQuarterImmutable(objective)) {
            objective.setQuarter(existingObjective.getQuarter());
            LocalDateTime modifiedOn = objective.getModifiedOn();
            objective.setModifiedOn(existingObjective.getModifiedOn());
            if (!objective.equals(existingObjective)) {
                objective.setModifiedOn(modifiedOn);
            }
            if (!existingObjective.equals(objective)) {
                objective.setModifiedOn(modifiedOn);
            }
        }
        checkObjective(objective);
        return objectivePersistenceService.updateObjective(objective);
    }

    private void checkObjective(Objective objective) {
        if (StringUtils.isBlank(objective.getTitle())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Missing attribute title when creating objective");
        } else if (objective.getModifiedOn() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Failed to generate attribute modifiedOn when creating objective");
        }
    }

    public List<Objective> getObjectiveByTeamIdAndQuarterId(Long teamId, Long quarterId) {
        return objectivePersistenceService.getObjectiveByTeamIdAndQuarterId(teamId, quarterId);
    }

    public void updateObjectiveProgress(Long objectiveId) {
        List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(objectiveId);
        if (keyResultList.isEmpty()) {
            Objective objective = objectivePersistenceService.getObjectiveById(objectiveId);
            objective.setProgress(null);
            objectivePersistenceService.saveObjective(objective);
        } else {
            double objectiveProgress = keyResultList.stream()
                    .mapToDouble(progressBusinessService::calculateKeyResultProgress).average()
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                            "Progress calculation failed!"));

            Objective objective = objectivePersistenceService.getObjectiveById(objectiveId);
            objective.setProgress((long) Math.floor(objectiveProgress));
            objectivePersistenceService.saveObjective(objective);
        }
    }

    @Transactional
    public void deleteObjectiveById(Long id) {
        List<KeyResult> keyResults = keyResultPersistenceService.getKeyResultsByObjective(getObjectiveById(id));
        for (KeyResult keyResult : keyResults) {
            keyResultBusinessService.deleteKeyResultById(keyResult.getId());
        }
        objectivePersistenceService.deleteObjectiveById(id);
    }

    public Integer activeObjectivesAmountOfTeam(Team team, Quarter quarter) {
        // validate quarter in objective validator by using the quarter validator
        return objectivePersistenceService.countByTeamAndQuarter(team, quarter);
    }
}
