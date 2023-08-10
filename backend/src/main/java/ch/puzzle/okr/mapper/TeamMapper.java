package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.TeamService;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    private final TeamService teamService;

    public TeamMapper(TeamService teamService) {
        this.teamService = teamService;
    }

    public TeamDto toDto(Team team) {
        Integer activeObjectives = teamService.activeObjectivesAmountOfTeam(team);
        return new TeamDto(team.getId(), team.getName(), activeObjectives);
    }

    public Team toTeam(TeamDto teamDto) {
        return Team.Builder.builder().withId(teamDto.id()).withName(teamDto.name()).build();
    }
}
