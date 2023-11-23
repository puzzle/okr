package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        checkUserAuthorization("not authorized to create team");
        Team savedTeam = teamBusinessService.createTeam(entity);
        savedTeam.setWriteable(true);
        return savedTeam;
    }

    public Team updateEntity(Team entity, Long id) {
        checkUserAuthorization("not authorized to update team");
        Team updatedTeam = teamBusinessService.updateTeam(entity, id);
        updatedTeam.setWriteable(true);
        return updatedTeam;
    }

    public void deleteEntity(Long id) {
        checkUserAuthorization("not authorized to delete team");
        teamBusinessService.deleteTeam(id);
    }

    private void checkUserAuthorization(String message) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        if (!hasRoleWriteAll(authorizationUser)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
        }
    }

    public List<Team> getAllTeams() {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        boolean isWritable = hasRoleWriteAll(authorizationUser);
        List<Team> allTeams = teamBusinessService.getAllTeams();
        allTeams.forEach(team -> team.setWriteable(isWritable));
        return allTeams;
    }
}
