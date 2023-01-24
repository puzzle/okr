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

    public void setObjective(GoalObjectiveDto objective) {
        this.objective = objective;
    }

    public GoalKeyResultDto getKeyresult() {
        return keyresult;
    }

    public void setKeyresult(GoalKeyResultDto keyresult) {
        this.keyresult = keyresult;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Long getProgress() {
        return progress;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }

    public String getQuarterLabel() {
        return quarterLabel;
    }

    public void setQuarterLabel(String quarterLabel) {
        this.quarterLabel = quarterLabel;
    }

    public ExpectedEvolution getExpectedEvolution() {
        return expectedEvolution;
    }

    public void setExpectedEvolution(ExpectedEvolution expectedEvolution) {
        this.expectedEvolution = expectedEvolution;
    }

    public Unit getUnit() {
        return unit;
    }

    public void setUnit(Unit unit) {
        this.unit = unit;
    }

    public Double getBasicValue() {
        return basicValue;
    }

    public void setBasicValue(Double basicValue) {
        this.basicValue = basicValue;
    }

    public Double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(Double targetValue) {
        this.targetValue = targetValue;
    }
}
