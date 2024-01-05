package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserAuthorizationService {
    private final UserBusinessService userBusinessService;
    private final AuthorizationService authorizationService;

    private final TeamAuthorizationService teamAuthorizationService;

    public UserAuthorizationService(UserBusinessService userBusinessService,
            AuthorizationService authorizationService, TeamAuthorizationService teamAuthorizationService) {
        this.userBusinessService = userBusinessService;
        this.authorizationService = authorizationService;
        this.teamAuthorizationService = teamAuthorizationService;
    }

    public List<User> getAllUsers() {
        var allUsers = userBusinessService.getAllUsers();
        allUsers.forEach(this::setTeamWritableForUser);
        return allUsers;
    }

    private void setTeamWritableForUser(User user) {
        user.getUserTeamList().forEach(this::setTeamWritableForUserTeam);
    }

    private void setTeamWritableForUserTeam(UserTeam userTeam) {
        var team = userTeam.getTeam();
        team.setWriteable(teamAuthorizationService.isUserWriteAllowed(team.getId()));
    }

    public User getById(long id) {
        var user = userBusinessService.getUserById(id);
        setTeamWritableForUser(user);
        return user;
    }
}
