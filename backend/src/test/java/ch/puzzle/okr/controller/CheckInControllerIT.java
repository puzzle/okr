package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.checkIn.CheckInDto;
import ch.puzzle.okr.dto.checkIn.CheckInMetricDto;
import ch.puzzle.okr.dto.checkIn.CheckInOrdinalDto;
import ch.puzzle.okr.mapper.checkIn.CheckInMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkIn.CheckIn;
import ch.puzzle.okr.models.checkIn.CheckInMetric;
import ch.puzzle.okr.models.checkIn.CheckInOrdinal;
import ch.puzzle.okr.models.checkIn.Zone;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.business.CheckInBusinessService;
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

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CheckInController.class)
class CheckInControllerIT {
    /* Mocked Services and Mappers */
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CheckInBusinessService checkInBusinessService;
    @MockBean
    private CheckInMapper checkInMapper;

    /* Test entities */
    static Objective objective = Objective.Builder.builder().withId(1L).build();
    static CheckIn checkInMetric = CheckInMetric.Builder.builder().withValue(30D).withCheckInType("metric")
            .withConfidence(5).withChangeInfo("ChangeInfo").withInitiatives("Initiatives")
            .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
            .withKeyResult(KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(6.0).withId(8L)
                    .withObjective(objective).build())
            .build();
    static CheckIn checkInOrdinal = CheckInOrdinal.Builder.builder().withValue(Zone.COMMIT.toString()).withId(4L)
            .withCreatedBy(User.Builder.builder().withId(2L).withFirstname("Robert").build())
            .withCreatedOn(LocalDateTime.MAX)
            .withKeyResult(
                    KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withTargetZone("Wald").withId(9L).build())
            .withChangeInfo("ChangeInfo").build();

    /* Test DTOs */
    static CheckInDto checkInMetricDto = new CheckInMetricDto(5L, "Changeinfo1", "Initiatives1", 6, 1L,
            User.Builder.builder().withId(1L).withFirstname("Frank").build(), null, null, "metric", 46D);
    static CheckInDto checkInOrdinalDto = new CheckInOrdinalDto(4L, "Changeinfo2", "Initiatives2", 5, 2L,
            User.Builder.builder().withId(1L).withFirstname("Frank").build(), null, null, "metric",
            String.valueOf(Zone.COMMIT));

    @BeforeEach
    void setUp() {
        BDDMockito.given(checkInMapper.toDto(checkInMetric)).willReturn(checkInMetricDto);
        BDDMockito.given(checkInMapper.toDto(checkInOrdinal)).willReturn(checkInOrdinalDto);
    }

