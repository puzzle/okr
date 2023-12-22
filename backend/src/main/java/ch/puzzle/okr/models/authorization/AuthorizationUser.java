package ch.puzzle.okr.models.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;

import java.util.List;

public record AuthorizationUser(User user, List<AuthorizationRole> roles) {
    public List<Long> extractTeamIds() {
        return this.user.getUserTeamList().stream()
                .map(UserTeam::getId)
                .toList();
    }

    public List<Long> extractTeamIdsWithAdminRole() {
        return this.user
                .getUserTeamList().stream()
                .filter(UserTeam::isTeamAdmin)
                .map(UserTeam::getId)
                .toList();
    }
}
