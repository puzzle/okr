package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.CompletedDto;
import ch.puzzle.okr.mapper.CompletedMapper;
import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.authorization.CompletedAuthorizationService;
import org.hamcrest.core.Is;
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

import static ch.puzzle.okr.KeyResultTestHelpers.JSON_PATH_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CompletedController.class)
class CompletedControllerIT {
    public static final String SUCCESSFUL_CREATE_BODY = """
            {
                "id":null,
                "objectiveId":3,
                "comment":"Wir haben es gut geschafft"
            }
            """;
    @MockBean
    CompletedAuthorizationService completedAuthorizationService;
    @MockBean
    private CompletedMapper completedMapper;
    private static final String WELL_DONE = "Wir haben es gut geschafft";

    Completed successfulCompleted = Completed.Builder.builder().withId(1L)
            .withObjective(Objective.Builder.builder().withId(3L).withTitle("Gute Lernende").build())
            .withComment(WELL_DONE).build();
    CompletedDto completedDto = new CompletedDto(22L,
            Objective.Builder.builder().withId(3L).withTitle("Gute Lernende").build(), WELL_DONE);
    String baseUrl = "/api/v2/completed";
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        BDDMockito.given(completedMapper.toDto(any())).willReturn(completedDto);
        BDDMockito.given(completedMapper.toCompleted(any())).willReturn(successfulCompleted);
    }

    @Test
    void createSuccessfulCompleted() throws Exception {
        BDDMockito.given(this.completedAuthorizationService.createCompleted(any())).willReturn(successfulCompleted);

        mvc.perform(post(baseUrl).content(SUCCESSFUL_CREATE_BODY).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath(JSON_PATH_ID, Is.is(22))).andExpect(jsonPath("$.objective.id", Is.is(3)))
                .andExpect(jsonPath("$.comment", Is.is(WELL_DONE)));
    }

    @Test
    void shouldDeleteCompleted() throws Exception {
        mvc.perform(delete("/api/v2/completed/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void throwExceptionWhenCompletedWithIdCantBeFoundWhileDeleting() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Completed not found"))
                .when(completedAuthorizationService).deleteCompletedByObjectiveId(anyLong());

        mvc.perform(delete("/api/v2/completed/1000").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
