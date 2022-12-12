package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.service.KeyResultService;
import ch.puzzle.okr.service.ObjectiveService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(ObjectiveController.class)
class ObjectiveControllerIT {

    static Objective objective1 = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    static Objective objective2 = Objective.Builder.builder().withId(7L).withTitle("Objective 2").build();
    static User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
            .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
    static Team team = Team.Builder.builder().withId(1L).withName("Team1").build();
    static Quarter quarter = Quarter.Builder.builder().withId(1L).withNumber(3).withYear(2020).build();
    static Objective fullObjective = Objective.Builder.builder().withId(42L).withTitle("FullObjective").withOwner(user)
            .withTeam(team).withQuarter(quarter).withDescription("This is our description").withProgress(33L)
            .withCreatedOn(LocalDateTime.MAX).build();
    static List<Objective> objectiveList = Arrays.asList(objective1, objective2);
    static ObjectiveDto objective1Dto = new ObjectiveDto(5L, "Objective 1", 1L, "Alice", "Wunderland", 1L, "Puzzle", 2L,
            1, 2022, "This is a description", 20L);
    static ObjectiveDto objective2Dto = new ObjectiveDto(7L, "Objective 2", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1L,
            1, 2022, "This is a description", 20L);
    static KeyResult keyResult1 = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1").build();
    static KeyResult keyResult2 = KeyResult.Builder.builder().withId(7L).withTitle("Keyresult 2").build();
    static MeasureDto measure1Dto = new MeasureDto(1L, 5L, 10, "foo", "boo", 1L, null);
    static MeasureDto measure2Dto = new MeasureDto(2L, 7L, 10, "foo", "boo", 1L, null);
    static List<KeyResultMeasureDto> keyResultsMeasureList = List.of(
            new KeyResultMeasureDto(5L, 1L, "Keyresult 1", "Description", 1L, "Paco", "Egiman", 4L, 1, 2022,
                    ExpectedEvolution.CONSTANT, Unit.PERCENT, 20L, 100L, measure1Dto),
            new KeyResultMeasureDto(7L, 1L, "Keyresult 2", "Description", 1L, "Robin", "Papier", 4L, 1, 2022,
                    ExpectedEvolution.CONSTANT, Unit.PERCENT, 20L, 100L, measure2Dto));
    static KeyResultDto keyresult1Dto = new KeyResultDto(5L, 1L, "Keyresult 1", "Description", 1L, "Alice",
            "Wunderland", 4L, 1, 2022, ExpectedEvolution.CONSTANT, Unit.PERCENT, 20L, 100L);
    static KeyResultDto keyresult2Dto = new KeyResultDto(7L, 1L, "Keyresult 2", "Description", 1L, "Alice",
            "Wunderland", 4L, 1, 2022, ExpectedEvolution.CONSTANT, Unit.PERCENT, 20L, 100L);

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ObjectiveService objectiveService;
    @MockBean
    private KeyResultService keyResultService;
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
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5))).andExpect(jsonPath("$[0].title", Is.is("Objective 1")))
                .andExpect(jsonPath("$[1].id", Is.is(7))).andExpect(jsonPath("$[1].title", Is.is("Objective 2")));
    }

    @Test
    void shouldGetAllObjectivesIfNoObjectiveExists() throws Exception {
        BDDMockito.given(objectiveService.getAllObjectives()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/objectives").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldGetAllKeyResultsByObjective() throws Exception {
        BDDMockito.given(keyResultService.getAllKeyResultsByObjectiveWithMeasure(1L)).willReturn(keyResultsMeasureList);

        mvc.perform(get("/api/v1/objectives/1/keyresults").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5))).andExpect(jsonPath("$[0].title", Is.is("Keyresult 1")))
                .andExpect(jsonPath("$[0].measure.id", Is.is(1))).andExpect(jsonPath("$[1].id", Is.is(7)))
                .andExpect(jsonPath("$[1].title", Is.is("Keyresult 2")))
                .andExpect(jsonPath("$[1].measure.id", Is.is(2)));
    }

    @Test
    void shouldGetAllKeyResultsIfNoKeyResultExistsInObjective() throws Exception {
        BDDMockito.given(keyResultService.getAllKeyResultsByObjective(1)).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/objectives/1/keyresults").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnErrorWhenObjectiveDoesntExistWhenGettingKeyResults() throws Exception {
        BDDMockito.given(keyResultService.getAllKeyResultsByObjectiveWithMeasure(1L))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective with id 1 not found"));

        mvc.perform(get("/api/v1/objectives/1/keyresults").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(status().isNotFound());
    }

    @Test
    void getObjective() throws Exception {
        BDDMockito.given(objectiveService.getObjective(5L)).willReturn(objective1);

        mvc.perform(get("/api/v1/objectives/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.title", Is.is("Objective 1")));
    }

    @Test
    void getObjectiveFail() throws Exception {
        BDDMockito.given(objectiveService.getObjective(10L))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(get("/api/v1/objectives/10").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnObjectiveWhenCreatingNewObjective() throws Exception {
        ObjectiveDto testObjective = new ObjectiveDto(42L, "Program Faster", 1L, "Rudi", "Grochde", 3L, "PuzzleITC", 1L,
                4, 2022, "Just be faster", 5L);

        BDDMockito.given(objectiveMapper.toDto(any())).willReturn(testObjective);
        BDDMockito.given(objectiveService.saveObjective(any())).willReturn(fullObjective);

        mvc.perform(post("/api/v1/objectives").contentType(MediaType.APPLICATION_JSON).content(
                "{\"title\": \"FullObjective\", \"ownerId\": 1, \"ownerFirstname\": \"Bob\", \"ownerLastname\": \"Kaufmann\", \"teamId\": 1, \"teamName\": \"Team1\", \"quarterId\": 1, \"quarterNumber\": 3, \"quarterYear\": 2020, \"description\": \"This is our description\", \"progress\": 33.3}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string(
                        "{\"id\":42,\"title\":\"Program Faster\",\"ownerId\":1,\"ownerFirstname\":\"Rudi\",\"ownerLastname\":\"Grochde\",\"teamId\":3,\"teamName\":\"PuzzleITC\",\"quarterId\":1,\"quarterNumber\":4,\"quarterYear\":2022,\"description\":\"Just be faster\",\"progress\":5}"));
        verify(objectiveService, times(1)).saveObjective(any());
    }

    @Test
    void shouldReturnResponseStatusExceptionWhenCreatingObjectiveWithNullValues() throws Exception {
        BDDMockito.given(objectiveService.saveObjective(any())).willThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute title when creating objective"));

        mvc.perform(post("/api/v1/objectives").contentType(MediaType.APPLICATION_JSON).content(
                "{\"title\": null, \"ownerId\": 1, \"ownerFirstname\": \"Bob\", \"ownerLastname\": \"Kaufmann\", \"teamId\": 1, \"teamName\": \"Team1\", \"quarterId\": 1, \"quarterNumber\": 3, \"quarterYear\": 2020, \"description\": \"This is our description\", \"progress\": 33.3}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @Disabled("Requestbody was wrong (swagger was implemented)")
    void shouldReturnUpdatedObjective() throws Exception {
        ObjectiveDto testObjective = new ObjectiveDto(1L, "Hunting", 1L, "Rudi", "Grochde", 3L, "PuzzleITC", 1L, 4,
                2022, "Everything Fine", 5L);
        Objective objective = Objective.Builder.builder().withId(1L).withDescription("Everything Fine").withProgress(5L)
                .withTitle("Hunting").build();

        BDDMockito.given(objectiveMapper.toDto(any())).willReturn(testObjective);
        BDDMockito.given(objectiveService.updateObjective(anyLong(), any())).willReturn(objective);

        mvc.perform(put("/api/v1/objectives/10")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(1))).andExpect(jsonPath("$.description", Is.is("Everything Fine")))
                .andExpect(jsonPath("$.title", Is.is("Hunting")));
    }

    @Test
    @Disabled("Requestbody was wrong (swagger was implemented)")
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(objectiveService.updateObjective(anyLong(), any())).willThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed objective -> Attribut is invalid"));

        mvc.perform(put("/api/v1/objectives/10")).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        BDDMockito.given(objectiveService.updateObjective(anyLong(), any())).willThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed objective -> Attribut is invalid"));

        mvc.perform(put("/api/v1/objectives/10")).andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
