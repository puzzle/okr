package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.security.JwtHelper;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import static ch.puzzle.okr.Constants.*;
import static ch.puzzle.okr.ErrorKey.*;

@Service
public class AuthorizationService {

    private final AuthorizationRegistrationService authorizationRegistrationService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final ActionPersistenceService actionPersistenceService;
    private final JwtHelper jwtHelper;

    public AuthorizationService(AuthorizationRegistrationService authorizationRegistrationService,
            ObjectivePersistenceService objectivePersistenceService, ActionPersistenceService actionPersistenceService,
            JwtHelper jwtHelper) {
        this.authorizationRegistrationService = authorizationRegistrationService;
        this.actionPersistenceService = actionPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.jwtHelper = jwtHelper;
    }

    public static boolean hasRoleWriteForTeam(AuthorizationUser authorizationUser, Long teamId) {
        if (hasRoleWriteAndReadAll(authorizationUser)) {
            return true;
        }
        return authorizationUser.isUserAdminInTeam(teamId);
    }

    public static void checkRoleWriteAndReadAll(AuthorizationUser user,
            OkrResponseStatusException notAuthorizedException) {
        if (hasRoleWriteAndReadAll(user)) {
            return;
        }
        throw notAuthorizedException;
    }

    public static boolean hasRoleWriteAndReadAll(AuthorizationUser user) {
        return user.user().isOkrChampion();
    }

    public AuthorizationUser updateOrAddAuthorizationUser() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Jwt token = (Jwt) authentication.getPrincipal();

        User user = jwtHelper.getUserFromJwt(token);

        return authorizationRegistrationService.updateOrAddAuthorizationUser(user);
    }

    public void hasRoleReadByObjectiveId(Long objectiveId, AuthorizationUser authorizationUser) {
        objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser,
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, OBJECTIVE));
    }

    public void hasRoleReadByKeyResultId(Long keyResultId, AuthorizationUser authorizationUser) {
        objectivePersistenceService.findObjectiveByKeyResultId(keyResultId, authorizationUser,
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, KEY_RESULT));
    }

    public void hasRoleReadByCheckInId(Long checkInId, AuthorizationUser authorizationUser) {
        objectivePersistenceService.findObjectiveByCheckInId(checkInId, authorizationUser,
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, CHECK_IN));
    }

    public void hasRoleCreateOrUpdate(Objective objective, AuthorizationUser authorizationUser) {

        hasRoleWriteForTeam(authorizationUser, objective.getTeam(),
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_WRITE, OBJECTIVE));
    }

    public void hasRoleCreateOrUpdate(KeyResult keyResult, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(keyResult.getObjective().getId(),
                authorizationUser, OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, KEY_RESULT));

        hasRoleWriteForTeam(authorizationUser, objective.getTeam(),
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_WRITE, KEY_RESULT));
    }

    public void hasRoleCreateOrUpdate(CheckIn checkIn, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(checkIn.getKeyResult().getId(),
                authorizationUser, OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, CHECK_IN));

        hasRoleWriteForTeam(authorizationUser, objective.getTeam(),
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_WRITE, CHECK_IN));
    }

    public boolean hasRoleWriteForTeam(Objective objective, AuthorizationUser authorizationUser) {
        return hasRoleWriteForTeam(authorizationUser, objective.getTeam());
    }

    public boolean hasRoleWriteForTeam(KeyResult keyResult, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(keyResult.getObjective().getId(),
                authorizationUser, OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, KEY_RESULT));
        return hasRoleWriteForTeam(authorizationUser, objective.getTeam());
    }

    public boolean hasRoleWriteForTeam(CheckIn checkIn, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(checkIn.getKeyResult().getId(),
                authorizationUser, OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, CHECK_IN));

        return hasRoleWriteForTeam(authorizationUser, objective.getTeam());
    }

    public void hasRoleCreateOrUpdateByObjectiveId(Long objectiveId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser,
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, OBJECTIVE));

        hasRoleWriteForTeam(authorizationUser, objective.getTeam(),
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_WRITE, OBJECTIVE));
    }

    public void hasRoleDeleteByObjectiveId(Long objectiveId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser,
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, OBJECTIVE));

        hasRoleWriteForTeam(authorizationUser, objective.getTeam(),
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_DELETE, OBJECTIVE));
    }

    public void hasRoleDeleteByKeyResultId(Long keyResultId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(keyResultId, authorizationUser,
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, KEY_RESULT));

        hasRoleWriteForTeam(authorizationUser, objective.getTeam(),
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_DELETE, KEY_RESULT));
    }

    public void hasRoleDeleteByActionId(Long actionId, AuthorizationUser authorizationUser) {
        Action action = actionPersistenceService.findById(actionId);
        hasRoleWriteForTeam(authorizationUser, action.getKeyResult().getObjective().getTeam(),
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_DELETE, ACTION));

    }

    public void hasRoleDeleteByCheckInId(Long checkInId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByCheckInId(checkInId, authorizationUser,
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, CHECK_IN));

        hasRoleWriteForTeam(authorizationUser, objective.getTeam(),
                OkrResponseStatusException.of(NOT_AUTHORIZED_TO_DELETE, CHECK_IN));
    }

    private void hasRoleWriteForTeam(AuthorizationUser authorizationUser, Team team,
            OkrResponseStatusException notAuthorizedException) {
        if (hasRoleWriteForTeam(authorizationUser, team)) {
            return;
        }
        throw notAuthorizedException;
    }

    private boolean hasRoleWriteForTeam(AuthorizationUser authorizationUser, Team team) {
        return hasRoleWriteForTeam(authorizationUser, team.getId());
    }
}
