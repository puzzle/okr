package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.dto.userokrdata.UserOkrDataDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.mapper.UserOkrDataMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.UserTeam;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.USER;

@Service
public class UserAuthorizationService {
    private final UserBusinessService userBusinessService;
    private final AuthorizationService authorizationService;

    private final TeamAuthorizationService teamAuthorizationService;
    private final KeyResultBusinessService keyResultBusinessService;

    public UserAuthorizationService(UserBusinessService userBusinessService, AuthorizationService authorizationService,
            TeamAuthorizationService teamAuthorizationService, KeyResultBusinessService keyResultBusinessService) {
        this.userBusinessService = userBusinessService;
        this.authorizationService = authorizationService;
        this.teamAuthorizationService = teamAuthorizationService;
        this.keyResultBusinessService = keyResultBusinessService;
    }

    public List<User> getAllUsers() {
        List<User> allUsers = userBusinessService.getAllUsers();
        allUsers.forEach(this::setTeamWritableForUser);
        return allUsers;
    }

    private void setTeamWritableForUser(User user) {
        user.getUserTeamList().forEach(this::setTeamWritableForUserTeam);
    }

    private void setTeamWritableForUserTeam(UserTeam userTeam) {
        Team team = userTeam.getTeam();
        team.setWriteable(teamAuthorizationService.isUserWriteAllowed(team.getId()));
    }

    public User getById(long id) {
        User user = userBusinessService.getUserById(id);
        setTeamWritableForUser(user);
        return user;
    }

    public User setIsOkrChampion(long id, boolean isOkrChampion) {
        User user = userBusinessService.getUserById(id);
        AuthorizationService.checkRoleWriteAndReadAll(authorizationService.updateOrAddAuthorizationUser(),
                OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_WRITE, USER));
        return userBusinessService.setIsOkrChampion(user, isOkrChampion);
    }

    public List<User> createUsers(List<User> userList) {
        AuthorizationService.checkRoleWriteAndReadAll(authorizationService.updateOrAddAuthorizationUser(),
                OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_WRITE, USER));
        return userBusinessService.createUsers(userList);
    }

    public boolean isUserMemberOfTeams(long id) {
        List<UserTeam> userTeamList = userBusinessService.getUserById(id).getUserTeamList();
        return userTeamList != null && !userTeamList.isEmpty();
    }

    public void deleteEntityById(long id) {
        AuthorizationService.checkRoleWriteAndReadAll(authorizationService.updateOrAddAuthorizationUser(),
                OkrResponseStatusException.of(ErrorKey.NOT_AUTHORIZED_TO_DELETE, USER));

        userBusinessService.deleteEntityById(id);
    }

    public UserOkrDataDto getUserOkrData(long id) {
        List<KeyResult> keyResultsOwnedByUser = keyResultBusinessService.getKeyResultsOwnedByUser(id);
        return new UserOkrDataMapper().toDto(keyResultsOwnedByUser);
    }
}
