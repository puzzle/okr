package ch.puzzle.okr.dto;

import java.util.List;

public class OverviewDto {
    private TeamDto teamDto;
    private List<ObjectiveDto> objectives;

    public OverviewDto(TeamDto teamDto, List<ObjectiveDto> objectives) {
        this.teamDto = teamDto;
        this.objectives = objectives;
    }

    public TeamDto getTeamDto() {
        return teamDto;
    }

    public void setTeamDto(TeamDto teamDto) {
        this.teamDto = teamDto;
    }

    public List<ObjectiveDto> getObjectives() {
        return objectives;
    }

    public void setObjectives(List<ObjectiveDto> objectives) {
        this.objectives = objectives;
    }
}
