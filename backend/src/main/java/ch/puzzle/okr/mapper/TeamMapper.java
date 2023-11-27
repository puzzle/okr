package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.OrganisationDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TeamMapper {

    private final OrganisationMapper organisationMapper;

    public TeamMapper(OrganisationMapper organisationMapper) {
        this.organisationMapper = organisationMapper;
    }

    public TeamDto toDto(Team team, List<Long> userTeamIds) {
        List<OrganisationDto> organisationDTOs = team.getAuthorizationOrganisation().stream()
                .map(organisationMapper::toDto).toList();
        boolean filterIsActive = userTeamIds.contains(team.getId());
        return new TeamDto(team.getId(), team.getVersion(), team.getName(), organisationDTOs, filterIsActive);
    }

    public Team toTeam(TeamDto teamDto) {
        List<Organisation> organisations = teamDto.organisations().stream().map(organisationMapper::toOrganisation)
                .toList();
        return Team.Builder.builder().withId(teamDto.id()).withVersion(teamDto.version()).withName(teamDto.name())
                .withAuthorizationOrganisation(organisations).build();
    }
}
