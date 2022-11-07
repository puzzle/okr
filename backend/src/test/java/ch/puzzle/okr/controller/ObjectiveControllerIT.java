package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.ObjectiveDTO;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.*;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ObjectiveController.class)
class ObjectiveControllerIT {

    static Objective objective1 = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    static Objective objective2 = Objective.Builder.builder().withId(7L).withTitle("Objective 2").build();
    static List<Objective> objectiveList = Arrays.asList(objective1, objective2);
    static ObjectiveDTO objective1Dto = new ObjectiveDTO(5L, "Objective 1", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1, 2022, "This is a description", 20.0);
    static ObjectiveDTO objective2Dto = new ObjectiveDTO(7L, "Objective 2", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1, 2022, "This is a description", 20.0);
    static KeyResult keyResult1 = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1").build();
    static KeyResult keyResult2 = KeyResult.Builder.builder().withId(7L).withTitle("Keyresult 2").build();
    static List<KeyResult> keyResultList = Arrays.asList(keyResult1, keyResult2);
    static KeyResultDto keyresult1Dto = new KeyResultDto(5L, 1L, "Keyresult 1", "Description", 1L, "Alice", "Wunderland", 1L, 1, 2022, ExpectedEvolution.CONSTANT, Unit.PERCENT, 20L, 100L);
    static KeyResultDto keyresult2Dto = new KeyResultDto(7L, 1L, "Keyresult 2", "Description", 1L, "Alice", "Wunderland", 1L, 1, 2022, ExpectedEvolution.CONSTANT, Unit.PERCENT, 20L, 100L);

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ObjectiveService objectiveService;
    @MockBean
    private ObjectiveMapper objectiveMapper;
    @MockBean
    private KeyResultMapper keyResultMapper;

    @BeforeEach
    void setUp() {
        // setup objective mapper
        BDDMockito.given(objectiveMapper.toDto(objective1)).willReturn(objective1Dto);
        BDDMockito.given(objectiveMapper.toDto(objective2)).willReturn(objective2Dto);
        BDDMockito.given(keyResultMapper.toDto(keyResult1)).willReturn(keyresult1Dto);
        BDDMockito.given(keyResultMapper.toDto(keyResult2)).willReturn(keyresult2Dto);
    }

    @Test
    void shouldGetAllObjectives() throws Exception {
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
    void shouldGetAllObjectivesIfNoObjectiveExists() throws Exception {
        BDDMockito.given(objectiveService.getAllObjectives()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/objectives").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)))
        ;
    }

    @Test
    void shouldGetAllKeyResultsByObjective() throws Exception {
        BDDMockito.given(objectiveService.getAllKeyResultsByObjective(1)).willReturn(keyResultList);

        mvc.perform(get("/api/v1/objectives/1/keyresults").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5)))
                .andExpect(jsonPath("$[0].title", Is.is("Keyresult 1")))
                .andExpect(jsonPath("$[1].id", Is.is(7)))
                .andExpect(jsonPath("$[1].title", Is.is("Keyresult 2")))
        ;
    }

    @Test
    void shouldGetAllKeyResultsIfNoKeyResultExistsInObjective() throws Exception {
        BDDMockito.given(objectiveService.getAllKeyResultsByObjective(1)).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/objectives/1/keyresults").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)))
        ;
    }

    @Test
    void shouldReturnErrorWhenObjectiveDoesntExistWhenGettingKeyResults() throws Exception {
        BDDMockito.given(objectiveService.getAllKeyResultsByObjective(1))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective with id 1 not found"));

        mvc.perform(get("/api/v1/objectives/1/keyresults").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(status().isNotFound())
        ;
    }

    @Test
    void getObjective() throws Exception {
        BDDMockito.given(objectiveService.getObjective(5L)).willReturn(objective1);

        mvc.perform(get("/api/v1/objectives/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.title", Is.is("Objective 1")))
        ;
    }

    @Test
    void getObjectiveFail() throws Exception {
        BDDMockito.given(objectiveService.getObjective(10L)).willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mvc.perform(get("/api/v1/objectives/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
        ;
    }
}
