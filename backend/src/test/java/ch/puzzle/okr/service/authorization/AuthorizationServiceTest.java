package ch.puzzle.okr.service.authorization;

import static ch.puzzle.okr.Constants.*;
import static ch.puzzle.okr.ErrorKey.*;
import static ch.puzzle.okr.service.authorization.AuthorizationService.hasRoleWriteAndReadAll;
import static ch.puzzle.okr.service.authorization.AuthorizationService.hasRoleWriteForTeam;
import static ch.puzzle.okr.test.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.security.JwtHelper;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.test.TestHelper;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.jwt.Jwt;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {
    @InjectMocks
    private AuthorizationService authorizationService;
    @Mock
    AuthorizationRegistrationService authorizationRegistrationService;
    @Mock
    ObjectivePersistenceService objectivePersistenceService;
    @Mock
    JwtHelper jwtHelper;

    private final List<Team> adminTeams = List
            .of(Team.Builder.builder().withName("Team 1").withId(1L).build(),
                Team.Builder.builder().withName("Team 2").withId(2L).build());
    private final List<Team> memberTeams = List
            .of(Team.Builder.builder().withName("Team 3").withId(3L).build(),
                Team.Builder.builder().withName("Team 4").withId(4L).build());
    private final List<Team> otherTeams = List
            .of(Team.Builder.builder().withName("Team 5").withId(5L).build(),
                Team.Builder.builder().withName("Team 6").withId(6L).build());

    private final User user = defaultUserWithTeams(1L, adminTeams, memberTeams);
    private final User okrChampion = defaultOkrChampion(1L);

    @DisplayName("Should return false when user does not have write all role")
    @Test
    void shouldReturnFalseWhenUserDoesNotHaveWriteAllRole() {
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        assertFalse(hasRoleWriteAndReadAll(authorizationUser));
    }

    @DisplayName("Should return true when user is OKR champion for team")
    @Test
    void shouldReturnTrueWhenUserIsOkrChampionForTeam() {
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);
        assertTrue(hasRoleWriteForTeam(authorizationUser, 3L));
    }

    @DisplayName("Should return true when user is admin for team")
    @Test
    void shouldReturnTrueWhenUserIsAdminForTeam() {
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        assertTrue(hasRoleWriteForTeam(authorizationUser, 1L));
    }

    @DisplayName("Should return true when user is a member of the team")
    @Test
    void shouldReturnTrueWhenUserIsMemberOfTeam() {
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        assertTrue(hasRoleWriteForTeam(authorizationUser, 3L));
    }

    @DisplayName("Should return false when user is not a member of the team")
    @Test
    void shouldReturnFalseWhenUserIsNotMemberOfTeam() {
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        assertFalse(hasRoleWriteForTeam(authorizationUser, 5L));
    }

    @DisplayName("Should return AuthorizationUser from security context")
    @Test
    void shouldReturnAuthorizationUserFromSecurityContext() {
        User user = defaultUser(null);
        Jwt token = mockJwtToken(user);
        AuthorizationUser authorizationUser = defaultAuthorizationUser();
        setSecurityContext(token);

        when(jwtHelper.getUserFromJwt(any(Jwt.class))).thenReturn(user);
        when(authorizationRegistrationService.updateOrAddAuthorizationUser(user)).thenReturn(authorizationUser);

        assertNotNull(authorizationService.updateOrAddAuthorizationUser());
    }

    @DisplayName("Should allow read access by objective ID when permitted")
    @Test
    void shouldAllowReadAccessByObjectiveIdWhenPermitted() {
        Long id = 1L;
        OkrResponseStatusException okrResponseStatusException = OkrResponseStatusException
                .of(NOT_AUTHORIZED_TO_READ, OBJECTIVE);
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertDoesNotThrow(() -> authorizationService.hasRoleReadByObjectiveId(id, authorizationUser));
        verify(objectivePersistenceService, times(1))
                .findObjectiveById(id, authorizationUser, okrResponseStatusException);
    }

    @DisplayName("Should throw exception when objective not found for read by KeyResult ID")
    @Test
    void shouldThrowExceptionWhenObjectiveNotFoundForReadByKeyResultId() {
        Long id = 13L;
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        OkrResponseStatusException expectedException = OkrResponseStatusException
                .of(NOT_AUTHORIZED_TO_READ, "KeyResult");

        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(id), eq(authorizationUser), any()))
                .thenThrow(expectedException);

        OkrResponseStatusException actualException = assertThrows(OkrResponseStatusException.class,
                                                                  () -> authorizationService
                                                                          .hasRoleReadByKeyResultId(id,
                                                                                                    authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_READ, "KeyResult"));

        assertEquals(UNAUTHORIZED, actualException.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(actualException.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(actualException.getReason()));
    }

    @DisplayName("Should allow read access by KeyResult ID when permitted")
    @Test
    void shouldAllowReadAccessByKeyResultIdWhenPermitted() {
        Long id = 13L;
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertDoesNotThrow(() -> authorizationService.hasRoleReadByKeyResultId(id, authorizationUser));
        verify(objectivePersistenceService, times(1))
                .findObjectiveByKeyResultId(id,
                                            authorizationUser,
                                            OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, KEY_RESULT));
    }

    @DisplayName("Should throw exception when objective not found for read by CheckIn ID")
    @Test
    void shouldThrowExceptionWhenObjectiveNotFoundForReadByCheckInId() {
        Long id = 13L;
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        OkrResponseStatusException expectedException = OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, CHECK_IN);
        when(objectivePersistenceService.findObjectiveByCheckInId(eq(id), eq(authorizationUser), any()))
                .thenThrow(expectedException);

        OkrResponseStatusException actualException = assertThrows(OkrResponseStatusException.class,
                                                                  () -> authorizationService
                                                                          .hasRoleReadByCheckInId(id,
                                                                                                  authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_READ, CHECK_IN));

        assertEquals(UNAUTHORIZED, actualException.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(actualException.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(actualException.getReason()));
    }

    @DisplayName("Should allow read access by CheckIn ID when permitted")
    @Test
    void shouldAllowReadAccessByCheckInIdWhenPermitted() {
        Long id = 6L;
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertDoesNotThrow(() -> authorizationService.hasRoleReadByCheckInId(id, authorizationUser));
        verify(objectivePersistenceService, times(1))
                .findObjectiveByCheckInId(id,
                                          authorizationUser,
                                          OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ, CHECK_IN));
    }

    @DisplayName("Should allow create or update for all objectives when authorized")
    @Test
    void shouldAllowCreateOrUpdateForAllObjectivesWhenAuthorized() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdate(objective, authorizationUser));
    }

    @DisplayName("Should allow create or update when user is admin")
    @Test
    void shouldAllowCreateOrUpdateWhenUserIsAdmin() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(1L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdate(objective, authorizationUser));
    }

    @DisplayName("Should allow create or update when user is team member")
    @Test
    void shouldAllowCreateOrUpdateWhenUserIsTeamMember() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(3L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdate(objective, authorizationUser));
    }

    @DisplayName("Should throw exception for create or update when user is not in team")
    @Test
    void shouldThrowExceptionForCreateOrUpdateWhenUserIsNotInTeam() {
        var id = otherTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> authorizationService
                                                                    .hasRoleCreateOrUpdate(objective,
                                                                                           authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_WRITE, "Objective"));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should allow create or update for key results when authorized")
    @Test
    void shouldAllowCreateOrUpdateForKeyResultsWhenAuthorized() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(1L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);
        when(objectivePersistenceService
                .findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdate(keyResult, authorizationUser));
    }

    @DisplayName("Should allow create or update as admin for key results")
    @Test
    void shouldAllowCreateOrUpdateAsAdminForKeyResults() {
        var id = adminTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService
                .findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdate(keyResult, authorizationUser));
    }

    @DisplayName("Should throw exception when not authorized for key results")
    @Test
    void shouldThrowExceptionWhenNotAuthorizedForKeyResults() {
        var id = otherTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService
                .findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser), any()))
                .thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> authorizationService
                                                                    .hasRoleCreateOrUpdate(keyResult,
                                                                                           authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_WRITE, "KeyResult"));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should allow create or update as admin for team check-ins")
    @Test
    void shouldPassThroughWhenAuthorizedAsAdminForTeamCheckIns() {
        var id = adminTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService
                .findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getObjective().getId()),
                                            eq(authorizationUser),
                                            any()))
                .thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdate(checkIn, authorizationUser));
    }

    @DisplayName("Should pass through when authorized as member for team check-ins")
    @Test
    void shouldPassThroughWhenAuthorizedAsMemberForTeamCheckIns() {
        var id = memberTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService
                .findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getObjective().getId()),
                                            eq(authorizationUser),
                                            any()))
                .thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdate(checkIn, authorizationUser));
    }

    @DisplayName("Should throw exception when not in team for team check-ins")
    @Test
    void shouldThrowExceptionWhenNotInTeamForTeamCheckIns() {
        var id = otherTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService
                .findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getObjective().getId()),
                                            eq(authorizationUser),
                                            any()))
                .thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> authorizationService
                                                                    .hasRoleCreateOrUpdate(checkIn, authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_WRITE, "Check-in"));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should pass through when authorized for all objectives by objective ID")
    @Test
    void shouldPassThroughWhenAuthorizedForAllObjectivesByObjectiveId() {
        Long id = 13L;
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);

        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any())).thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdateByObjectiveId(id, authorizationUser));
    }

    @DisplayName("Should pass through when authorized as admin by objective ID")
    @Test
    void shouldPassThroughWhenAuthorizedAsAdminByObjectiveId() {
        var id = adminTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any())).thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdateByObjectiveId(id, authorizationUser));
    }

    @DisplayName("Should pass through when authorized as member by objective ID")
    @Test
    void shouldPassThroughWhenAuthorizedAsMemberByObjectiveId() {
        var id = memberTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any())).thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleCreateOrUpdateByObjectiveId(id, authorizationUser));
    }

    @DisplayName("Should throw exception when not in team by objective ID")
    @Test
    void shouldThrowExceptionWhenNotInTeamByObjectiveId() {
        var id = otherTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any())).thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> authorizationService
                                                                    .hasRoleCreateOrUpdateByObjectiveId(id,
                                                                                                        authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_WRITE, "Objective"));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @DisplayName("Should return true when authorized to write all objectives for team")
    @Test
    void shouldReturnTrueWhenAuthorizedToWriteAllObjectivesForTeam() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);

        assertTrue(authorizationService.hasRoleWriteForTeam(objective, authorizationUser));
    }

    @DisplayName("Should return true when authorized as admin for team")
    @Test
    void shouldReturnTrueWhenAuthorizedAsAdminForTeam() {
        var id = adminTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertTrue(authorizationService.hasRoleWriteForTeam(objective, authorizationUser));
    }

    @DisplayName("Should return true when authorized as member for team")
    @Test
    void shouldReturnTrueWhenAuthorizedAsMemberForTeam() {
        var id = memberTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertTrue(authorizationService.hasRoleWriteForTeam(objective, authorizationUser));
    }

    @DisplayName("Should return false when not member of team")
    @Test
    void shouldReturnFalseWhenNotMemberOfTeam() {
        var id = otherTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertFalse(authorizationService.hasRoleWriteForTeam(objective, authorizationUser));
    }

    @DisplayName("Should return true when authorized to write all key results for team")
    @Test
    void shouldReturnTrueWhenAuthorizedToWriteAllKeyResultsForTeam() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(4L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);

        when(objectivePersistenceService
                .findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertTrue(authorizationService.hasRoleWriteForTeam(keyResult, authorizationUser));
    }

    @DisplayName("Should return true when authorized as admin for key results")
    @Test
    void shouldReturnTrueWhenAuthorizedAsAdminForKeyResults() {
        var id = adminTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService
                .findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertTrue(authorizationService.hasRoleWriteForTeam(keyResult, authorizationUser));
    }

    @DisplayName("Should return false when not authorized to write key results")
    @Test
    void shouldReturnFalseWhenNotAuthorizedToWriteKeyResults() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = mockAuthorizationUser(user);

        when(objectivePersistenceService
                .findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertFalse(authorizationService.hasRoleWriteForTeam(keyResult, authorizationUser));
    }

    @DisplayName("Should return true when authorized to write all check-ins for team")
    @Test
    void shouldReturnTrueWhenAuthorizedToWriteAllCheckInsForTeam() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);

        when(objectivePersistenceService
                .findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getId()), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertTrue(authorizationService.hasRoleWriteForTeam(checkIn, authorizationUser));
    }

    @DisplayName("Should return true when authorized as admin for check-ins")
    @Test
    void shouldReturnTrueWhenAuthorizedAsAdminForCheckIns() {
        var id = adminTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService
                .findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getId()), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertTrue(authorizationService.hasRoleWriteForTeam(checkIn, authorizationUser));
    }

    @DisplayName("Should return false when not authorized to write check-ins")
    @Test
    void shouldReturnFalseWhenNotAuthorizedToWriteCheckIns() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = mockAuthorizationUser(user);

        when(objectivePersistenceService
                .findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getId()), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertFalse(authorizationService.hasRoleWriteForTeam(checkIn, authorizationUser));
    }

    @DisplayName("Should pass through when authorized for all objectives by objective ID for deletion")
    @Test
    void shouldPassThroughWhenAuthorizedForAllObjectivesByObjectiveIdForDeletion() {
        Long id = 13L;
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);

        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any())).thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleDeleteByObjectiveId(id, authorizationUser));
    }

    @DisplayName("Should pass through when authorized for all team key results by key result ID")
    @Test
    void shouldPassThroughWhenAuthorizedForAllTeamKeyResultsByKeyResultId() {
        Long id = 13L;
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(1L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);

        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(id), eq(authorizationUser), any()))
                .thenReturn(objective);

        authorizationService.hasRoleDeleteByKeyResultId(id, authorizationUser);
        assertDoesNotThrow(() -> authorizationService.hasRoleDeleteByKeyResultId(id, authorizationUser));
    }

    @DisplayName("Should pass through when authorized as admin for team check-ins by check-in ID")
    @Test
    void shouldPassThroughWhenAuthorizedAsAdminForTeamCheckInsByCheckInId() {
        var id = adminTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(1L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveByCheckInId(eq(id), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleDeleteByCheckInId(id, authorizationUser));
    }

    @DisplayName("Should pass through when authorized as member for team check-ins by check-in ID")
    @Test
    void shouldPassThroughWhenAuthorizedAsMemberForTeamCheckInsByCheckInId() {
        var id = memberTeams.getFirst().getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(1L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveByCheckInId(eq(id), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleDeleteByCheckInId(id, authorizationUser));
    }

    @DisplayName("Should pass through when OKR champion for key results by key result ID")
    @Test
    void shouldPassThroughWhenOkrChampionForKeyResultsByKeyResultId() {
        var otherTeamId = otherTeams.getFirst().getId();
        Objective objective = Objective.Builder
                .builder()
                .withTeam(Team.Builder.builder().withId(otherTeamId).build())
                .build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrChampion);
        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(1L), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleDeleteByKeyResultId(1L, authorizationUser));
    }

    @DisplayName("Should pass through when admin for key results by key result ID")
    @Test
    void shouldPassThroughWhenAdminForKeyResultsByKeyResultId() {
        var otherTeamId = adminTeams.getFirst().getId();
        Objective objective = Objective.Builder
                .builder()
                .withTeam(Team.Builder.builder().withId(otherTeamId).build())
                .build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(1L), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleDeleteByKeyResultId(1L, authorizationUser));
    }

    @DisplayName("Should pass through when member for key results by key result ID")
    @Test
    void shouldPassThroughWhenMemberForKeyResultsByKeyResultId() {
        var otherTeamId = memberTeams.getFirst().getId();
        Objective objective = Objective.Builder
                .builder()
                .withTeam(Team.Builder.builder().withId(otherTeamId).build())
                .build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(1L), eq(authorizationUser), any()))
                .thenReturn(objective);

        assertDoesNotThrow(() -> authorizationService.hasRoleDeleteByKeyResultId(1L, authorizationUser));
    }

    @DisplayName("Should throw exception when not in team for key results by key result ID")
    @Test
    void shouldThrowExceptionWhenNotInTeamForKeyResultsByKeyResultId() {
        var otherTeamId = otherTeams.getFirst().getId();
        Objective objective = Objective.Builder
                .builder()
                .withTeam(Team.Builder.builder().withId(otherTeamId).build())
                .build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(1L), eq(authorizationUser), any()))
                .thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> authorizationService
                                                                    .hasRoleDeleteByKeyResultId(1L, authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_DELETE, "KeyResult"));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    private void setSecurityContext(Jwt token) {
        SecurityContextHolder.setContext(new SecurityContextImpl(new Authentication() {
            @Override
            public Collection<? extends GrantedAuthority> getAuthorities() {
                return null;
            }

            @Override
            public Object getCredentials() {
                return null;
            }

            @Override
            public Object getDetails() {
                return null;
            }

            @Override
            public Object getPrincipal() {
                return token;
            }

            @Override
            public boolean isAuthenticated() {
                return false;
            }

            @Override
            public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {

            }

            @Override
            public String getName() {
                return "unit test authentication";
            }
        }));
    }
}
