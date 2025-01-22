package ch.puzzle.okr.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.persistence.PersistenceBase;
import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import org.hamcrest.core.Is;
import org.hibernate.validator.internal.constraintvalidators.bv.AssertTrueValidator;
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

import java.util.Optional;

@AutoConfigureMockMvc
@SpringIntegrationTest
class UnitControllerIT {
    @Autowired
    private MockMvc mvc;
    private final String URL_BASE = "/api/v2/unit";
    String CREATE_UNIT_BODY = "{\"unitName\":\"TestUnit\"}";
    private final User GL_USER = TestHelper.glUser();

    @Autowired
    private UnitPersistenceService unitPersistenceService;

    @BeforeEach
    void setUp(){
        TenantContext.setCurrentTenant("pitc");
    }

    @Test
    void test() throws Exception {
        Jwt jwt = TestHelper.mockJwtToken(GL_USER);
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
}
