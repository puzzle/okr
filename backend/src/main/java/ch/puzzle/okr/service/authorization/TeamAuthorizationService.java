package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.TEAM;
import static ch.puzzle.okr.service.authorization.AuthorizationService.hasRoleWriteAndReadAll;

@Service
public class TeamAuthorizationService {
    private final TeamBusinessService teamBusinessService;
    private final AuthorizationService authorizationService;

    public TeamAuthorizationService(TeamBusinessService teamBusinessService,
                                    AuthorizationService authorizationService) {
        this.teamBusinessService = teamBusinessService;
        this.authorizationService = authorizationService;
    }

    public Team createEntity(Team entity) {
        // everybody is allowed to create a new team
        Team savedTeam = teamBusinessService.createTeam(entity, authorizationService.updateOrAddAuthorizationUser());
        savedTeam.setWriteable(true);
        return savedTeam;
    }

    public Team updateEntity(Team entity, Long id) {
        checkUserAuthorization(OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_WRITE, TEAM), entity.getId());
        Team updatedTeam = teamBusinessService.updateTeam(entity, id);
        updatedTeam.setWriteable(true);
        return updatedTeam;
    }

    public void deleteEntity(Long id) {
        checkUserAuthorization(OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_DELETE, TEAM), id);
        teamBusinessService.deleteTeam(id);
    }

    public void addUsersToTeam(long entityId, List<Long> userIdList) {
        checkUserAuthorization(OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_WRITE, TEAM), entityId);
        teamBusinessService.addUsersToTeam(entityId, userIdList);
    }

    private void checkUserAuthorization(OkrResponseStatusException exception, Long teamId) {
        if (isUserWriteAllowed(teamId)) {
            return;
        }
        throw exception;
    }

    public boolean isUserWriteAllowed(Long teamId) {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        if (hasRoleWriteAndReadAll(authorizationUser)) {
            return true;
        }
        return authorizationUser.isUserAdminInTeam(teamId);
    }

    public List<Team> getAllTeams() {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        List<Team> allTeams = teamBusinessService.getAllTeams(authorizationUser);
        allTeams.forEach(team -> team.setWriteable(isUserWriteAllowed(team.getId())));
        return allTeams;
    }

    public void removeUserFromTeam(long entityId, long userId) {
        // user is allowed to remove own membership of any team
        if (userId != authorizationService.updateOrAddAuthorizationUser().user().getId()) {
            checkUserAuthorization(OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_WRITE, TEAM), entityId);
        }
        teamBusinessService.removeUserFromTeam(entityId, userId);
    }

    public void updateOrAddTeamMembership(long entityId, long userId, boolean isAdmin) {
        checkUserAuthorization(OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_WRITE, TEAM), entityId);
        teamBusinessService.updateOrAddTeamMembership(entityId, userId, isAdmin);
    }
}
