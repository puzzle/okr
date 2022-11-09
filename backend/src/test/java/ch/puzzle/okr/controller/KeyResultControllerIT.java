package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.service.KeyResultService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
class KeyResultControllerIT {
    @MockBean
    KeyResultRepository keyResultRepository;

    @MockBean
    KeyResultMapper keyResultMapper;

    @MockBean
    MeasureMapper measureMapper;

    @MockBean
    KeyResultService keyResultService;

    static User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann").withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
    static KeyResult keyResult1 = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1").build();
    static Measure measure1 = Measure.Builder.builder().withId(1L).withKeyResult(keyResult1).withCreatedOn(LocalDateTime.MAX).withChangeInfo("Changeinfo1").withInitiatives("Initiatives1").withCreatedBy(user).withValue(23).build();
    static Measure measure2 = Measure.Builder.builder().withId(4L).withKeyResult(keyResult1).withCreatedOn(LocalDateTime.MAX).withChangeInfo("Changeinfo2").withInitiatives("Initiatives2").withCreatedBy(user).withValue(12).build();
    static List<Measure> measureList = Arrays.asList(measure1, measure2);


    @Autowired
    private MockMvc mvc;
    private KeyResultDto keyResultDTO;
    private Objective objective;
    private Quarter quarter;
    private KeyResult keyResult;


    void setUp() {
        this.keyResultDTO = new KeyResultDto(5L, 5L, "", "", 5L, "", "", 5L, 2, 2022, ExpectedEvolution.INCREASE, Unit.PERCENT, 0L, 1L);
        BDDMockito.given(keyResultMapper.toKeyResult(keyResultDTO)).willReturn(keyResult);

        this.objective = Objective.Builder.builder()
                .withId(5L)
                .withTitle("Objective 1")
                .build();

        this.quarter = Quarter.Builder.builder()
                .withId(5L)
                .withNumber(2)
                .withYear(2022)
                .build();

        this.keyResult = KeyResult.Builder.builder()
                .withId(5L)
                .withTitle("test")
                .withObjective(this.objective)
                .withOwner(this.user)
                .withQuarter(this.quarter)
                .build();
    }

    @Test
    void shouldReturnUpdatedKeyresult() throws Exception {
        KeyResult keyResult = KeyResult.Builder.builder()
                .withId(1L)
                .withTitle("Updated Keyresult 1")
                .build();
        BDDMockito.given(keyResultService.updateKeyResult(any())).willReturn(keyResult);

        mvc.perform(put("/api/v1/keyresults/1").contentType(MediaType.APPLICATION_JSON).content("{\"title\":  \"Updated Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(1)))
                .andExpect(jsonPath("$.title", Is.is("Updated Keyresult 1")));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(keyResultService.updateKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND,
                        any()));
        mvc.perform(put("/api/v1/keyresults/10").contentType(MediaType.APPLICATION_JSON).content("{\"title\":  \"Updated Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createKeyResult() throws Exception {
        BDDMockito.given(this.keyResultService.getQuarterById(5)).willReturn(this.quarter);
        BDDMockito.given(this.keyResultService.getOwnerById(5)).willReturn(this.user);
        BDDMockito.given(this.keyResultService.getObjectivebyId(5)).willReturn(this.objective);
        BDDMockito.given(this.keyResultService.createKeyResult(any())).willReturn(this.keyResult);

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(post("/api/v1/keyresults")
                        .content(mapper.writeValueAsString(this.keyResultDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(5)));
    }

    @Test
    void invalidDTO() throws Exception {
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Error"));

        ObjectMapper mapper = new ObjectMapper();
        mvc.perform(post("/api/v1/keyresults")
                        .content(mapper.writeValueAsString(this.keyResultDTO))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnMeasuresFromKeyResult() throws Exception {
        BDDMockito.given(keyResultService.getAllMeasuresByKeyResult(anyLong())).willReturn(measureList);

        System.out.println(measureList);

        mvc.perform(get("/api/v1/keyresults/5/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].value", Is.is(23)))
                .andExpect(jsonPath("$[0].createdBy", Is.is(1)))
                .andExpect(jsonPath("$[1].id", Is.is(4)))
                .andExpect(jsonPath("$[1].value", Is.is(12)))
                .andExpect(jsonPath("$[1].createdBy", Is.is(2)))
        ;
    }

    @Test
    void shouldGetAllMeasuresIfNoMeasureExistsInKeyResult() throws Exception {
        BDDMockito.given(keyResultService.getAllMeasuresByKeyResult(1)).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/keyresults/1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)))
        ;
    }

    @Test
    void shouldReturnErrorWhenKeyResultDoesntExistWhenGettingMeasuresFromKeyResult() throws Exception {
        BDDMockito.given(keyResultService.getAllMeasuresByKeyResult(1))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));

        mvc.perform(get("/api/v1/keyresults/1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(status().isNotFound())
        ;
    }
}
