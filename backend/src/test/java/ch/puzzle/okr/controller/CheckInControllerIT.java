package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.checkIn.CheckInDto;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.business.MeasureBusinessService;
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
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CheckInController.class)
class CheckInControllerIT {
    static Objective objective = Objective.Builder.builder().withId(1L).build();
    static Measure measure = Measure.Builder.builder().withId(5L)
            .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
            .withCreatedOn(LocalDateTime.MAX)
            .withKeyResult(KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(6.0).withId(8L)
                    .withObjective(objective).build())
            .withValue(30D).withChangeInfo("ChangeInfo").withInitiatives("Initiatives").build();
    static Measure anotherMeasure = Measure.Builder.builder().withId(4L)
            .withCreatedBy(User.Builder.builder().withId(2L).withFirstname("Robert").build())
            .withCreatedOn(LocalDateTime.MAX)
            .withKeyResult(
                    KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withTargetZone("Wald").withId(9L).build())
            .withValue(35D).withChangeInfo("ChangeInfo").build();
    static List<Measure> measureList = Arrays.asList(measure, anotherMeasure);
    static CheckInDto checkInDto = new CheckIn(5L, 8L, 30D, "changeInfo", "Initiatives", 1L, LocalDateTime.MAX,
            Instant.parse("2022-08-12T01:01:00.00Z"));
    static CheckInDto anotherCheckInDto = new CheckInDto(4L, 9L, 35D, "changeInfo", "Initiatives", 2L,
            LocalDateTime.MAX, Instant.parse("2022-08-12T01:01:00.00Z"));
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CheckInBusinessService checkInBusinessService;
    @MockBean
    private CheckInMapper checkInMapper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(checkInMapper.toDto(measure)).willReturn(checkInDto);
        BDDMockito.given(checkInMapper.toDto(anotherMeasure)).willReturn(anotherCheckInDto);
    }

    @Test
    void shouldReturnMeasureWhenCreatingNewMeasure() throws Exception {
        CheckInDto testMeasure = new CheckInDto(5L, 5L, 30D, "changeInfo", "initiatives", 1L, LocalDateTime.now(),
                Instant.parse("2022-08-12T01:01:00.00Z"));

        BDDMockito.given(checkInBusinessService.saveMeasure(any(), any())).willReturn(measure);
        BDDMockito.given(checkInMapper.toDto(any())).willReturn(testMeasure);
        BDDMockito.given(checkInMapper.toMeasure(any())).willReturn(measure);

        mvc.perform(post("/api/v2/checkIns").contentType(MediaType.APPLICATION_JSON).content(
                "{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": \"changeInfo\", \"initiatives \": \"initiatives\", \"createdById \": 1, \"measureDate \": \"2022-08-12T01:01:00\"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.keyResultId", Is.is(5))).andExpect(jsonPath("$.value", Is.is(30D)))
                .andExpect(jsonPath("$.changeInfo", Is.is("changeInfo")));
    }

    @Test
    void shouldReturnResponseStatusExceptionWhenCreatingMeasureNullName() throws Exception {
        BDDMockito.given(checkInBusinessService.saveMeasure(any(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "The given user is null"));

        mvc.perform(post("/api/v2/checkIns").contentType(MediaType.APPLICATION_JSON).content(
                "{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": \"changeInfo\", \"initiatives \": \"initiatives\", \"createdById \": null, \"measureDate \": null}")
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnCorrectMeasure() throws Exception {
        CheckInDto testMeasure = new CheckInDto(5L, 5L, 30D, "changeInfo", "initiatives", 1L, LocalDateTime.now(),
                Instant.parse("2022-08-12T01:01:00.00Z"));
        BDDMockito.given(checkInBusinessService.updateMeasure(anyLong(), any(), any())).willReturn(measure);
        BDDMockito.given(checkInMapper.toDto(any())).willReturn(testMeasure);
        BDDMockito.given(checkInMapper.toMeasure(any())).willReturn(measure);

        mvc.perform(put("/api/v2/checkIns/1").contentType(MediaType.APPLICATION_JSON)
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
        BDDMockito.given(checkInBusinessService.updateMeasure(anyLong(), any(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(put("/api/v1/measures/3").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": "
                        + "\"changeInfo\", \"initiatives \": \"initiatives\", " + "\"createdById \": null}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        BDDMockito.given(checkInBusinessService.updateMeasure(anyLong(), any(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST));
        mvc.perform(put("/api/v2/checkIns/3").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"keyResultId\": null , \"value\": 30, \"changeInfo\": "
                        + "\"changeInfo\", \"initiatives \": \"initiatives\", " + "\"createdById \": null}"))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }
}
