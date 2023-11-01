package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.business.ActionBusinessService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ExtendWith(MockitoExtension.class)
class ActionAuthorizationServiceTest {
    @InjectMocks
    private ActionAuthorizationService actionAuthorizationService;
    @Mock
    ActionBusinessService actionBusinessService;
    @Mock
    AuthorizationService authorizationService;

    private final AuthorizationUser authorizationUser = defaultAuthorizationUser();

    Action action1;
    Action action2;
    List<Action> actionList = new ArrayList<>();

    @BeforeEach
    void setup() {
        this.action1 = Action.Builder.builder().withId(1L).withAction("Neue Katze").withIsChecked(false).withPriority(0)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(10L).withTitle("KR Title").build()).build();
        this.action2 = Action.Builder.builder().withId(2L).withAction("Neues Lama").withIsChecked(true).withPriority(1)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(10L).withTitle("KR Title").build()).build();
        this.actionList = List.of(action1, action2);
    }

    @Test
    void getEntityById_ShouldReturnActions_WhenAuthorized() {
        Long keyResultId = 10L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(actionBusinessService.getActionsByKeyResultId(keyResultId)).thenReturn(actionList);

        List<Action> actions = actionAuthorizationService.getEntitiesByKeyResultId(keyResultId);

        assertEquals(actionList, actions);
    }

    @Test
    void getEntityById_ShouldReturnActionWritable_WhenAuthorized() {
        Long keyResultId = 10L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        when(actionBusinessService.getActionsByKeyResultId(keyResultId)).thenReturn(actionList);
        when(authorizationService.isWriteable((KeyResult) any(), any())).thenReturn(true);

        List<Action> actions = actionAuthorizationService.getEntitiesByKeyResultId(keyResultId);

        actions.forEach(action -> {
            assertTrue(action.isWriteable());
        });
    }

    @Test
    void getEntityById_ShouldThrowException_WhenNotAuthorized() {
        Long keyResultId = 10L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleReadByKeyResultId(keyResultId, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> actionAuthorizationService.getEntitiesByKeyResultId(keyResultId));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void createEntity_ShouldReturnCreatedAction_WhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        action1.setWriteable(false);
        action2.setWriteable(false);

        List<Action> receivedActionList = List.of(action1, action2);

        actionAuthorizationService.createEntities(actionList);

        verify(actionBusinessService, times(1)).createEntities(receivedActionList);
    }

    @Test
    void createEntity_ShouldThrowException_WhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(actionList.get(0).getKeyResult(), authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> actionAuthorizationService.createEntities(actionList));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void updateEntity_ShouldUpdateAction_WhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        actionAuthorizationService.updateEntities(actionList);

        verify(actionBusinessService, times(1)).updateEntities(actionList);
    }

    @Test
    void updateEntity_ShouldThrowException_WhenNotAuthorized() {
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleCreateOrUpdate(actionList.get(0).getKeyResult(), authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> actionAuthorizationService.updateEntities(actionList));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }

    @Test
    void deleteEntityById_ShouldPassThrough_WhenAuthorized() {
        Long id = 5L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        actionAuthorizationService.deleteActionByActionId(id);

        verify(actionBusinessService, times(1)).deleteEntityById(id);
    }

    @Test
    void deleteEntityById_ShouldThrowException_WhenNotAuthorized() {
        Long id = 8L;
        String reason = "junit test reason";
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);
        doThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED, reason)).when(authorizationService)
                .hasRoleDeleteByActionId(id, authorizationUser);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> actionAuthorizationService.deleteActionByActionId(id));
        assertEquals(UNAUTHORIZED, exception.getStatus());
        assertEquals(reason, exception.getReason());
    }
}
