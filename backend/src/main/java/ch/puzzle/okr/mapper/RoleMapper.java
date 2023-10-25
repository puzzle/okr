package ch.puzzle.okr.mapper;

import ch.puzzle.okr.models.authorization.AuthorizationRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

import static ch.puzzle.okr.models.authorization.AuthorizationRole.*;

@Component
public class RoleMapper {
    @Value("${okr.organisation.name.1stLevel}")
    private String firstLevelOrganisationName;
    @Value("${okr.organisation.name.2ndLevel}")
    private String secondLevelOrganisationName;

    public List<AuthorizationRole> mapOrganisationNames(List<String> organisationNames) {
        List<AuthorizationRole> roles = new ArrayList<>();
        if (hasFirstLevelOrganisationName(organisationNames)) {
            roles.addAll(List.of(READ_ALL_DRAFT, WRITE_ALL));
        } else if (hasSecondLevelOrganisationName(organisationNames)) {
            roles.addAll(List.of(READ_TEAMS_DRAFT, WRITE_ALL_TEAMS));
        } else {
            roles.addAll(List.of(READ_TEAM_DRAFT, WRITE_TEAM));
        }
        roles.add(READ_ALL_PUBLISHED);
        return roles;
    }

    private boolean hasFirstLevelOrganisationName(List<String> organisationNames) {
        return organisationNames.contains(firstLevelOrganisationName);
    }

    private boolean hasSecondLevelOrganisationName(List<String> organisationNames) {
        return organisationNames.contains(secondLevelOrganisationName);
    }
}
