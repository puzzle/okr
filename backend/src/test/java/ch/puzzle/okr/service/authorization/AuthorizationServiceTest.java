package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.test.TestHelper;
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

import java.util.Collection;
import java.util.List;

import static ch.puzzle.okr.ErrorKey.*;
import static ch.puzzle.okr.test.TestHelper.*;
import static ch.puzzle.okr.service.authorization.AuthorizationService.hasRoleWriteAndReadAll;
import static ch.puzzle.okr.service.authorization.AuthorizationService.hasRoleWriteForTeam;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

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

    private final List<Team> adminTeams = List.of(Team.Builder.builder().withName("Team 1").withId(1L).build(),
            Team.Builder.builder().withName("Team 2").withId(2L).build());
    private final List<Team> memberTeams = List.of(Team.Builder.builder().withName("Team 3").withId(3L).build(),
            Team.Builder.builder().withName("Team 4").withId(4L).build());
    private final List<Team> otherTeams = List.of(Team.Builder.builder().withName("Team 5").withId(5L).build(),
            Team.Builder.builder().withName("Team 6").withId(6L).build());

    private final User user = defaultUserWithTeams(1L, adminTeams, memberTeams);
    private final User okrUser = defaultOkrChampion(1L);

    @Test
    void hasRoleWriteAllShouldReturnTrueWhenContainsRole() {
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);
        assertTrue(hasRoleWriteAndReadAll(authorizationUser));
    }

    @Test
    void hasRoleWriteAllShouldReturnFalseWhenDoesNotContainsRole() {
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        assertFalse(hasRoleWriteAndReadAll(authorizationUser));
    }

    @Test
    void hasRoleWriteForTeam_shouldReturnTrueWhenOkrChampion() {
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);
        assertTrue(hasRoleWriteForTeam(authorizationUser, 3L));
    }

    @Test
    void hasRoleWriteForTeam_shouldReturnTrueWhenUserIsAdmin() {
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        assertTrue(hasRoleWriteForTeam(authorizationUser, 1L));
    }

    @Test
    void hasRoleWriteForTeam_shouldReturnFalseWhenUserIsNotAdmin() {
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        assertFalse(hasRoleWriteForTeam(authorizationUser, 3L));
    }

    @Test
    void getAuthorizationUserShouldReturnAuthorizationUser() {
        User user = defaultUser(null);
        Jwt token = mockJwtToken(user);
        AuthorizationUser authorizationUser = defaultAuthorizationUser();
        setSecurityContext(token);

        when(jwtHelper.getUserFromJwt(any(Jwt.class))).thenReturn(user);
        when(authorizationRegistrationService.updateOrAddAuthorizationUser(user)).thenReturn(authorizationUser);

        assertNotNull(authorizationService.updateOrAddAuthorizationUser());
    }

    @Test
    void hasRoleReadByObjectiveIdShouldPassThroughWhenPermitted() {
        Long id = 13L;
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any()))
                .thenReturn(new Objective());

        authorizationService.hasRoleReadByObjectiveId(id, authorizationUser);
    }

    @Test
    void hasRoleReadByKeyResultIdShouldThrowExceptionWhenObjectiveNotFound() {
        Long id = 13L;
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        OkrResponseStatusException expectedException = OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ,
                "KeyResult");

        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(id), eq(authorizationUser), any()))
                .thenThrow(expectedException);

        OkrResponseStatusException actualException = assertThrows(OkrResponseStatusException.class,
                () -> authorizationService.hasRoleReadByKeyResultId(id, authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_READ, "KeyResult"));

        assertEquals(UNAUTHORIZED, actualException.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(actualException.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(actualException.getReason()));
    }

    @Test
    void hasRoleReadByKeyResultIdShouldPassThroughWhenPermitted() {
        Long id = 13L;
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        OkrResponseStatusException expectedException = OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ,
                "KeyResult");
        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(id), eq(authorizationUser), any()))
                .thenReturn(new Objective());

        authorizationService.hasRoleReadByKeyResultId(id, authorizationUser);
    }

    @Test
    void hasRoleReadByCheckInIdShouldThrowExceptionWhenObjectiveNotFound() {
        Long id = 13L;
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        OkrResponseStatusException expectedException = OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ,
                "Check-in");
        when(objectivePersistenceService.findObjectiveByCheckInId(eq(id), eq(authorizationUser), any()))
                .thenThrow(expectedException);

        OkrResponseStatusException actualException = assertThrows(OkrResponseStatusException.class,
                () -> authorizationService.hasRoleReadByCheckInId(id, authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_READ, "Check-in"));

        assertEquals(UNAUTHORIZED, actualException.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(actualException.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(actualException.getReason()));
    }

    @Test
    void hasRoleReadByCheckInIdShouldPassThroughWhenPermitted() {
        Long id = 13L;
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        OkrResponseStatusException expectedException = OkrResponseStatusException.of(NOT_AUTHORIZED_TO_READ,
                "Check-in");

        when(objectivePersistenceService.findObjectiveByCheckInId(eq(id), eq(authorizationUser), any()))
                .thenReturn(new Objective());

        authorizationService.hasRoleReadByCheckInId(id, authorizationUser);
    }

    @Test
    void hasRoleCreateOrUpdateShouldPassThroughWhenAuthorizedForAllObjectives() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);

        authorizationService.hasRoleCreateOrUpdate(objective, authorizationUser);
    }

    @Test
    void hasRoleCreateOrUpdateShouldThrowExceptionWhenMember() {
        var id = memberTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> authorizationService.hasRoleCreateOrUpdate(objective, authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_WRITE, "Objective"));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void hasRoleCreateOrUpdateShouldPassThroughWhenAuthorizedForKeyResults() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(1L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);

        when(objectivePersistenceService.findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser),
                any())).thenReturn(objective);

        authorizationService.hasRoleCreateOrUpdate(keyResult, authorizationUser);
    }

    @Test
    void hasRoleCreateOrUpdateShouldPassThroughWhenAuthorizedAsAdminForKeyResults() {
        var id = adminTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser),
                any())).thenReturn(objective);

        authorizationService.hasRoleCreateOrUpdate(keyResult, authorizationUser);
    }

    @Test
    void hasRoleCreateOrUpdateShouldThorwExceptionWhenNotAuthorizedForKeyResults() {
        var id = otherTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser),
                any())).thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> authorizationService.hasRoleCreateOrUpdate(keyResult, authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_WRITE, "KeyResult"));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void hasRoleCreateOrUpdateShouldPassThroughWhenAuthorizedAsAdminForTeamCheckIns() {
        var id = adminTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getObjective().getId()),
                eq(authorizationUser), any())).thenReturn(objective);

        authorizationService.hasRoleCreateOrUpdate(checkIn, authorizationUser);
    }

    @Test
    void hasRoleCreateOrUpdateShouldThrowExceptionWhenMemberForTeamCheckIns() {
        var id = memberTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getObjective().getId()),
                eq(authorizationUser), any())).thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> authorizationService.hasRoleCreateOrUpdate(checkIn, authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_WRITE, "Check-in"));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }

    @Test
    void hasRoleCreateOrUpdateByObjectiveIdShouldPassThroughWhenAuthorizedForAllObjectives() {
        Long id = 13L;
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);

        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any())).thenReturn(objective);

        authorizationService.hasRoleCreateOrUpdateByObjectiveId(id, authorizationUser);
    }

    @Test
    void hasRoleCreateOrUpdateByObjectiveIdShouldPassThroughWhenAuthorizedAsAdmin() {
        var id = adminTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any())).thenReturn(objective);

        authorizationService.hasRoleCreateOrUpdateByObjectiveId(id, authorizationUser);
    }

    @Test
    void hasRoleCreateOrUpdateByObjectiveIdShouldThrowExceptionWhenAuthorizedAsMember() {
        var id = memberTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any())).thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> authorizationService.hasRoleCreateOrUpdateByObjectiveId(id, authorizationUser));

        List<ErrorDto> expectedErrors = List.of(ErrorDto.of(NOT_AUTHORIZED_TO_WRITE, "Objective"));

        assertEquals(UNAUTHORIZED, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));

    }

    @Test
    void hasRoleWriteForTeamShouldReturnTrueWhenAuthorizedToWriteAllObjectives() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);

        assertTrue(authorizationService.hasRoleWriteForTeam(objective, authorizationUser));
    }

    @Test
    void hasRoleWriteForTeamShouldReturnTrueWhenAuthorizedAsAdmin() {
        var id = adminTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertTrue(authorizationService.hasRoleWriteForTeam(objective, authorizationUser));
    }

    @Test
    void hasRoleWriteForTeamShouldReturnFalseWhenAuthorizedAsMember() {
        var id = memberTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        assertFalse(authorizationService.hasRoleWriteForTeam(objective, authorizationUser));
    }

    @Test
    void hasRoleWriteForTeamShouldReturnTrueWhenAuthorizedToWriteAllKeyResults() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(4L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);

        when(objectivePersistenceService.findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser),
                any())).thenReturn(objective);

        assertTrue(authorizationService.hasRoleWriteForTeam(keyResult, authorizationUser));
    }

    @Test
    void hasRoleWriteForTeamShouldReturnTrueWhenAuthorizedAsAdminForKeyResults() {
        var id = adminTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser),
                any())).thenReturn(objective);

        assertTrue(authorizationService.hasRoleWriteForTeam(keyResult, authorizationUser));
    }

    @Test
    void hasRoleWriteForTeamShouldReturnFalseWhenNotAuthorizedToWriteKeyResults() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        AuthorizationUser authorizationUser = mockAuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveById(eq(keyResult.getObjective().getId()), eq(authorizationUser),
                any())).thenReturn(objective);

        assertFalse(authorizationService.hasRoleWriteForTeam(keyResult, authorizationUser));
    }

    @Test
    void hasRoleWriteForTeamShouldReturnTrueWhenAuthorizedToWriteAllCheckIns() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);

        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getId()),
                eq(authorizationUser), any())).thenReturn(objective);

        assertTrue(authorizationService.hasRoleWriteForTeam(checkIn, authorizationUser));
    }

    @Test
    void hasRoleWriteForTeamShouldReturnTrueWhenAuthorizedAsAdminForCheckIns() {
        var id = adminTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(id).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getId()),
                eq(authorizationUser), any())).thenReturn(objective);

        assertTrue(authorizationService.hasRoleWriteForTeam(checkIn, authorizationUser));
    }

    @Test
    void hasRoleWriteForTeamShouldReturnFalseWhenNotAuthorizedToWriteCheckIns() {
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        KeyResult keyResult = KeyResultMetric.Builder.builder().withObjective(objective).build();
        CheckIn checkIn = CheckInMetric.Builder.builder().withKeyResult(keyResult).build();
        AuthorizationUser authorizationUser = mockAuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(checkIn.getKeyResult().getId()),
                eq(authorizationUser), any())).thenReturn(objective);

        assertFalse(authorizationService.hasRoleWriteForTeam(checkIn, authorizationUser));
    }

    @Test
    void hasRoleDeleteByObjectiveIdShouldPassThroughWhenAuthorizedForAllObjectives() {
        Long id = 13L;
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(5L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);

        when(objectivePersistenceService.findObjectiveById(eq(id), eq(authorizationUser), any())).thenReturn(objective);

        authorizationService.hasRoleDeleteByObjectiveId(id, authorizationUser);
    }

    @Test
    void hasRoleDeleteByKeyResultIdShouldPassThroughWhenAuthorizedForAllTeamsKeyResults() {
        Long id = 13L;
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(1L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(okrUser);

        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(id), eq(authorizationUser), any()))
                .thenReturn(objective);

        authorizationService.hasRoleDeleteByKeyResultId(id, authorizationUser);
    }

    @Test
    void hasRoleDeleteByCheckInIdShouldPassThroughWhenAuthorizedAsAdminForTeamCheckIns() {
        var id = adminTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(1L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveByCheckInId(eq(id), eq(authorizationUser), any()))
                .thenReturn(objective);

        authorizationService.hasRoleDeleteByCheckInId(id, authorizationUser);
    }

    @Test
    void hasRoleDeleteByCheckInIdShouldPassThroughWhenAuthorizedAsMemberForTeamCheckIns() {
        var id = memberTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(1L).build()).build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);

        when(objectivePersistenceService.findObjectiveByCheckInId(eq(id), eq(authorizationUser), any()))
                .thenReturn(objective);

        authorizationService.hasRoleDeleteByCheckInId(id, authorizationUser);
    }

    @Test
    void hasRoleDeleteByKeyResultIdShouldThrowExceptionWhenNotAuthorized() {
        var memeberId = memberTeams.get(0).getId();
        var otherId = otherTeams.get(0).getId();
        Objective objective = Objective.Builder.builder().withTeam(Team.Builder.builder().withId(memeberId).build())
                .build();
        AuthorizationUser authorizationUser = new AuthorizationUser(user);
        when(objectivePersistenceService.findObjectiveByKeyResultId(eq(otherId), eq(authorizationUser), any()))
                .thenReturn(objective);

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> authorizationService.hasRoleDeleteByKeyResultId(otherId, authorizationUser));

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
