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

    @Value("${okr.user.champion.emails}")
    private String okrChampionEmails;

    @Override
    public List<AuthorizationRole> mapAuthorizationRoles(User user) {
        List<AuthorizationRole> roles = new ArrayList<>();
        if (isOkrChampion(user)) {
            roles.addAll(List.of(READ_ALL_DRAFT, WRITE_ALL));
        } else {
            roles.addAll(List.of(READ_TEAM_DRAFT, WRITE_TEAM));
        }
        roles.add(READ_ALL_PUBLISHED);
        return roles;
    }

    private boolean isOkrChampion(User user) {
        String[] champions = okrChampionEmails.split(DELIMITER);
        return Arrays.stream(champions).anyMatch(c -> Objects.equals(c, user.getEmail()));
    }
}
