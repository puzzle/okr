package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.QuarterService;
import ch.puzzle.okr.service.TeamService;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class TeamMapper {

    private final TeamService teamService;
    private final QuarterService quarterService;

    public TeamMapper(TeamService teamService, QuarterService quarterService) {
        this.teamService = teamService;
        this.quarterService = quarterService;
    }

    public TeamDto toDto(Team team, Long quarterId) {
        long chosenQuarterId = quarterId == null ? quarterService.getActiveQuarter().getId() : quarterId;
        Integer activeObjectives = teamService.activeObjectivesAmountOfTeam(team, chosenQuarterId);
        return new TeamDto(team.getId(), team.getName(), activeObjectives);
    }

    @Deprecated
    public TeamDto toDto(Team team) {
        return new TeamDto(team.getId(), team.getName());
    }

    public Team toTeam(TeamDto teamDto) {
        return Team.Builder.builder().withId(teamDto.id()).withName(teamDto.name()).build();
    }
}
