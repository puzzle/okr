package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.goal.GoalDto;
import ch.puzzle.okr.dto.goal.GoalKeyResultDto;
import ch.puzzle.okr.dto.goal.GoalObjectiveDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.business.MeasureBusinessService;
import ch.puzzle.okr.service.business.ProgressBusinessService;
import org.springframework.stereotype.Component;

@Component
public class GoalMapper {

    private final ProgressBusinessService progressBusinessService;

    private final MeasureBusinessService measureBusinessService;

    public GoalMapper(ProgressBusinessService progressBusinessService, MeasureBusinessService measureBusinessService) {
        this.progressBusinessService = progressBusinessService;
        this.measureBusinessService = measureBusinessService;

    }

    public GoalDto toDto(KeyResult keyResult) {
        Objective objective = keyResult.getObjective();
        return new GoalDto(new GoalObjectiveDto(objective.getId(), objective.getTitle(), objective.getDescription()),
                new GoalKeyResultDto(keyResult.getId(), keyResult.getTitle(), keyResult.getDescription(),
                        keyResult.getOwner()),
                objective.getTeam(), progressBusinessService.calculateKeyResultProgress(keyResult),
                keyResult.getObjective().getQuarter().getLabel(), keyResult.getExpectedEvolution(), keyResult.getUnit(),
                keyResult.getBasisValue(), keyResult.getTargetValue(),
                measureBusinessService.getCurrentValue(keyResult));
    }
}
