package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class ObjectiveAuthorizationServiceTest {
    @InjectMocks
    private ObjectiveAuthorizationService objectiveAuthorizationService;
    @Mock
    ObjectiveBusinessService objectiveBusinessService;
    @Mock
    AuthorizationService authorizationService;
    private final Jwt token = defaultJwtToken();
    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    private final Objective newObjective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();

    @Test
    void createEntity_ShouldReturnObjective_WhenAuthorized() {
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(objectiveBusinessService.createEntity(newObjective, authorizationUser)).thenReturn(newObjective);

        Objective objective = objectiveAuthorizationService.createEntity(newObjective, token);
        assertEquals(newObjective, objective);
    }

    @Test
    void createEntity_ShouldThrowException_WhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(newObjective, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.createEntity(newObjective, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void getEntityById_ShouldReturnObjective_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(objectiveBusinessService.getEntityById(id)).thenReturn(newObjective);

        Objective Objective = objectiveAuthorizationService.getEntityById(id, token);
        assertEquals(newObjective, Objective);
    }

    @Test
    void getEntityById_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleReadByObjectiveId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.getEntityById(id, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntity_ShouldReturnUpdatedObjective_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        when(objectiveBusinessService.updateEntity(id, newObjective, authorizationUser)).thenReturn(newObjective);

        Objective Objective = objectiveAuthorizationService.updateEntity(id, newObjective, token);
        assertEquals(newObjective, Objective);
    }

    @Test
    void updateEntity_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(newObjective, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.updateEntity(id, newObjective, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void deleteEntityById_ShouldPassThrough_WhenAuthorized() {
        Long id = 13L;
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);

        objectiveAuthorizationService.deleteEntityById(id, token);
    }

    @Test
    void deleteEntityById_ShouldThrowException_WhenNotAuthorized() {
        Long id = 13L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser(token)).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByObjectiveId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> objectiveAuthorizationService.deleteEntityById(id, token));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }
}
