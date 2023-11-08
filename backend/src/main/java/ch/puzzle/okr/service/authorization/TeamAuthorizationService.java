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

    public TeamAuthorizationService(TeamBusinessService teamBusinessService, AuthorizationService authorizationService) {
        this.teamBusinessService = teamBusinessService;
        this.authorizationService = authorizationService;
    }

    public Team createEntity(Team entity) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        if (!authorizationUser.roles().contains(AuthorizationRole.WRITE_ALL)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "not authorized to create teams");
        }
        return teamBusinessService.createTeam(entity);
    }

    public List<Team> getEntities() {
        return teamBusinessService.getAllTeams();
    }
}
