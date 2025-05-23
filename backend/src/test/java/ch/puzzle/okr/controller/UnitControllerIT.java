package ch.puzzle.okr.controller;

import static ch.puzzle.okr.test.TestHelper.*;
import static ch.puzzle.okr.test.TestHelper.FTE_UNIT;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.puzzle.okr.dto.UnitDto;
import ch.puzzle.okr.mapper.UnitMapper;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.authorization.AuthorizationService;
import ch.puzzle.okr.service.authorization.UnitAuthorizationService;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

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
                .of("PROZENT", "ZAHL", "CHF", "EUR", "FTE", "UNKNOWN", "TO_BE_UPDATED", "TO_BE_DELETED");
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
        UnitDto unitDTO = new UnitDto(null, "TestUnit", null, false);
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
        UnitDto unitDTO = new UnitDto(null, "TestUnit", null, false);
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
        UnitDto unitDTO = new UnitDto(100L, "UPDATED_UNIT", null, false);
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
        UnitDto unitDTO = new UnitDto(100L, "UPDATED_UNIT", null, false);
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

    @Nested
    @DisplayName("MockedUnitControllerIT")
    class MockedUnitControllerIT {
        private final String URL_BASE = "/api/v2/units";
        private final ObjectMapper objectMapper = new ObjectMapper();
        @Autowired
        private MockMvc mvc;
        @MockitoBean
        private UnitAuthorizationService unitAuthorizationService;
        @MockitoBean
        private AuthorizationService authorizationService;
        @MockitoBean
        private UnitMapper unitMapper;

        @BeforeEach
        void setUp() {
            TenantContext.setCurrentTenant("pitc");
            when(unitMapper.toDto(any(Unit.class))).thenAnswer(invocation -> {
                Unit unit = invocation.getArgument(0);
                return new UnitDto(unit.getId(), unit.getUnitName(), glUserDto(), false);
            });

            when(unitMapper.toUnit(any(UnitDto.class))).thenAnswer(invocation -> {
                UnitDto unit = invocation.getArgument(0);
                return Unit.Builder.builder().id(unit.id()).unitName(unit.unitName()).createdBy(glUser()).build();
            });
        }

        @Test
        void shouldReturnAllUnits() throws Exception {
            when(unitAuthorizationService.getAllUnits()).thenReturn(List.of(FTE_UNIT, PERCENT_UNIT, NUMBER_UNIT));
            List<String> unitNames = List.of("PERCENT", "NUMBER", "FTE");
            mvc
                    .perform(get(URL_BASE).contentType(MediaType.APPLICATION_JSON)
                                     .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                            )
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$", Matchers.hasSize(3)))
                    .andExpect(jsonPath("$[*].unitName", Matchers.containsInAnyOrder(unitNames.toArray())));
        }

        @Test
        void shouldReturnAllUnitsOfOwner() throws Exception {
            when(unitAuthorizationService.getUnitsOfUser()).thenReturn(List.of(FTE_UNIT, PERCENT_UNIT, NUMBER_UNIT));
            List<String> unitNames = List.of("PERCENT", "NUMBER", "FTE");
            mvc
                    .perform(get(URL_BASE + "/user").contentType(MediaType.APPLICATION_JSON)
                                     .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                            )

                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$", Matchers.hasSize(3)))
                    .andExpect(jsonPath("$[*].unitName", Matchers.containsInAnyOrder(unitNames.toArray())));
        }

        @Test
        void shouldReturnNewUnitWithCurrentUserAsOwner() throws Exception {
            UnitDto unitDTO = new UnitDto(null, "TestUnit", null, false);
            String unitJson = objectMapper.writeValueAsString(unitDTO);
            when(unitAuthorizationService.createUnit(any(Unit.class))).thenReturn(FTE_UNIT);

            mvc
                    .perform(post(URL_BASE)
                            .content(unitJson)
                            .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.owner.email", Is.is("gl@gl.com")))
                    .andExpect(jsonPath("$.unitName", Is.is("FTE")));
        }

        @Test
        void shouldReturn401ForInvalidUserWhenCreatingUnit() throws Exception {
            UnitDto unitDTO = new UnitDto(null, "TestUnit", null, false);
            String unitJson = objectMapper.writeValueAsString(unitDTO);
            when(unitAuthorizationService.createUnit(any(Unit.class)))
                    .thenThrow(new ResponseStatusException(HttpStatus.UNAUTHORIZED));
            mvc
                    .perform(post(URL_BASE)
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .content(unitJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        }

        @Test
        void shouldReturn200ForUserWhenUpdatingUnit() throws Exception {
            UnitDto unitDTO = new UnitDto(null, "TestUnit", null, false);
            String unitJson = objectMapper.writeValueAsString(unitDTO);
            when(unitAuthorizationService.updateUnit(any(), any(Unit.class))).thenReturn(FTE_UNIT);

            mvc
                    .perform(put(URL_BASE + "/100")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                            .content(unitJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk())
                    .andExpect(jsonPath("$.unitName", Is.is("FTE")));
        }

        @Test
        void shouldReturn403ForWrongUserWhenUpdatingUnit() throws Exception {
            UnitDto unitDTO = new UnitDto(null, "TestUnit", null, false);
            String unitJson = objectMapper.writeValueAsString(unitDTO);
            when(unitAuthorizationService.updateUnit(any(), any(Unit.class)))
                    .thenThrow(new ResponseStatusException(HttpStatus.FORBIDDEN));
            mvc
                    .perform(put(URL_BASE + "/100")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(bbtJwtToken()))
                            .content(unitJson)
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        void shouldReturn403ForWrongUserWhenDeletingUnit() throws Exception {
            doThrow(new ResponseStatusException(HttpStatus.FORBIDDEN))
                    .when(unitAuthorizationService)
                    .deleteUnitById(101L);
            mvc
                    .perform(delete(URL_BASE + "/101")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(bbtJwtToken()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isForbidden());
        }

        @Test
        void shouldReturn200ForRightUserWhenDeletingUnit() throws Exception {
            mvc
                    .perform(delete(URL_BASE + "/101")
                            .with(SecurityMockMvcRequestPostProcessors.csrf())
                            .with(SecurityMockMvcRequestPostProcessors.jwt().jwt(glJwtToken()))
                            .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(MockMvcResultMatchers.status().isOk());
            verify(unitAuthorizationService, times(1)).deleteUnitById(101L);
        }
    }
}
