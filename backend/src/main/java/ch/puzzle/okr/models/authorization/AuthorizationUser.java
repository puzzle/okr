package ch.puzzle.okr.models.authorization;

import ch.puzzle.okr.models.User;

import java.util.List;

public record AuthorizationUser(User user, List<Long> teamIds, Long firstLevelTeamId,
        List<AuthorizationReadRole> readRoles, List<AuthorizationWriteRole> writeRoles) {
}
