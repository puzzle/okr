package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ObjectiveDTO;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.ObjectiveService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ObjectiveController.class)
class ObjectiveControllerIT {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private ObjectiveService objectiveService;
    @MockBean
    private ObjectiveMapper objectiveMapper;

    static Objective objective1 = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    static Objective objective2 = Objective.Builder.builder().withId(7L).withTitle("Objective 2").build();
    static List<Objective> objectiveList = Arrays.asList(objective1, objective2);

    static ObjectiveDTO objective1Dto = new ObjectiveDTO(5L, "Objective 1", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1, 2022, "This is a description", 20.0);
    static ObjectiveDTO objective2Dto = new ObjectiveDTO(7L, "Objective 2", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1, 2022, "This is a description", 20.0);


    @BeforeEach
    void setUp() {
        // setup team mapper
        BDDMockito.given(objectiveMapper.toDto(objective1)).willReturn(objective1Dto);
        BDDMockito.given(objectiveMapper.toDto(objective2)).willReturn(objective2Dto);
    }

    @Test
    void shouldGetAllTeams() throws Exception {
        BDDMockito.given(objectiveService.getAllObjectives()).willReturn(objectiveList);

        mvc.perform(get("/api/v1/objectives").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5)))
                .andExpect(jsonPath("$[0].title", Is.is("Objective 1")))
                .andExpect(jsonPath("$[1].id", Is.is(7)))
                .andExpect(jsonPath("$[1].title", Is.is("Objective 2")))
        ;
    }

    @Test
    void shouldGetAllTeamsIfNoTeamsExists() throws Exception {
        BDDMockito.given(objectiveService.getAllObjectives()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/objectives").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)))
        ;
    }
}
