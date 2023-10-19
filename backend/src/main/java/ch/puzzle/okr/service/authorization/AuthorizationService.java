package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;

import static ch.puzzle.okr.models.authorization.AuthorizationReadRole.*;
import static ch.puzzle.okr.models.authorization.AuthorizationWriteRole.*;
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
    private final JwtUserConverter jwtUserConverter;

    public AuthorizationService(AuthorizationRegistrationService authorizationRegistrationService,
            ObjectivePersistenceService objectivePersistenceService, JwtUserConverter jwtUserConverter) {
        this.authorizationRegistrationService = authorizationRegistrationService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.jwtUserConverter = jwtUserConverter;
    }

    public static boolean hasRoleReadTeamsDraft(AuthorizationUser user) {
        return user.readRoles().contains(READ_TEAMS_DRAFT);
    }

    public static boolean hasRoleReadTeamDraft(AuthorizationUser user) {
        return user.readRoles().contains(READ_TEAM_DRAFT);
    }

    public static boolean hasRoleReadAllPublished(AuthorizationUser user) {
        return user.readRoles().contains(READ_ALL_PUBLISHED);
    }

    public static boolean hasRoleReadAllDraft(AuthorizationUser user) {
        return user.readRoles().contains(READ_ALL_DRAFT);
    }

    public static boolean hasRoleWriteAll(AuthorizationUser user) {
        return user.writeRoles().contains(WRITE_ALL);
    }

    public static boolean hasRoleWriteAllTeams(AuthorizationUser user) {
        return user.writeRoles().contains(WRITE_ALL_TEAMS);
    }

    public static boolean hasRoleWriteTeam(AuthorizationUser user) {
        return user.writeRoles().contains(WRITE_TEAM);
    }

    public AuthorizationUser getAuthorizationUser(Jwt token) {
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

    public void hasRoleDeleteByCheckInId(Long checkInId, AuthorizationUser authorizationUser) {
        Objective objective = objectivePersistenceService.findObjectiveByCheckInId(checkInId, authorizationUser,
                NOT_AUTHORIZED_TO_READ_CHECK_IN);
        hasRoleWrite(authorizationUser, objective.getTeam(), NOT_AUTHORIZED_TO_DELETE_CHECK_IN);
    }

    private void hasRoleWrite(AuthorizationUser authorizationUser, Team team, String reason) {
        if (hasRoleWriteAll(authorizationUser)) {
            return;
        } else if (hasRoleWriteAllTeams(authorizationUser)
                && !Objects.equals(authorizationUser.firstLevelTeamId(), team.getId())) {
            return;
        } else if (hasRoleWriteTeam(authorizationUser) && authorizationUser.teamIds().contains(team.getId())) {
            return;
        }
        throw new ResponseStatusException(UNAUTHORIZED, reason);
    }
}
