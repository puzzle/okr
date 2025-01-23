package ch.puzzle.okr.controller;

import static ch.puzzle.okr.test.TestHelper.glUser;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import java.util.Optional;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringIntegrationTest
class UnitControllerIT {
    @Autowired
    private MockMvc mvc;
    private final String URL_BASE = "/api/v2/unit";
    String CREATE_UNIT_BODY = "{\"unitName\":\"TestUnit\"}";

    @Autowired
    private UnitPersistenceService unitPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("pitc");
    }

    @Test
    void shouldReturnNewUnitWithCurrentUserAsOwner() throws Exception {
        Jwt jwt = TestHelper.mockJwtToken(glUser());
        mvc
                .perform(post(URL_BASE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(jwt))
                        .content(CREATE_UNIT_BODY)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.owner.email", Is.is("gl@gl.com")))
                .andExpect(jsonPath("$.unitName", Is.is("TestUnit")));
        Optional<Unit> testUnit = unitPersistenceService.findUnitByUnitName("TestUnit");
        Assertions.assertTrue(testUnit.isPresent());
        Assertions.assertEquals("TestUnit", testUnit.get().getUnitName());
        Assertions.assertEquals("gl@gl.com", testUnit.get().getCreatedBy().getEmail());
    }

    @Test
    void shouldReturn401ForInvalidUserWhenCreatingUnit() throws Exception {
        mvc
                .perform(post(URL_BASE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(CREATE_UNIT_BODY)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    void shouldReturn200ForUserWhenUpdatingUnit() throws Exception {
        mvc
                .perform(put(URL_BASE)
                                 .with(SecurityMockMvcRequestPostProcessors.csrf())
                                 .content(CREATE_UNIT_BODY)
                                 .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldReturn403ForWrongUserWhenUpdatingUnit() throws Exception {
        mvc
                .perform(put(URL_BASE)
                                 .with(SecurityMockMvcRequestPostProcessors.csrf())
                                 .content(CREATE_UNIT_BODY)
                                 .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        Optional<Unit> testUnit = unitPersistenceService.findUnitByUnitName("TestUnit");
        Assertions.assertTrue(testUnit.isEmpty());
    }
}
