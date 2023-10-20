package ch.puzzle.okr.controller;

import ch.puzzle.okr.service.ClientConfigService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClientConfigController.class)
class ClientConfigControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private ClientConfigService clientConfigService;

    @Test
    void shouldEmptyUsersCache() throws Exception {
        mvc.perform(get("/config").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful());
        verify(clientConfigService, times(1)).getConfigBasedOnActiveEnv();
    }

}