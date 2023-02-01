package ch.puzzle.okr.dto.goal;

import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.Unit;

public class GoalDto {
    private GoalObjectiveDto objective;
    private GoalKeyResultDto keyresult;
    private Long teamId;
    private String teamName;
    private Long progress;
    private String quarterLabel;
    private ExpectedEvolution expectedEvolution;
    private Unit unit;
    private Double basicValue;
    private Double targetValue;

    public GoalDto(GoalObjectiveDto objective, GoalKeyResultDto keyresult, Team team, Long progress,
            String quarterLabel, ExpectedEvolution expectedEvolution, Unit unit, Double basicValue,
            Double targetValue) {
        this.objective = objective;
        this.keyresult = keyresult;
        this.teamId = team.getId();
        this.teamName = team.getName();
        this.progress = progress;
        this.quarterLabel = quarterLabel;
        this.expectedEvolution = expectedEvolution;
        this.unit = unit;
        this.basicValue = basicValue;
        this.targetValue = targetValue;
    }

    public GoalObjectiveDto getObjective() {
        return objective;
    }

    public GoalKeyResultDto getKeyresult() {
        return keyresult;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public Long getProgress() {
        return progress;
    }

    public String getQuarterLabel() {
        return quarterLabel;
    }

    public ExpectedEvolution getExpectedEvolution() {
        return expectedEvolution;
    }

    public Unit getUnit() {
        return unit;
    }

    public Double getBasicValue() {
        return basicValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }
}
