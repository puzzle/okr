package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class TeamMapper {

    private final TeamBusinessService teamBusinessService;
    private final QuarterBusinessService quarterBusinessService;

    public TeamMapper(TeamBusinessService teamBusinessService, QuarterBusinessService quarterBusinessService) {
        this.teamBusinessService = teamBusinessService;
        this.quarterBusinessService = quarterBusinessService;
    }

    public TeamDto toDto(Team team, Long quarterId) {
        long chosenQuarterId = quarterId == null ? quarterBusinessService.getActiveQuarter(LocalDate.now()).getId()
                : quarterId;
        Integer activeObjectives = teamBusinessService.activeObjectivesAmountOfTeam(team, chosenQuarterId);
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
