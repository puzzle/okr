package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.converter.JwtUserConverter;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.models.authorization.AuthorizationRole.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@Service
public class AuthorizationService {
    private static final String NOT_AUTHORIZED_TO_READ_OBJECTIVE = "not authorized to read objective";
    private static final String NOT_AUTHORIZED_TO_READ_KEY_RESULT = "not authorized to read key result";
    private static final String NOT_AUTHORIZED_TO_READ_CHECK_IN = "not authorized to read check in";
    private static final String NOT_AUTHORIZED_TO_WRITE_OBJECTIVE = "not authorized to create or update objective";
    private static final String NOT_AUTHORIZED_TO_WRITE_KEY_RESULT = "not authorized to create or update key result";
    private static final String NOT_AUTHORIZED_TO_WRITE_CHECK_IN = "not authorized to create or update check in";
    private static final String NOT_AUTHORIZED_TO_DELETE_OBJECTIVE = "not authorized to delete objective";
    private static final String NOT_AUTHORIZED_TO_DELETE_KEY_RESULT = "not authorized to delete key result";
    private static final String NOT_AUTHORIZED_TO_DELETE_CHECK_IN = "not authorized to delete check in";
    private final AuthorizationRegistrationService authorizationRegistrationService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final ActionPersistenceService actionPersistenceService;
    private final JwtUserConverter jwtUserConverter;

    public AuthorizationService(AuthorizationRegistrationService authorizationRegistrationService,
            ObjectivePersistenceService objectivePersistenceService, ActionPersistenceService actionPersistenceService,
            JwtUserConverter jwtUserConverter) {
        this.authorizationRegistrationService = authorizationRegistrationService;
        this.actionPersistenceService = actionPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.jwtUserConverter = jwtUserConverter;
    }

    public static boolean hasRoleReadTeamsDraft(AuthorizationUser user) {
        return user.roles().contains(READ_TEAMS_DRAFT);
    }

    public static boolean hasRoleReadTeamDraft(AuthorizationUser user) {
        return user.roles().contains(READ_TEAM_DRAFT);
    }

    public static boolean hasRoleReadAllPublished(AuthorizationUser user) {
        return user.roles().contains(READ_ALL_PUBLISHED);
    }

    public static boolean hasRoleReadAllDraft(AuthorizationUser user) {
        return user.roles().contains(READ_ALL_DRAFT);
    }

    public static boolean hasRoleWriteAll(AuthorizationUser user) {
        return user.roles().contains(WRITE_ALL);
    }

    public static boolean hasRoleWriteAllTeams(AuthorizationUser user) {
        return user.roles().contains(WRITE_ALL_TEAMS);
    }

    public static boolean hasRoleWriteTeam(AuthorizationUser user) {
        return user.roles().contains(WRITE_TEAM);
    }

