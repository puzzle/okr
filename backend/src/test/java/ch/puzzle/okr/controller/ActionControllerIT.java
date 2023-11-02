package ch.puzzle.okr.controller;

import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.authorization.ActionAuthorizationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ActionController.class)
class ActionControllerIT {
    public static final String SUCCESSFUL_UPDATE_BODY = """
            [
                {
                    "id":1,
                    "action":"Neuer Drucker",
                    "priority":0,
                    "isChecked":true,
                    "keyResultId":8
                },
                {
                    "id":2,
                    "action":"Neues Papier",
                    "priority":1,
                    "isChecked":false,
                    "keyResultId":8
                }
            ]
            """;
    public static final String SUCCESSFUL_UPDATE_BODY_SINGLE_ACTION = """
            [
                {
                    "id":1,
                    "action":"Neuer Drucker",
                    "priority":0,
                    "isChecked":true,
                    "keyResultId":8
                }
            ]
            """;
    @MockBean
    ActionAuthorizationService actionAuthorizationService;
    @MockBean
    ActionMapper actionMapper;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        Action action = Action.Builder.builder().withId(3L).withAction("Neues Haus").withPriority(1).withIsChecked(true)
                .withKeyResult(KeyResultMetric.Builder.builder().withId(10L).withTitle("KR Title").build()).build();
        BDDMockito.given(actionMapper.toActions(any())).willReturn(List.of(action, action));
    }

    @Test
    void updateSuccessfulActions() throws Exception {
        mvc.perform(put("/api/v2/action").content(SUCCESSFUL_UPDATE_BODY).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(actionMapper, times(1)).toActions(any());
        verify(actionAuthorizationService, times(1)).updateEntities(any());
    }

    @Test
    void updateSuccessfulOnlyOneAction() throws Exception {
        mvc.perform(put("/api/v2/action").content(SUCCESSFUL_UPDATE_BODY_SINGLE_ACTION)
                .contentType(MediaType.APPLICATION_JSON).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());

        verify(actionMapper, times(1)).toActions(any());
        verify(actionAuthorizationService, times(1)).updateEntities(any());
    }

    @Test
    void shouldDeleteAction() throws Exception {
        mvc.perform(delete("/api/v2/action/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void throwExceptionWhenActionWithIdCantBeFoundWhileDeleting() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Action not found")).when(actionAuthorizationService)
                .deleteActionByActionId(anyLong());

        mvc.perform(delete("/api/v2/action/1000").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
