package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.business.ActionBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
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
    private final KeyResult keyResult = KeyResultMetric.Builder.builder().withId(10L).withTitle("KR Title").build();
    private final Action action1 = Action.Builder.builder().withId(1L).withAction("Neue Katze").withIsChecked(false)
            .withPriority(0).withKeyResult(keyResult).build();
    private final Action action2 = Action.Builder.builder().withId(2L).withAction("Neues Lama").withIsChecked(true)
            .withPriority(1).withKeyResult(keyResult).build();
    private final List<Action> actionList = List.of(action1, action2);

    @ParameterizedTest
    @ValueSource(booleans = { true, false })
    void getActionsByKeyResultShouldReturnListOfActions(boolean isWriteable) {
        keyResult.setWriteable(isWriteable);
        when(actionBusinessService.getActionsByKeyResultId(anyLong())).thenReturn(actionList);

        List<Action> foundActionList = actionAuthorizationService.getActionsByKeyResult(action1.getKeyResult());

        assertThat(actionList).hasSameElementsAs(foundActionList);
        foundActionList.forEach(action -> assertEquals(isWriteable, action.isWriteable()));
    }

    @Test
    void createEntityShouldReturnCreatedActionWhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        action1.setWriteable(false);
        action2.setWriteable(false);

        List<Action> receivedActionList = List.of(action1, action2);

        actionAuthorizationService.createEntities(actionList);

        verify(actionBusinessService, times(1)).createEntities(receivedActionList);
    }

    @Test
    void createEntityShouldThrowExceptionWhenNotAuthorized() {
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
    void updateEntitiesShouldUpdateActionWhenAuthorized() {
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        actionAuthorizationService.updateEntities(actionList);

        verify(actionBusinessService, times(1)).updateEntities(actionList);
    }

    @Test
    void updateEntitiesShouldThrowExceptionWhenNotAuthorized() {
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
    void deleteEntityByIdShouldPassThroughWhenAuthorized() {
        Long id = 5L;
        when(authorizationService.getAuthorizationUser()).thenReturn(authorizationUser);

        actionAuthorizationService.deleteActionByActionId(id);

        verify(actionBusinessService, times(1)).deleteEntityById(id);
    }

    @Test
    void deleteEntityByIdShouldThrowExceptionWhenNotAuthorized() {
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
