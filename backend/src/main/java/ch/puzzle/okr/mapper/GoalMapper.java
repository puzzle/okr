package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.goal.GoalDto;
import ch.puzzle.okr.dto.goal.GoalKeyResultDto;
import ch.puzzle.okr.dto.goal.GoalObjectiveDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.GoalService;
import ch.puzzle.okr.service.ProgressService;
import org.springframework.stereotype.Component;

@Component
public class GoalMapper {

    private final ProgressService progressService;

    private final GoalService goalService;

    public GoalMapper(ProgressService progressService, GoalService goalService) {
        this.progressService = progressService;
        this.goalService = goalService;

    }

    public GoalDto toDto(KeyResult keyResult) {
        Objective objective = keyResult.getObjective();
        return new GoalDto(new GoalObjectiveDto(objective.getId(), objective.getTitle(), objective.getDescription()),
                new GoalKeyResultDto(keyResult.getId(), keyResult.getTitle(), keyResult.getDescription(),
                        keyResult.getOwner()),
                objective.getTeam(), progressService.calculateKeyResultProgress(keyResult),
                keyResult.getObjective().getQuarter().getLabel(), keyResult.getExpectedEvolution(), keyResult.getUnit(),
                keyResult.getBasisValue(), keyResult.getTargetValue(), goalService.getCurrentValue(keyResult));
    }
}