    public AuthorizationUser getAuthorizationUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Jwt token = (Jwt) authentication.getPrincipal();
        User user = jwtUserConverter.convert(token);
        return authorizationRegistrationService.registerAuthorizationUser(user, token);
    }

    public void hasRoleReadByObjectiveId(Long objectiveId, AuthorizationUser authorizationUser) {
        objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser, NOT_AUTHORIZED_TO_READ_OBJECTIVE);
    }

    public void hasRoleReadByKeyResultId(Long keyResultId, AuthorizationUser authorizationUser) {
        objectivePersistenceService.findObjectiveByKeyResultId(keyResultId, authorizationUser,
                NOT_AUTHORIZED_TO_READ_KEY_RESULT);
    }

    public void hasRoleReadByCheckInId(Long checkInId, AuthorizationUser authorizationUser) {
        objectivePersistenceService.findObjectiveByCheckInId(checkInId, authorizationUser,
                NOT_AUTHORIZED_TO_READ_CHECK_IN);
    }

    public void hasRoleCreateOrUpdate(Objective objective, AuthorizationUser authorizationUser) {
        hasRoleWrite(authorizationUser, objective.getTeam(), NOT_AUTHORIZED_TO_WRITE_OBJECTIVE);
    }

    public void hasRoleCreateOrUpdate(KeyResult keyResult, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(keyResult.getObjective().getId(),
                authorizationUser, NOT_AUTHORIZED_TO_READ_KEY_RESULT);
        hasRoleWrite(authorizationUser, objective.getTeam(), NOT_AUTHORIZED_TO_WRITE_KEY_RESULT);
    }

    public void hasRoleCreateOrUpdate(CheckIn checkIn, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(checkIn.getKeyResult().getId(),
                authorizationUser, NOT_AUTHORIZED_TO_READ_CHECK_IN);
        hasRoleWrite(authorizationUser, objective.getTeam(), NOT_AUTHORIZED_TO_WRITE_CHECK_IN);
    }

    public boolean isWriteable(Objective objective, AuthorizationUser authorizationUser) {
        return isWriteable(authorizationUser, objective.getTeam());
    }

    public boolean isWriteable(KeyResult keyResult, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(keyResult.getObjective().getId(),
                authorizationUser, NOT_AUTHORIZED_TO_READ_KEY_RESULT);
        return isWriteable(authorizationUser, objective.getTeam());
    }

    public boolean isWriteable(CheckIn checkIn, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(checkIn.getKeyResult().getId(),
                authorizationUser, NOT_AUTHORIZED_TO_READ_CHECK_IN);
        return isWriteable(authorizationUser, objective.getTeam());
    }

    public void hasRoleCreateOrUpdateByObjectiveId(Long objectiveId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser,
                NOT_AUTHORIZED_TO_READ_OBJECTIVE);
        hasRoleWrite(authorizationUser, objective.getTeam(), NOT_AUTHORIZED_TO_WRITE_OBJECTIVE);
    }

    public void hasRoleDeleteByObjectiveId(Long objectiveId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser,
                NOT_AUTHORIZED_TO_READ_OBJECTIVE);
        hasRoleWrite(authorizationUser, objective.getTeam(), NOT_AUTHORIZED_TO_DELETE_OBJECTIVE);
    }

    public void hasRoleDeleteByKeyResultId(Long keyResultId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(keyResultId, authorizationUser,
                NOT_AUTHORIZED_TO_READ_KEY_RESULT);
        hasRoleWrite(authorizationUser, objective.getTeam(), NOT_AUTHORIZED_TO_DELETE_KEY_RESULT);
    }

    public void hasRoleDeleteByActionId(Long actionId, AuthorizationUser authorizationUser) {
        Action action = actionPersistenceService.findById(actionId);
        hasRoleWrite(authorizationUser, action.getKeyResult().getObjective().getTeam(),
                NOT_AUTHORIZED_TO_DELETE_KEY_RESULT);
    }

    public void hasRoleDeleteByCheckInId(Long checkInId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByCheckInId(checkInId, authorizationUser,
                NOT_AUTHORIZED_TO_READ_CHECK_IN);
        hasRoleWrite(authorizationUser, objective.getTeam(), NOT_AUTHORIZED_TO_DELETE_CHECK_IN);
    }

    private void hasRoleWrite(AuthorizationUser authorizationUser, Team team, String reason) {
        if (isWriteable(authorizationUser, team)) {
            return;
        }
        throw new ResponseStatusException(UNAUTHORIZED, reason);
    }

    private boolean isWriteable(AuthorizationUser authorizationUser, Team team) {
        return isWriteable(authorizationUser, team.getId());
    }

    public boolean isWriteable(AuthorizationUser authorizationUser, Long teamId) {
        if (hasRoleWriteAll(authorizationUser)) {
            return true;
        } else if (hasRoleWriteAllTeams(authorizationUser) && !authorizationUser.firstLevelTeamIds().contains(teamId)) {
            return true;
        } else
            return hasRoleWriteTeam(authorizationUser) && authorizationUser.userTeamIds().contains(teamId);
    }
}
