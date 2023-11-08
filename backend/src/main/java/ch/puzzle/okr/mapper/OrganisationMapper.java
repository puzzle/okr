package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.OrganisationDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Organisation;
import ch.puzzle.okr.models.Team;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class OrganisationMapper {
    private final TeamMapper teamMapper;

    public OrganisationMapper(TeamMapper teamMapper) {
        this.teamMapper = teamMapper;
    }

    public OrganisationDto toDto(Organisation organisation) {
        List<TeamDto> teamDTOs = organisation.getTeams().stream().map(team -> this.teamMapper.toDto(team, null))
                .toList();
        return new OrganisationDto(organisation.getId(), organisation.getVersion(), organisation.getOrgName(), teamDTOs,
                organisation.getState());
    }

    public Organisation toOrganisation(OrganisationDto organisationDto) {
        List<Team> teams = organisationDto.teams().stream().map(this.teamMapper::toTeam).toList();
        return Organisation.Builder.builder().withId(organisationDto.id()).withVersion(organisationDto.version())
                .withOrgName(organisationDto.orgName()).withTeams(teams).withState(organisationDto.state()).build();
    }
}
