package ch.puzzle.okr.models.authorization;

import java.util.List;

import ch.puzzle.okr.models.User;

public record AuthorizationUser(User user) {
    public List<Long> extractTeamIds() {
        return this.user.getUserTeamList()
                        .stream()
                        .map(userTeam -> userTeam.getTeam()
                                                 .getId())
                        .toList();
    }

    public boolean isUserMemberInTeam(Long teamId) {
        return this.user.getUserTeamList()
                        .stream()
                        .anyMatch(ut -> ut.getTeam()
                                          .getId()
                                          .equals(teamId));
    }

    public boolean isUserAdminInTeam(Long teamId) {
        return this.user.getUserTeamList()
                        .stream()
                        .anyMatch(ut -> ut.isTeamAdmin() && ut.getTeam()
                                                              .getId()
                                                              .equals(teamId));
    }
}
