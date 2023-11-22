package ch.puzzle.okr.mapper.role;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationRole;

import java.util.List;

public interface RoleMapper {
    List<AuthorizationRole> mapAuthorizationRoles(List<String> organisationNames, User user);
}
