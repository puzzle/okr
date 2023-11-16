package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationRole;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
        return teamBusinessService.createTeam(entity);
    }

    public Team updateEntity(Team entity, Long id) {
        checkUserAuthorization("not authorized to update team");
        return teamBusinessService.updateTeam(entity, id);
    }

    public void deleteEntity(Long id) {
        checkUserAuthorization("not authorized to delete team");
        teamBusinessService.deleteTeam(id);
    }

    public void checkUserAuthorization(String message) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        if (!authorizationUser.roles().contains(AuthorizationRole.WRITE_ALL)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, message);
        }
    }

    public List<Team> getEntities() {
        return teamBusinessService.getAllTeams();
    }
}
