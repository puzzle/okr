package ch.puzzle.okr.dto;

import java.util.List;

public class OverviewDto {
    private TeamDto team;
    private List<ObjectiveDto> objectives;

    public OverviewDto(TeamDto team, List<ObjectiveDto> objectives) {
        this.team = team;
        this.objectives = objectives;
    }

    public TeamDto getTeam() {
        return team;
    }

    public void setTeam(TeamDto team) {
        this.team = team;
    }

    public List<ObjectiveDto> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<ObjectiveDto> objectives) {
        this.objectives = objectives;
    }
}
