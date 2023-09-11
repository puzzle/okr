package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.checkIn.CheckInDto;
import ch.puzzle.okr.dto.checkIn.CheckInMetricDto;
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
    static CheckIn checkIn = CheckInMetric.Builder.builder().withValue(30D).withCheckInType("metric").withConfidence(5)
            .withChangeInfo("ChangeInfo").withInitiatives("Initiatives")
            .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
            .withKeyResult(KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(6.0).withId(8L)
                    .withObjective(objective).build())
            .build();
    static CheckIn anotherCheckIn = CheckInOrdinal.Builder.builder().withValue(Zone.COMMIT).withId(4L)
            .withCreatedBy(User.Builder.builder().withId(2L).withFirstname("Robert").build())
            .withCreatedOn(LocalDateTime.MAX)
            .withKeyResult(
                    KeyResultOrdinal.Builder.builder().withCommitZone("Baum").withTargetZone("Wald").withId(9L).build())
            .withChangeInfo("ChangeInfo").build();
    static List<CheckIn> checkInList = Arrays.asList(checkIn, anotherCheckIn);

    static CheckInDto checkInDto = new CheckInMetricDto(5L, "Changeinfo1", "Initiatives1", 6,
            KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(6.0).withId(8L).withObjective(objective)
                    .build(),
            User.Builder.builder().withId(1L).withFirstname("Frank").build(), LocalDateTime.MAX, LocalDateTime.MAX,
            "metric", 46D);
    static CheckInDto anotherCheckInDto = new CheckInMetricDto(4L, "Changeinfo2", "Initiatives2", 5,
            KeyResultMetric.Builder.builder().withBaseline(3.0).withStretchGoal(6.0).withId(8L).withObjective(objective)
                    .build(),
            User.Builder.builder().withId(1L).withFirstname("Frank").build(), LocalDateTime.MAX, LocalDateTime.MAX,
            "metric", 30D);
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CheckInBusinessService checkInBusinessService;
    @MockBean
    private CheckInMapper checkInMapper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(checkInMapper.toDto(checkIn)).willReturn(checkInDto);
        BDDMockito.given(checkInMapper.toDto(anotherCheckIn)).willReturn(anotherCheckInDto);
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(checkInBusinessService.updateCheckIn(anyLong(), any(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));
        mvc.perform(put("/api/v1/checkIns/3").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())
                .content("{\"keyResultId\": 5 , \"value\": 30, \"changeInfo\": "
                        + "\"changeInfo\", \"initiatives \": \"initiatives\", " + "\"createdById \": null}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
