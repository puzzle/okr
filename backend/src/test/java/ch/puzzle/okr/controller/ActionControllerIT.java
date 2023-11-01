package ch.puzzle.okr.controller;

import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.service.authorization.ActionAuthorizationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ActionController.class)
class ActionControllerIT {
    public static final String SUCCESSFUL_UPDATE_BODY = """
            {
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

            }
            """;
    @MockBean
    ActionAuthorizationService actionAuthorizationService;
    @MockBean
    ActionMapper actionMapper;
    String baseUrl = "/api/v2/action";
    @Autowired
    private MockMvc mvc;

    @Test
    void updateSuccessfulAction() throws Exception {
        mvc.perform(post(baseUrl).content(SUCCESSFUL_UPDATE_BODY).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
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
