package ch.puzzle.okr.controller;

import static ch.puzzle.okr.controller.OverviewControllerIT.JSON_PATH_ROOT;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.service.clientconfig.ClientConfigService;
import java.util.Map;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ClientConfigController.class)
public class ClientConfigControllerIT {

    @Autowired
    private MockMvc mvc;
    @MockitoBean
    private ClientConfigService configService;

    @DisplayName("Should get client config with correct values")
    @Test
    void shouldGetClientConfig() throws Exception {
        BDDMockito.given(configService.getConfigBasedOnActiveEnv(anyString())).willReturn(createClientConfigDto());

        mvc
                .perform(get("/config").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT, Matchers.aMapWithSize(10)))
                .andExpect(jsonPath("$.activeProfile", Matchers.is("Active_Profile")))
                .andExpect(jsonPath("$.issuer", Matchers.is("Issuer")))
                .andExpect(jsonPath("$.clientId", Matchers.is("Client_Id")))
                .andExpect(jsonPath("$.favicon", Matchers.is("Favicon")))
                .andExpect(jsonPath("$.logo", Matchers.is("Logo")))
                .andExpect(jsonPath("$.triangles", Matchers.is("Triangles")))
                .andExpect(jsonPath("$.backgroundLogo", Matchers.is("Background_Logo")))
                .andExpect(jsonPath("$.title", Matchers.is("Title")))
                .andExpect(jsonPath("$.helpSiteUrl", Matchers.is("Help_Site_Url")))
                .andExpect(jsonPath("$.customStyles.font-family", Matchers.is("verdana")))
                .andExpect(jsonPath("$.customStyles.font-size", Matchers.is("20px")));
    }

    private ClientConfigDto createClientConfigDto() {
        Map<String, String> customStyles = Map.of("font-family", "verdana", "font-size", "20px");
        return new ClientConfigDto("Active_Profile",
                                   "Issuer",
                                   "Client_Id",
                                   "Favicon",
                                   "Logo",
                                   "Triangles",
                                   "Background_Logo",
                                   "Title",
                                   "Help_Site_Url",
                                   customStyles);
    }

}
