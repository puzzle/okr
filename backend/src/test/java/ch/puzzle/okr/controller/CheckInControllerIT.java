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
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import com.fasterxml.jackson.databind.node.ObjectNode;
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
    @MockBean
    private KeyResultBusinessService keyResultBusinessService;

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
            LocalDateTime.MAX, LocalDateTime.MAX, 46D);
    static CheckInDto checkInOrdinalDto = new CheckInOrdinalDto(4L, "Changeinfo2", "Initiatives2", 5, 2L,
            LocalDateTime.MAX, LocalDateTime.MAX, String.valueOf(Zone.COMMIT));

    @BeforeEach
    void setUp() {
        BDDMockito.given(checkInMapper.toDto(checkInMetric)).willReturn(checkInMetricDto);
        BDDMockito.given(checkInMapper.toDto(checkInOrdinal)).willReturn(checkInOrdinalDto);
    }

    @Test
    void shouldGetMetricCheckInWithId() throws Exception {
        BDDMockito.given(checkInBusinessService.getCheckInById(5L)).willReturn(checkInMetric);

        mvc.perform(get("/api/v2/checkIns/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.initiatives", Is.is("Initiatives1")))
                .andExpect(jsonPath("$.confidence", Is.is(6))).andExpect(jsonPath("$.keyResultId", Is.is(1)))
                .andExpect(jsonPath("$.modifiedOn", Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath("$.createdOn", Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath("$.valueMetric", Is.is(46D)));
    }

    @Test
    void shouldGetOrdinalCheckInWithId() throws Exception {
        BDDMockito.given(checkInBusinessService.getCheckInById(5L)).willReturn(checkInOrdinal);

        mvc.perform(get("/api/v2/checkIns/5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.id", Is.is(4)))
                .andExpect(jsonPath("$.initiatives", Is.is("Initiatives2")))
                .andExpect(jsonPath("$.confidence", Is.is(5))).andExpect(jsonPath("$.keyResultId", Is.is(2)))
                .andExpect(jsonPath("$.modifiedOn", Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath("$.createdOn", Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath("$.zone", Is.is(Zone.COMMIT.toString())));
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
        BDDMockito.given(keyResultBusinessService.getKeyResultById(anyLong())).willReturn(KeyResultMetric.Builder.builder()
                .withId(1L).withKeyResultType("metric").build());
        BDDMockito.given(checkInBusinessService.updateCheckIn(anyLong(), any(), any())).willReturn(checkInMetric);
        BDDMockito.given(checkInMapper.toCheckIn(any(), any())).willReturn(checkInMetric);

        mvc.perform(put("/api/v2/checkIns/5").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"changeinfo\": \"Changeinfo1\", \"keyResultId\":  1}"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.changeInfo", Is.is("Changeinfo1")))
                .andExpect(jsonPath("$.initiatives", Is.is("Initiatives1")))
                .andExpect(jsonPath("$.confidence", Is.is(6)))
                .andExpect(jsonPath("$.keyResultId", Is.is(1)));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(anyLong())).willReturn(KeyResultMetric.Builder.builder()
                .withId(1L).withKeyResultType("metric").build());
        BDDMockito.given(checkInMapper.toCheckIn(any(), any())).willReturn(checkInMetric);
        BDDMockito.given(checkInBusinessService.updateCheckIn(anyLong(), any(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        mvc.perform(put("/api/v2/checkIns/5").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"changeinfo\": \"Changeinfo1\", \"keyResultId\":  1}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldCreateCheckInMetric() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(anyLong())).willReturn(KeyResultMetric.Builder.builder()
                .withId(1L).withKeyResultType("metric").build());
        BDDMockito.given(checkInBusinessService.saveCheckIn(any(), any())).willReturn(checkInMetric);

        mvc.perform(post("/api/v2/checkIns").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"changeinfo\": \"Changeinfo1\", \"keyResultId\":  1}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.changeInfo", Is.is("Changeinfo1")))
                .andExpect(jsonPath("$.initiatives", Is.is("Initiatives1")))
                .andExpect(jsonPath("$.confidence", Is.is(6)))
                .andExpect(jsonPath("$.keyResultId", Is.is(1)))
                .andExpect(jsonPath("$.valueMetric", Is.is(46D)));
    }

    @Test
    void shouldCreateCheckInOrdinal() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(anyLong())).willReturn(KeyResultMetric.Builder.builder()
                .withId(1L).withKeyResultType("ordinal").build());
        BDDMockito.given(checkInBusinessService.saveCheckIn(any(), any())).willReturn(checkInOrdinal);

        mvc.perform(post("/api/v2/checkIns").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"changeinfo\": \"Changeinfo1\", \"keyResultId\":  1}"))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(4)))
                .andExpect(jsonPath("$.changeInfo", Is.is("Changeinfo2")))
                .andExpect(jsonPath("$.initiatives", Is.is("Initiatives2")))
                .andExpect(jsonPath("$.confidence", Is.is(5))).andExpect(jsonPath("$.keyResultId", Is.is(2)))
                .andExpect(jsonPath("$.zone", Is.is(Zone.COMMIT.toString())));
    }
}
