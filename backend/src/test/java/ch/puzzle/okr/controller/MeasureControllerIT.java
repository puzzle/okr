package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.MeasureService;
import ch.puzzle.okr.service.ProgressService;
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
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(MeasureController.class)
class MeasureControllerIT {
    static Measure measure = Measure.Builder.builder().withId(5L)
            .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
            .withCreatedOn(LocalDateTime.MAX)
            .withKeyResult(KeyResult.Builder.builder().withId(8L).withBasisValue(12L).withTargetValue(50L).build())
            .withValue(30).withChangeInfo("ChangeInfo").withInitiatives("Initiatives").build();
    static Measure anotherMeasure = Measure.Builder.builder().withId(4L)
            .withCreatedBy(User.Builder.builder().withId(2L).withFirstname("Robert").build())
            .withCreatedOn(LocalDateTime.MAX)
            .withKeyResult(KeyResult.Builder.builder().withId(9L).withBasisValue(0L).withTargetValue(100L).build())
            .withValue(35).withChangeInfo("ChangeInfo").build();
    static MeasureDto measureDto = new MeasureDto(5L, 8L, 30, "changeInfo", "Initiatives", 1L, LocalDateTime.MAX);
    static MeasureDto anotherMeasureDto = new MeasureDto(4L, 9L, 35, "changeInfo", "Initiatives", 2L,
            LocalDateTime.MAX);
    static List<Measure> measureList = Arrays.asList(measure, anotherMeasure);

    @Autowired
    private MockMvc mvc;
    @MockBean
    private MeasureService measureService;
    @MockBean
    private MeasureMapper measureMapper;

    @MockBean
    private ProgressService progressService;

    @BeforeEach
    void setUp() {
        // setup team mapper
        BDDMockito.given(measureMapper.toDto(measure)).willReturn(measureDto);
        BDDMockito.given(measureMapper.toDto(anotherMeasure)).willReturn(anotherMeasureDto);
    }

    @Test
    void shouldGetAllMeasures() throws Exception {
        BDDMockito.given(measureService.getAllMeasures()).willReturn(measureList);
        BDDMockito.given(progressService.getObjectiveProgressInPercent(any())).willReturn(5D);

        mvc.perform(get("/api/v1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5))).andExpect(jsonPath("$[0].keyResultId", Is.is(8)))
                .andExpect(jsonPath("$[0].value", Is.is(30)))
                .andExpect(jsonPath("$[0].changeInfo", Is.is("changeInfo")))
                .andExpect(jsonPath("$[0].initiatives", Is.is("Initiatives")))
                .andExpect(jsonPath("$[0].createdById", Is.is(1))).andExpect(jsonPath("$[1].id", Is.is(4)))
                .andExpect(jsonPath("$[1].keyResultId", Is.is(9))).andExpect(jsonPath("$[1].value", Is.is(35)))
                .andExpect(jsonPath("$[1].changeInfo", Is.is("changeInfo")))
                .andExpect(jsonPath("$[1].createdById", Is.is(2)));
    }

    @Test
    void shouldGetAllMeasureIfNoMeasureExists() throws Exception {
        BDDMockito.given(measureService.getAllMeasures()).willReturn(Collections.emptyList());
        BDDMockito.given(progressService.getObjectiveProgressInPercent(any())).willReturn(5D);

        mvc.perform(get("/api/v1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnMeasureWhenCreatingNewMeasure() throws Exception {
        MeasureDto testMeasure = new MeasureDto(5L, 5L, 30, "changeInfo", "initiatives", 1L, LocalDateTime.now());

        BDDMockito.given(measureService.saveMeasure(any())).willReturn(measure);
        BDDMockito.given(measureMapper.toDto(any())).willReturn(testMeasure);
        BDDMockito.given(progressService.getObjectiveProgressInPercent(any())).willReturn(5D);
        BDDMockito.given(measureMapper.toMeasure(any())).willReturn(measure);

        mvc.perform(post("/api/v1/measures").contentType(MediaType.APPLICATION_JSON).content(
                "{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": \"changeInfo\", \"initiatives \": \"initiatives\", \"createdById \": 1}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.keyResultId", Is.is(5))).andExpect(jsonPath("$.value", Is.is(30)))
                .andExpect(jsonPath("$.changeInfo", Is.is("changeInfo")));
    }

    @Test
    void shouldReturnResponseStatusExceptionWhenCreatingMeasureNullName() throws Exception {
        BDDMockito.given(measureService.saveMeasure(any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is null"));
        BDDMockito.given(progressService.getObjectiveProgressInPercent(any())).willReturn(5D);

        mvc.perform(post("/api/v1/measures").contentType(MediaType.APPLICATION_JSON).content(
                "{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": \"changeInfo\", \"initiatives \": \"initiatives\", \"createdById \": null}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnCorrectMeasure() throws Exception {
        MeasureDto testMeasure = new MeasureDto(5L, 5L, 30, "changeInfo", "initiatives", 1L, LocalDateTime.now());

        BDDMockito.given(measureService.updateMeasure(anyLong(), any())).willReturn(measure);
        BDDMockito.given(measureMapper.toDto(any())).willReturn(testMeasure);
        BDDMockito.given(progressService.getObjectiveProgressInPercent(any())).willReturn(5D);
        BDDMockito.given(measureMapper.toMeasure(any())).willReturn(measure);

        mvc.perform(put("/api/v1/measures/1").contentType(MediaType.APPLICATION_JSON)
                .content("{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": "
                        + "\"changeInfo\", \"initiatives \": \"initiatives\", " + "\"createdById \": null}"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.value", Is.is(30)))
                .andExpect(jsonPath("$.createdById", Is.is(1)))
                .andExpect(jsonPath("$.initiatives", Is.is("initiatives")));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(measureService.updateMeasure(anyLong(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(put("/api/v1/measures/3").contentType(MediaType.APPLICATION_JSON)
                .content("{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": "
                        + "\"changeInfo\", \"initiatives \": \"initiatives\", " + "\"createdById \": null}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        BDDMockito.given(measureService.updateMeasure(anyLong(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        mvc.perform(put("/api/v1/measures/3").contentType(MediaType.APPLICATION_JSON)
                .content("{\"keyResultId\": null , \"value\": 30, \"changeInfo\": "
                        + "\"changeInfo\", \"initiatives \": \"initiatives\", " + "\"createdById \": null}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
