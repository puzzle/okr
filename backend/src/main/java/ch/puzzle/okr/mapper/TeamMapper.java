package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Team;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {
    public TeamDto toDto(Team team) {
        return new TeamDto(team.getId(), team.getName());
    }

    public Team toTeam(TeamDto teamDto) {
        return Team.Builder.builder().withId(teamDto.id()).withName(teamDto.name()).build();
    }
}
