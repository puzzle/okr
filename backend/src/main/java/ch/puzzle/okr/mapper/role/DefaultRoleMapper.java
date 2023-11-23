package ch.puzzle.okr.mapper.role;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static ch.puzzle.okr.models.authorization.AuthorizationRole.*;

@Component
public class DefaultRoleMapper implements RoleMapper {
    private static final String DELIMITER = ",";

    @Value("${okr.organisation.name.1stLevel}")
    private String firstLevelOrganisationName;
    @Value("${okr.organisation.name.2ndLevel}")
    private String secondLevelOrganisationName;
    @Value("${okr.user.champion.usernames}")
    private String okrChampionUsernames;

    @Override
    public List<AuthorizationRole> mapAuthorizationRoles(List<String> organisationNames, User user) {
        List<AuthorizationRole> roles = new ArrayList<>();
        if (hasFirstLevelOrganisationName(organisationNames) || isOkrChampion(user)) {
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

    private boolean isOkrChampion(User user) {
        String[] champions = okrChampionUsernames.split(DELIMITER);
        return Arrays.stream(champions).anyMatch(c -> Objects.equals(c, user.getUsername()));
    }
}
