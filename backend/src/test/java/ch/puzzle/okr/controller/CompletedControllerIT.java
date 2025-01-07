package ch.puzzle.okr.controller;

import static ch.puzzle.okr.test.KeyResultTestHelpers.JSON_PATH_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.puzzle.okr.dto.CompletedDto;
import ch.puzzle.okr.mapper.CompletedMapper;
import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.authorization.CompletedAuthorizationService;
import ch.puzzle.okr.test.dto.builder.CompletedDtoBuilder;
import ch.puzzle.okr.test.dto.builder.ObjectiveDtoBuilder;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CompletedController.class)
class CompletedControllerIT {
    private static final long COMPLETED_ID = 22L;
    private static final int COMPLETED_ID_AS_INT = (int) COMPLETED_ID;
    private static final long OBJECTIVE_ID = 3L;
    private static final int OBJECTIVE_ID_AS_INT = (int) OBJECTIVE_ID;
    private static final String COMPLETED_COMMENT = "Wir haben es gut geschafft";

    private static final String SUCCESSFUL_CREATE_BODY = """
            {
                "id":null,
                "objectiveId":%d,
                "comment":"%s"
            }
            """.formatted(OBJECTIVE_ID, COMPLETED_COMMENT);

    @MockitoBean
    CompletedAuthorizationService completedAuthorizationService;
    @MockitoBean
    private CompletedMapper completedMapper;

    private final Completed successfulCompleted = Completed.Builder
            .builder() //
            .withId(COMPLETED_ID) //
            .withObjective(Objective.Builder
                    .builder() //
                    .withId(OBJECTIVE_ID) //
                    .build()) //
            .withComment(COMPLETED_COMMENT) //
            .build();

    private final CompletedDto completedDto = CompletedDtoBuilder
            .builder() //
            .withId(COMPLETED_ID) //
            .withComment(COMPLETED_COMMENT) //
            .withObjectiveDto(ObjectiveDtoBuilder
                    .builder() //
                    .withId(OBJECTIVE_ID) //
                    .build()) //
            .build();

    String baseUrl = "/api/v2/completed";
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        BDDMockito.given(completedMapper.toDto(any())).willReturn(completedDto);
        BDDMockito.given(completedMapper.toCompleted(any())).willReturn(successfulCompleted);
    }

    @DisplayName("Should create a completed-objective")
    @Test
    void createShouldCreateCompleted() throws Exception {
        BDDMockito.given(this.completedAuthorizationService.createCompleted(any())).willReturn(successfulCompleted);

        mvc
                .perform(post(baseUrl) //
                        .content(SUCCESSFUL_CREATE_BODY) //
                        .contentType(MediaType.APPLICATION_JSON) //
                        .with(SecurityMockMvcRequestPostProcessors.csrf())) //
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()) //
                .andExpect(jsonPath(JSON_PATH_ID, Is.is(COMPLETED_ID_AS_INT))) //
                .andExpect(jsonPath("$.id", Is.is(COMPLETED_ID_AS_INT))) //
                .andExpect(jsonPath("$.objective.id", Is.is(OBJECTIVE_ID_AS_INT))) //
                .andExpect(jsonPath("$.comment", Is.is(COMPLETED_COMMENT)));
    }

    @DisplayName("Should delete a completed-objective")
    @Test
    void deleteShouldDeleteCompleted() throws Exception {
        mvc
                .perform(delete("/api/v2/completed/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @DisplayName("Should throw not found exception when a completed-objective with non existent id can not be found")
    @Test
    void deleteShouldThrowExceptionWhenCompletedWithIdCantBeFound() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Completed not found"))
                .when(completedAuthorizationService)
                .deleteCompletedByObjectiveId(anyLong());

        mvc
                .perform(delete("/api/v2/completed/1000").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
