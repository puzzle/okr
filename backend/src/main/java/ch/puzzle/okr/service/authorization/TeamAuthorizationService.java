package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.TEAM;
import static ch.puzzle.okr.service.authorization.AuthorizationService.hasRoleWriteAll;

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
        checkUserAuthorization(OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_WRITE, TEAM));
        Team savedTeam = teamBusinessService.createTeam(entity);
        savedTeam.setWriteable(true);
        return savedTeam;
    }

    public Team updateEntity(Team entity, Long id) {
        checkUserAuthorization(OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_WRITE, TEAM));
        Team updatedTeam = teamBusinessService.updateTeam(entity, id);
        updatedTeam.setWriteable(true);
        return updatedTeam;
    }

    public void deleteEntity(Long id) {
        checkUserAuthorization(OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_DELETE, TEAM));
        teamBusinessService.deleteTeam(id);
    }

    private void checkUserAuthorization(OkrResponseStatusException exception) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        if (!hasRoleWriteAll(authorizationUser)) {
            throw exception;
        }
    }

    public List<Long> getUserTeamIds() {
        return this.authorizationService.getAuthorizationUser().userTeamIds();
    }

    public List<Team> getAllTeams() {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        boolean isWritable = hasRoleWriteAll(authorizationUser);
        List<Team> allTeams = teamBusinessService.getAllTeams();
        allTeams.forEach(team -> team.setWriteable(isWritable));
        return allTeams;
    }
}
