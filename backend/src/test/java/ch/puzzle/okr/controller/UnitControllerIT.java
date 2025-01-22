package ch.puzzle.okr.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WithMockUser
@AutoConfigureMockMvc
@SpringIntegrationTest
class UnitControllerIT {
    @Autowired
    private MockMvc mvc;
    private final String URL_BASE = "/api/v2/unit";
    String CREATE_UNIT_BODY = "{\"unitName\":\"TestUnit\"}";

    @Autowired
    private UnitPersistenceService unitPersistenceService;

    @Test
    void test() throws Exception {
        Jwt jwt = TestHelper.mockJwtToken("Jaya", "Norris", "gl@gl.com");

        TenantContext.setCurrentTenant("pitc");
        mvc
                .perform(post(URL_BASE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt))
                        .content(CREATE_UNIT_BODY)
                        .contentType("application/json"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.owner.email", Is.is("gl@gl.com")));
    }
}
