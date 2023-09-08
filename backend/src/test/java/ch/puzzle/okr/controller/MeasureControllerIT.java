package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.business.MeasureBusinessService;
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
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(MeasureController.class)
class MeasureControllerIT {
    static Objective objective = Objective.Builder.builder().withId(1L).build();
    static Measure measure = Measure.Builder.builder().withId(5L)
            .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
            .withCreatedOn(LocalDateTime.MAX)
            .withKeyResult(KeyResult.Builder.builder().withId(8L).withBasisValue(12D).withObjective(objective)
                    .withTargetValue(50D).build())
            .withValue(30D).withChangeInfo("ChangeInfo").withInitiatives("Initiatives").build();
    static Measure anotherMeasure = Measure.Builder.builder().withId(4L)
            .withCreatedBy(User.Builder.builder().withId(2L).withFirstname("Robert").build())
            .withCreatedOn(LocalDateTime.MAX)
            .withKeyResult(KeyResult.Builder.builder().withId(9L).withBasisValue(0D).withTargetValue(100D).build())
            .withValue(35D).withChangeInfo("ChangeInfo").build();
    static List<Measure> measureList = Arrays.asList(measure, anotherMeasure);
    static MeasureDto measureDto = new MeasureDto(5L, 8L, 30D, "changeInfo", "Initiatives", 1L, LocalDateTime.MAX,
            Instant.parse("2022-08-12T01:01:00.00Z"));
    static MeasureDto anotherMeasureDto = new MeasureDto(4L, 9L, 35D, "changeInfo", "Initiatives", 2L,
            LocalDateTime.MAX, Instant.parse("2022-08-12T01:01:00.00Z"));
    @Autowired
    private MockMvc mvc;
    @MockBean
    private MeasureBusinessService measureBusinessService;
    @MockBean
    private ProgressService progressService;
    @MockBean
    private MeasureMapper measureMapper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(measureMapper.toDto(measure)).willReturn(measureDto);
        BDDMockito.given(measureMapper.toDto(anotherMeasure)).willReturn(anotherMeasureDto);
    }

    @Test
    void shouldGetAllMeasures() throws Exception {
        BDDMockito.given(measureBusinessService.getAllMeasures()).willReturn(measureList);

        mvc.perform(get("/api/v1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5))).andExpect(jsonPath("$[0].keyResultId", Is.is(8)))
                .andExpect(jsonPath("$[0].value", Is.is(30D)))
                .andExpect(jsonPath("$[0].changeInfo", Is.is("changeInfo")))
                .andExpect(jsonPath("$[0].initiatives", Is.is("Initiatives")))
                .andExpect(jsonPath("$[0].createdById", Is.is(1))).andExpect(jsonPath("$[1].id", Is.is(4)))
                .andExpect(jsonPath("$[1].keyResultId", Is.is(9))).andExpect(jsonPath("$[1].value", Is.is(35D)))
                .andExpect(jsonPath("$[1].changeInfo", Is.is("changeInfo")))
                .andExpect(jsonPath("$[1].measureDate", Is.is("2022-08-12T01:01:00Z")))
                .andExpect(jsonPath("$[1].createdById", Is.is(2)));
    }

    @Test
    void shouldGetAllMeasureIfNoMeasureExists() throws Exception {
        BDDMockito.given(measureBusinessService.getAllMeasures()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnMeasureWhenCreatingNewMeasure() throws Exception {
        MeasureDto testMeasure = new MeasureDto(5L, 5L, 30D, "changeInfo", "initiatives", 1L, LocalDateTime.now(),
                Instant.parse("2022-08-12T01:01:00.00Z"));

        BDDMockito.given(measureBusinessService.saveMeasure(any())).willReturn(measure);
        BDDMockito.given(measureMapper.toDto(any())).willReturn(testMeasure);
        BDDMockito.given(measureMapper.toMeasure(any())).willReturn(measure);

        mvc.perform(post("/api/v1/measures").contentType(MediaType.APPLICATION_JSON).content(
                "{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": \"changeInfo\", \"initiatives \": \"initiatives\", \"createdById \": 1, \"measureDate \": \"2022-08-12T01:01:00\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.keyResultId", Is.is(5))).andExpect(jsonPath("$.value", Is.is(30D)))
                .andExpect(jsonPath("$.changeInfo", Is.is("changeInfo")));
    }

    @Test
    void shouldReturnResponseStatusExceptionWhenCreatingMeasureNullName() throws Exception {
        BDDMockito.given(measureBusinessService.saveMeasure(any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is null"));

        mvc.perform(post("/api/v1/measures").contentType(MediaType.APPLICATION_JSON).content(
                "{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": \"changeInfo\", \"initiatives \": \"initiatives\", \"createdById \": null, \"measureDate \": null}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnCorrectMeasure() throws Exception {
        MeasureDto testMeasure = new MeasureDto(5L, 5L, 30D, "changeInfo", "initiatives", 1L, LocalDateTime.now(),
                Instant.parse("2022-08-12T01:01:00.00Z"));
        BDDMockito.given(measureBusinessService.updateMeasure(anyLong(), any())).willReturn(measure);
        BDDMockito.given(measureMapper.toDto(any())).willReturn(testMeasure);
        BDDMockito.given(measureMapper.toMeasure(any())).willReturn(measure);

        mvc.perform(put("/api/v1/measures/1").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"keyResultId\": 1, \"value\": 30, \"changeInfo\": "
                        + "\"changeInfo\", \"initiatives \": \"initiatives\", \"createdById \": null, \"measureDate \": null}"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.value", Is.is(30D)))
                .andExpect(jsonPath("$.createdById", Is.is(1)))
                .andExpect(jsonPath("$.measureDate", Is.is("2022-08-12T01:01:00Z")))
                .andExpect(jsonPath("$.initiatives", Is.is("initiatives")));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(measureBusinessService.updateMeasure(anyLong(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(put("/api/v1/measures/3").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": "
                        + "\"changeInfo\", \"initiatives \": \"initiatives\", " + "\"createdById \": null}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        BDDMockito.given(measureBusinessService.updateMeasure(anyLong(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        mvc.perform(put("/api/v1/measures/3").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"keyResultId\": null , \"value\": 30, \"changeInfo\": "
                        + "\"changeInfo\", \"initiatives \": \"initiatives\", " + "\"createdById \": null}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldDeleteMeasure() throws Exception {
        when(measureBusinessService.getMeasureById(anyLong())).thenReturn(measure);
        mvc.perform(delete("/api/v1/measures/10").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void throwExceptionWhenMeasureWithIdCantBeFoundWhileDeleting() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Measure with measureId 100 not found"))
                .when(measureBusinessService).getMeasureById(anyLong());

        mvc.perform(delete("/api/v1/measures/1000").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
