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
        if (!hasRoleWriteAndReadAll(authorizationUser)) {
            throw exception;
        }
    }

    public List<Team> getAllTeams() {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        boolean isWritable = hasRoleWriteAndReadAll(authorizationUser);
        List<Team> allTeams = teamBusinessService.getAllTeams(authorizationUser);
        allTeams.forEach(team -> team.setWriteable(isWritable));
        return allTeams;
    }
}
