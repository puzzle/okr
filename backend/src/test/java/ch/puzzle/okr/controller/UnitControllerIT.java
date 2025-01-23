package ch.puzzle.okr.controller;

import static ch.puzzle.okr.test.TestHelper.bbtJwtToken;
import static ch.puzzle.okr.test.TestHelper.glJwtToken;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.puzzle.okr.dto.UnitDto;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import java.util.Optional;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@AutoConfigureMockMvc
@SpringIntegrationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UnitControllerIT {
    @Autowired
    private MockMvc mvc;
    private final String URL_BASE = "/api/v2/units";

    @Autowired
    private UnitPersistenceService unitPersistenceService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("pitc");
    }

    @Test
    @Order(1)
    void shouldReturnAllUnits() throws Exception {
        List<String> unitNames = List
                .of("PERCENT", "NUMBER", "CHF", "EUR", "FTE", "UNKNOWN", "TO_BE_UPDATED", "TO_BE_DELETED");
        mvc
                .perform(get(URL_BASE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(8)))
                .andExpect(jsonPath("$[*].unitName", Matchers.containsInAnyOrder(unitNames.toArray())));
    }

    @Test
    @Order(2)
    void shouldReturnAllUnitsOfOwner() throws Exception {
        List<String> unitNames = List.of("TO_BE_UPDATED", "TO_BE_DELETED");
        mvc
                .perform(get(URL_BASE + "/user")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[*].unitName", Matchers.containsInAnyOrder(unitNames.toArray())));
    }

    @Test
    @Order(3)
    void shouldReturnNewUnitWithCurrentUserAsOwner() throws Exception {
        UnitDto unitDTO = new UnitDto(null, "TestUnit", null);
        String unitJson = objectMapper.writeValueAsString(unitDTO);

        mvc
                .perform(post(URL_BASE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                        .content(unitJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.owner.email", Is.is("gl@gl.com")))
                .andExpect(jsonPath("$.unitName", Is.is("TestUnit")));
        Optional<Unit> testUnit = Optional.ofNullable(unitPersistenceService.findUnitByUnitName("TestUnit"));
        Assertions.assertTrue(testUnit.isPresent());
        Assertions.assertEquals("TestUnit", testUnit.get().getUnitName());
        Assertions.assertEquals("gl@gl.com", testUnit.get().getCreatedBy().getEmail());
    }

    @Test
    @Order(4)
    void shouldReturn401ForInvalidUserWhenCreatingUnit() throws Exception {
        UnitDto unitDTO = new UnitDto(null, "TestUnit", null);
        String unitJson = objectMapper.writeValueAsString(unitDTO);
        mvc
                .perform(post(URL_BASE)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(unitJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @Order(5)
    void shouldReturn200ForUserWhenUpdatingUnit() throws Exception {
        UnitDto unitDTO = new UnitDto(100L, "UPDATED_UNIT", null);
        String unitJson = objectMapper.writeValueAsString(unitDTO);
        mvc
                .perform(put(URL_BASE + "/100")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                        .content(unitJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.unitName", Is.is("UPDATED_UNIT")));
    }

    @Test
    @Order(6)
    void shouldReturn403ForWrongUserWhenUpdatingUnit() throws Exception {
        UnitDto unitDTO = new UnitDto(100L, "UPDATED_UNIT", null);
        String unitJson = objectMapper.writeValueAsString(unitDTO);
        mvc
                .perform(put(URL_BASE + "/100")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(bbtJwtToken()))
                        .content(unitJson)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Order(7)
    void shouldReturn403ForWrongUserWhenDeletingUnit() throws Exception {
        mvc
                .perform(delete(URL_BASE + "/101")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(bbtJwtToken()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    @Order(8)
    void shouldReturn200ForRightUserWhenDeletingUnit() throws Exception {
        mvc
                .perform(delete(URL_BASE + "/101")
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
