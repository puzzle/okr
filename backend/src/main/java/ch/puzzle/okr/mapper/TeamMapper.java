package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.OrganisationDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamMapper {

    private final TeamBusinessService teamBusinessService;
    private final QuarterBusinessService quarterBusinessService;
    private final OrganisationMapper organisationMapper;

    public TeamMapper(TeamBusinessService teamBusinessService, QuarterBusinessService quarterBusinessService,
            OrganisationMapper organisationMapper) {
        this.teamBusinessService = teamBusinessService;
        this.quarterBusinessService = quarterBusinessService;
        this.organisationMapper = organisationMapper;
    }

    public TeamDto toDto(Team team, Long quarterId) {
        long chosenQuarterId = quarterId == null ? quarterBusinessService.getCurrentQuarter().getId() : quarterId;
        List<OrganisationDto> organisationDTOs = team.getAuthorizationOrganisation().stream()
                .map(organisationMapper::toDto).toList();
        return new TeamDto(team.getId(), team.getName(), organisationDTOs);
    }

    public Team toTeam(TeamDto teamDto) {
        List<Organisation> organisations = teamDto.organisations().stream().map(organisationMapper::toOrganisation)
                .toList();
        return Team.Builder.builder().withId(teamDto.id()).withName(teamDto.name())
                .withAuthorizationOrganisation(organisations).build();
    }
}
