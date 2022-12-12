package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.service.KeyResultService;
import ch.puzzle.okr.service.ProgressService;
import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(KeyResultController.class)
class KeyResultControllerIT {

    static User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
            .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
    static KeyResult keyResult1 = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1").build();
    static Measure measure1 = Measure.Builder.builder().withId(1L).withKeyResult(keyResult1).withCreatedBy(user)
            .withCreatedOn(LocalDateTime.MAX).withChangeInfo("Changeinfo1").withInitiatives("Initiatives1")
            .withValue(23).build();
    static Measure measure2 = Measure.Builder.builder().withId(4L).withKeyResult(keyResult1).withCreatedBy(user)
            .withCreatedOn(LocalDateTime.MAX).withChangeInfo("Changeinfo2").withInitiatives("Initiatives2")
            .withValue(12).build();
    static List<Measure> measureList = Arrays.asList(measure1, measure2);
    static MeasureDto measureDto1 = new MeasureDto(1L, 5L, 34, "Changeinfo1", "Ininitatives1", 1L, LocalDateTime.MAX);
    static MeasureDto measureDto2 = new MeasureDto(4L, 5L, 12, "Changeinfo2", "Ininitatives2", 1L, LocalDateTime.MAX);
    static KeyResultDto keyResultDto = new KeyResultDto(5L, 5L, "", "", 5L, "", "", 5L, 2, 2022,
            ExpectedEvolution.INCREASE, Unit.PERCENT, 0L, 1L);
    static Objective objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    static Quarter quarter = Quarter.Builder.builder().withId(5L).withNumber(2).withYear(2022).build();
    static KeyResult keyResult = KeyResult.Builder.builder().withId(5L).withTitle("test").withObjective(objective)
            .withOwner(user).withQuarter(quarter).build();
    @MockBean
    KeyResultRepository keyResultRepository;
    @MockBean
    KeyResultMapper keyResultMapper;
    @MockBean
    MeasureMapper measureMapper;
    @MockBean
    KeyResultService keyResultService;
    @MockBean
    ProgressService progressService;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        BDDMockito.given(keyResultMapper.toKeyResult(keyResultDto)).willReturn(keyResult);
        BDDMockito.given(measureMapper.toDto(measure1)).willReturn(measureDto1);
        BDDMockito.given(measureMapper.toDto(measure2)).willReturn(measureDto2);
    }

    @Test
    void shouldReturnUpdatedKeyResult() throws Exception {
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withTitle("Updated Keyresult 1").build();
        KeyResultDto testKeyResult = new KeyResultDto(1L, 1L, "Program Faster", "Just be faster", 1L, "Rudi", "Grochde",
                1L, 4, 2022, ExpectedEvolution.INCREASE, Unit.PERCENT, 4L, 12L);

        BDDMockito.given(keyResultService.updateKeyResult(any())).willReturn(keyResult);
        BDDMockito.given(keyResultMapper.toDto(any())).willReturn(testKeyResult);
        BDDMockito.given(keyResultMapper.toKeyResult(any())).willReturn(keyResult);

        mvc.perform(put("/api/v1/keyresults/1").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":  \"Updated Keyresult 1\"}")).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", Is.is("Program Faster")))
                .andExpect(jsonPath("$.ownerFirstname", Is.is("Rudi")))
                .andExpect(jsonPath("$.unit", Is.is(Unit.PERCENT.toString()))).andReturn();
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(keyResultService.updateKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, any()));
        mvc.perform(put("/api/v1/keyresults/10").contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":  \"Updated Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createKeyResult() throws Exception {
        KeyResultDto testKeyResult = new KeyResultDto(5L, 1L, "Program Faster", "Just be faster", 1L, "Rudi", "Grochde",
                1L, 4, 2022, ExpectedEvolution.INCREASE, Unit.PERCENT, 4L, 12L);

        BDDMockito.given(this.keyResultService.getQuarterById(5)).willReturn(quarter);
        BDDMockito.given(this.keyResultService.getOwnerById(5)).willReturn(user);
        BDDMockito.given(this.keyResultService.getObjectivebyId(5)).willReturn(objective);
        BDDMockito.given(this.keyResultService.createKeyResult(any())).willReturn(keyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(testKeyResult);
        BDDMockito.given(keyResultMapper.toKeyResult(any())).willReturn(keyResult);

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(post("/api/v1/keyresults").content(mapper.writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Is.is(5)));
    }

    @Test
    void invalidDTO() throws Exception {
        BDDMockito.given(this.keyResultMapper.toKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Error"));

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(post("/api/v1/keyresults").content(mapper.writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)).andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnMeasuresFromKeyResult() throws Exception {
        BDDMockito.given(keyResultService.getAllMeasuresByKeyResult(5)).willReturn(measureList);

        mvc.perform(get("/api/v1/keyresults/5/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(1))).andExpect(jsonPath("$[0].value", Is.is(34)))
                .andExpect(jsonPath("$[0].keyResultId", Is.is(5))).andExpect(jsonPath("$[0].createdById", Is.is(1)))
                .andExpect(jsonPath("$[0].changeInfo", Is.is("Changeinfo1")))
                .andExpect(jsonPath("$[0].initiatives", Is.is("Ininitatives1")))
                .andExpect(jsonPath("$[1].id", Is.is(4))).andExpect(jsonPath("$[1].value", Is.is(12)))
                .andExpect(jsonPath("$[1].createdById", Is.is(1)))
                .andExpect(jsonPath("$[1].changeInfo", Is.is("Changeinfo2")))
                .andExpect(jsonPath("$[1].initiatives", Is.is("Ininitatives2")))
                .andExpect(jsonPath("$[1].keyResultId", Is.is(5)));
    }

    @Test
    void shouldGetAllMeasuresIfNoMeasureExistsInKeyResult() throws Exception {
        BDDMockito.given(keyResultService.getAllMeasuresByKeyResult(1)).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/keyresults/1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnErrorWhenKeyResultDoesntExistWhenGettingMeasuresFromKeyResult() throws Exception {
        BDDMockito.given(keyResultService.getAllMeasuresByKeyResult(1))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));

        mvc.perform(get("/api/v1/keyresults/1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(status().isNotFound());
    }
}
