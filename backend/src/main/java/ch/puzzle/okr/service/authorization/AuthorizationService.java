package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.converter.JwtUserConverter;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.converter.JwtConverterFactory;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.ActionPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import static ch.puzzle.okr.models.authorization.AuthorizationRole.*;

@Service
public class AuthorizationService {

    private final AuthorizationRegistrationService authorizationRegistrationService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final ActionPersistenceService actionPersistenceService;
    private final JwtConverterFactory jwtConverterFactory;

    public AuthorizationService(AuthorizationRegistrationService authorizationRegistrationService,
            ObjectivePersistenceService objectivePersistenceService, ActionPersistenceService actionPersistenceService,
            JwtConverterFactory jwtConverterFactory) {
        this.authorizationRegistrationService = authorizationRegistrationService;
        this.actionPersistenceService = actionPersistenceService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.jwtConverterFactory = jwtConverterFactory;
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
        User user = jwtConverterFactory.getJwtUserConverter().convert(token);
        return authorizationRegistrationService.registerAuthorizationUser(user, token);
    }

    public void hasRoleReadByObjectiveId(Long objectiveId, AuthorizationUser authorizationUser) {
        objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser,
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.OBJECTIVE));
    }

    public void hasRoleReadByKeyResultId(Long keyResultId, AuthorizationUser authorizationUser) {
        objectivePersistenceService.findObjectiveByKeyResultId(keyResultId, authorizationUser,
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.KEY_RESULT));
    }

    public void hasRoleReadByCheckInId(Long checkInId, AuthorizationUser authorizationUser) {
        objectivePersistenceService.findObjectiveByCheckInId(checkInId, authorizationUser,
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.CHECK_IN));
    }

    public void hasRoleCreateOrUpdate(Objective objective, AuthorizationUser authorizationUser) {

        hasRoleWrite(authorizationUser, objective.getTeam(),
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_WRITE, Constants.OBJECTIVE));
    }

    public void hasRoleCreateOrUpdate(KeyResult keyResult, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(keyResult.getObjective().getId(),
                authorizationUser, new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.KEY_RESULT));

        hasRoleWrite(authorizationUser, objective.getTeam(),
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_WRITE, Constants.KEY_RESULT));
    }

    public void hasRoleCreateOrUpdate(CheckIn checkIn, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(checkIn.getKeyResult().getId(),
                authorizationUser, new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.CHECK_IN));

        hasRoleWrite(authorizationUser, objective.getTeam(),
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_WRITE, Constants.CHECK_IN));
    }

    public boolean isWriteable(Objective objective, AuthorizationUser authorizationUser) {
        return isWriteable(authorizationUser, objective.getTeam());
    }

    public boolean isWriteable(KeyResult keyResult, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(keyResult.getObjective().getId(),
                authorizationUser, new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.KEY_RESULT));
        return isWriteable(authorizationUser, objective.getTeam());
    }

    public boolean isWriteable(CheckIn checkIn, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(checkIn.getKeyResult().getId(),
                authorizationUser, new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.CHECK_IN));

        return isWriteable(authorizationUser, objective.getTeam());
    }

    public void hasRoleCreateOrUpdateByObjectiveId(Long objectiveId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser,
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.OBJECTIVE));

        hasRoleWrite(authorizationUser, objective.getTeam(),
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_WRITE, Constants.OBJECTIVE));
    }

    public void hasRoleDeleteByObjectiveId(Long objectiveId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveById(objectiveId, authorizationUser,
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.OBJECTIVE));

        hasRoleWrite(authorizationUser, objective.getTeam(),
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_DELETE, Constants.OBJECTIVE));
    }

    public void hasRoleDeleteByKeyResultId(Long keyResultId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByKeyResultId(keyResultId, authorizationUser,
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.KEY_RESULT));

        hasRoleWrite(authorizationUser, objective.getTeam(),
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_DELETE, Constants.KEY_RESULT));
    }

    public void hasRoleDeleteByActionId(Long actionId, AuthorizationUser authorizationUser) {
        Action action = actionPersistenceService.findById(actionId);
        hasRoleWrite(authorizationUser, action.getKeyResult().getObjective().getTeam(),
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_DELETE, Constants.ACTION));

    }

    public void hasRoleDeleteByCheckInId(Long checkInId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByCheckInId(checkInId, authorizationUser,
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_READ, Constants.CHECK_IN));

        hasRoleWrite(authorizationUser, objective.getTeam(),
                new ErrorDto(ErrorMsg.NOT_AUTHORIZED_TO_DELETE, Constants.CHECK_IN));
    }

    private void hasRoleWrite(AuthorizationUser authorizationUser, Team team, ErrorDto error) {
        if (isWriteable(authorizationUser, team)) {
            return;
        }
        throw new OkrResponseStatusException(HttpStatus.UNAUTHORIZED, error);
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