    @Test
    void shouldGetMetricCheckInWithId() throws Exception {
        BDDMockito.given(checkInBusinessService.getCheckInById(5L)).willReturn(checkInMetric);

        mvc.perform(get("/api/v2/checkIns/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(checkInMetricDto.getId().intValue())))
                .andExpect(jsonPath("$.initiatives", Is.is(checkInMetricDto.getInitiatives())))
                .andExpect(jsonPath("$.confidence", Is.is(checkInMetricDto.getConfidence())))
                .andExpect(jsonPath("$.keyResultId", Is.is(checkInMetricDto.getKeyResultId().intValue())))
                .andExpect(jsonPath("$.createdBy.id", Is.is(checkInMetricDto.getCreatedBy().getId().intValue())))
                .andExpect(jsonPath("$.checkInType", Is.is(checkInMetricDto.getCheckInType())))
                .andExpect(jsonPath("$.modifiedOn", Is.is(checkInMetricDto.getModifiedOn())))
                .andExpect(jsonPath("$.createdOn", Is.is(checkInMetricDto.getCreatedOn())))
                .andExpect(jsonPath("$.value", Is.is(((CheckInMetricDto) checkInMetricDto).getValue())));
    }

    @Test
    void shouldGetOrdinalCheckInWithId() throws Exception {
        BDDMockito.given(checkInBusinessService.getCheckInById(5L)).willReturn(checkInOrdinal);

        mvc.perform(get("/api/v2/checkIns/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(checkInOrdinalDto.getId().intValue())))
                .andExpect(jsonPath("$.initiatives", Is.is(checkInOrdinalDto.getInitiatives())))
                .andExpect(jsonPath("$.confidence", Is.is(checkInOrdinalDto.getConfidence())))
                .andExpect(jsonPath("$.keyResultId", Is.is(checkInOrdinalDto.getKeyResultId().intValue())))
                .andExpect(jsonPath("$.createdBy.id", Is.is(checkInOrdinalDto.getCreatedBy().getId().intValue())))
                .andExpect(jsonPath("$.checkInType", Is.is(checkInOrdinalDto.getCheckInType())))
                .andExpect(jsonPath("$.modifiedOn", Is.is(checkInOrdinalDto.getModifiedOn())))
                .andExpect(jsonPath("$.createdOn", Is.is(checkInOrdinalDto.getCreatedOn())))
                .andExpect(jsonPath("$.value", Is.is(((CheckInOrdinalDto) checkInOrdinalDto).getValue())));
    }

    @Test
    void shouldNotFindTheCheckInWithId() throws Exception {
        BDDMockito.given(checkInBusinessService.getCheckInById(anyLong()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mvc.perform(get("/api/v2/checkIns/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedCheckIn() throws Exception {
        BDDMockito.given(checkInBusinessService.updateCheckIn(anyLong(), any(), any())).willReturn(checkInMetric);
        BDDMockito.given(checkInMapper.toCheckIn(any())).willReturn(checkInMetric);

        mvc.perform(put("/api/v2/checkIns/5").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"changeinfo\": \"Changeinfo1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(checkInMetricDto.getId().intValue())))
                .andExpect(jsonPath("$.changeInfo", Is.is(checkInMetricDto.getChangeInfo())))
                .andExpect(jsonPath("$.initiatives", Is.is(checkInMetricDto.getInitiatives())))
                .andExpect(jsonPath("$.confidence", Is.is(checkInMetricDto.getConfidence())))
                .andExpect(jsonPath("$.checkInType", Is.is(checkInMetricDto.getCheckInType())))
                .andExpect(jsonPath("$.keyResultId", Is.is(checkInMetricDto.getKeyResultId().intValue())));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(checkInBusinessService.updateCheckIn(anyLong(), any(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, any()));
        mvc.perform(put("/api/v2/checkIns/5").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"changeinfo\": \"Changeinfo1\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldCreateCheckInMetric() throws Exception {
        BDDMockito.given(checkInBusinessService.saveCheckIn(any(), any())).willReturn(checkInMetric);

        mvc.perform(post("/api/v2/checkIns").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"changeinfo\": \"Changeinfo1\"}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Is.is(checkInMetricDto.getId().intValue())))
                .andExpect(jsonPath("$.changeInfo", Is.is(checkInMetricDto.getChangeInfo())))
                .andExpect(jsonPath("$.initiatives", Is.is(checkInMetricDto.getInitiatives())))
                .andExpect(jsonPath("$.confidence", Is.is(checkInMetricDto.getConfidence())))
                .andExpect(jsonPath("$.keyResultId", Is.is(checkInMetricDto.getKeyResultId().intValue())))
                .andExpect(jsonPath("$.createdBy.id", Is.is(checkInMetricDto.getCreatedBy().getId().intValue())))
                .andExpect(jsonPath("$.value", Is.is(((CheckInMetricDto) checkInMetricDto).getValue())))
                .andExpect(jsonPath("$.checkInType", Is.is(checkInMetricDto.getCheckInType())));
    }

    @Test
    void shouldCreateCheckInOrdinal() throws Exception {
        BDDMockito.given(checkInBusinessService.saveCheckIn(any(), any())).willReturn(checkInOrdinal);

        mvc.perform(post("/api/v2/checkIns").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"changeinfo\": \"Changeinfo1\"}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", Is.is(checkInOrdinalDto.getId().intValue())))
                .andExpect(jsonPath("$.changeInfo", Is.is(checkInOrdinalDto.getChangeInfo())))
                .andExpect(jsonPath("$.initiatives", Is.is(checkInOrdinalDto.getInitiatives())))
                .andExpect(jsonPath("$.confidence", Is.is(checkInOrdinalDto.getConfidence())))
                .andExpect(jsonPath("$.keyResultId", Is.is(checkInOrdinalDto.getKeyResultId().intValue())))
                .andExpect(jsonPath("$.createdBy.id", Is.is(checkInOrdinalDto.getCreatedBy().getId().intValue())))
                .andExpect(jsonPath("$.value", Is.is(((CheckInOrdinalDto) checkInOrdinalDto).getValue())))
                .andExpect(jsonPath("$.checkInType", Is.is(checkInOrdinalDto.getCheckInType())));
    }
}
