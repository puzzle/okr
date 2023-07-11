package ch.puzzle.okr.dto.goal;

import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.Unit;

public record GoalDto(GoalObjectiveDto objective, GoalKeyResultDto keyresult, Long teamId, String teamName,
        Long progress, String quarterLabel, ExpectedEvolution expectedEvolution, Unit unit, Double basicValue,
        Double targetValue, Double value) {

    public GoalDto(GoalObjectiveDto objective, GoalKeyResultDto keyresult, Team team, Long progress,
            String quarterLabel, ExpectedEvolution expectedEvolution, Unit unit, Double basicValue, Double targetValue,
            Double value) {
        this(objective, keyresult, team.getId(), team.getName(), progress, quarterLabel, expectedEvolution, unit,
                basicValue, targetValue, value);
    }
}
