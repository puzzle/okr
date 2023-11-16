package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.OrganisationDto;
import ch.puzzle.okr.models.Organisation;
import org.springframework.stereotype.Component;

@Component
public class OrganisationMapper {

    public OrganisationDto toDto(Organisation organisation) {
        return new OrganisationDto(organisation.getId(), organisation.getVersion(), organisation.getOrgName(),
                organisation.getState());
    }

    public Organisation toOrganisation(OrganisationDto organisationDto) {
        return Organisation.Builder.builder().withId(organisationDto.id()).withVersion(organisationDto.version())
                .withOrgName(organisationDto.orgName()).withState(organisationDto.state()).build();
    }
}
