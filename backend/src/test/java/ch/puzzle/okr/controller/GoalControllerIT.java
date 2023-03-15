package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.goal.GoalDto;
import ch.puzzle.okr.dto.goal.GoalKeyResultDto;
import ch.puzzle.okr.dto.goal.GoalObjectiveDto;
import ch.puzzle.okr.mapper.GoalMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.service.KeyResultService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(GoalController.class)
class GoalControllerIT {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private KeyResultService keyResultService;
    @MockBean
    private GoalMapper goalMapper;

    static KeyResult keyResult1 = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1")
            .withObjective(Objective.Builder.builder().withId(1L).build()).build();

    static GoalDto goalDto1 = new GoalDto(new GoalObjectiveDto(1L, "Objective 1", "This is Objective description"),
            new GoalKeyResultDto(1L, "Keyresult 1", "This is Keyresult description"),
            Team.Builder.builder().withId(1L).withName("Puzzle").build(), 20L, "GJ 22/23-Q2",
            ExpectedEvolution.CONSTANT, Unit.PERCENT, 0D, 100D);

    @BeforeEach
    void setUp() {
        BDDMockito.given(goalMapper.toDto(keyResult1)).willReturn(goalDto1);
    }

    @Test
    void shouldGetGoalWithId() throws Exception {
        BDDMockito.given(keyResultService.getKeyResultById(1)).willReturn(keyResult1);

        mvc.perform(get("/api/v1/goals/1").contentType(MediaType.APPLICATION_JSON))
//                .andDo((goal) -> System.out.println(goal.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.objective.id", Is.is(1)))
                .andExpect(jsonPath("$.keyresult.id", Is.is(1))).andExpect(jsonPath("$.teamId", Is.is(1)))
                .andExpect(jsonPath("$.unit", Is.is("PERCENT")));
    }

    @Test
    void shouldNotFoundTheGoalWithId() throws Exception {
        BDDMockito.given(keyResultService.getKeyResultById(55))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 55 not found"));

        mvc.perform(get("/api/v1/goals/55").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(status().isNotFound());
    }
}
