package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

@Service
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultRepository keyResultRepository;
    private final KeyResultService keyResultService;

    public ObjectiveService(ObjectiveRepository objectiveRepository, KeyResultRepository keyResultRepository,
            @Lazy KeyResultService keyResultService) {
        this.objectiveRepository = objectiveRepository;
        this.keyResultRepository = keyResultRepository;
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
        return objectiveRepository.findByTeamIdOrderByTitleAsc(id);
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
        return objectiveRepository.save(objective);
    }

    public boolean isQuarterImmutable(Objective objective) {
        boolean quarterHasChanged = !Objects.equals(getObjective(objective.getId()).getQuarter().getId(),
                objective.getQuarter().getId());

        boolean hasMeasures = !keyResultService.getLastMeasures(objective.getId()).equals(Collections.emptyList());

        return quarterHasChanged && hasMeasures;
    }

    public Objective updateObjective(Objective objective) {
        Objective existingObjective = getObjective(objective.getId());
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
        return objectiveRepository.save(objective);
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
        return quarterId == null ? objectiveRepository.findByTeamIdOrderByTitleAsc(teamId)
                : objectiveRepository.findByQuarterIdAndTeamIdOrderByModifiedOnDesc(quarterId, teamId);
    }

    @Transactional
    public void deleteObjectiveById(Long id) {
        List<KeyResult> keyResults = keyResultRepository.findByObjectiveOrderByModifiedOnDesc(getObjective(id));
        for (KeyResult keyResult : keyResults) {
            keyResultService.deleteKeyResultById(keyResult.getId());
        }
        objectiveRepository.deleteById(id);
    }

    public Integer activeObjectivesAmountOfTeam(Team team, Quarter quarter) {
        // validate quarter in objective validator by using the quarter validator
        return objectiveRepository.countByTeamAndQuarter(team, quarter);
    }
}
