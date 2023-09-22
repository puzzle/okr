package ch.puzzle.okr.controller;

import ch.puzzle.okr.CheckInTestHelpers;
import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.checkin.CheckInOrdinalDto;
import ch.puzzle.okr.mapper.checkin.CheckInMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.checkin.CheckInOrdinal;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.business.CheckInBusinessService;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
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

import static ch.puzzle.okr.CheckInTestHelpers.*;
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

    @BeforeEach
    void setUp() {
        BDDMockito.given(checkInMapper.toDto(checkInMetric)).willReturn(checkInMetricDto);
        BDDMockito.given(checkInMapper.toDto(checkInOrdinal)).willReturn(checkInOrdinalDto);
    }

    @Test
    void shouldGetMetricCheckInWithId() throws Exception {
        BDDMockito.given(checkInBusinessService.getCheckInById(5L)).willReturn(checkInMetric);

        mvc.perform(get(CHECK_IN_5_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_INITIATIVES, Is.is(INITIATIVES_1)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(6)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(1)))
                .andExpect(jsonPath(JSON_PATH_MODIFIED_ON, Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath(JSON_PATH_CREATED_ON, Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath(JSON_PATH_VALUE_METRIC, Is.is(46D)));
    }

    @Test
    void shouldGetOrdinalCheckInWithId() throws Exception {
        BDDMockito.given(checkInBusinessService.getCheckInById(5L)).willReturn(checkInOrdinal);

        mvc.perform(get(CHECK_IN_5_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath(JSON_PATH_ID, Is.is(4)))
                .andExpect(jsonPath(JSON_PATH_INITIATIVES, Is.is(INITIATIVES_2)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(2)))
                .andExpect(jsonPath(JSON_PATH_MODIFIED_ON, Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath(JSON_PATH_CREATED_ON, Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath(JSON_PATH_ZONE, Is.is(Zone.COMMIT.toString())));
    }

    @Test
    void shouldNotFindTheCheckInWithId() throws Exception {
        BDDMockito.given(checkInBusinessService.getCheckInById(anyLong()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mvc.perform(get(CHECK_IN_5_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedCheckIn() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(anyLong())).willReturn(
                KeyResultMetric.Builder.builder().withId(1L).withKeyResultType(KEY_RESULT_TYPE_ORDINAL).build());
        BDDMockito.given(checkInBusinessService.updateCheckIn(anyLong(), any())).willReturn(checkInMetric);
        BDDMockito.given(checkInMapper.toCheckIn(any())).willReturn(checkInMetric);

        mvc.perform(put(CHECK_IN_5_URL).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content(JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_CHANGE_INFO, Is.is(CHANGE_INFO_1)))
                .andExpect(jsonPath(JSON_PATH_INITIATIVES, Is.is(INITIATIVES_1)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(6)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(1)));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(anyLong())).willReturn(
                KeyResultMetric.Builder.builder().withId(1L).withKeyResultType(KEY_RESULT_TYPE_METRIC).build());
        BDDMockito.given(checkInMapper.toCheckIn(any())).willReturn(checkInMetric);
        BDDMockito.given(checkInBusinessService.updateCheckIn(anyLong(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        mvc.perform(put(CHECK_IN_5_URL).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content(JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldCreateCheckInMetric() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(anyLong())).willReturn(
                KeyResultMetric.Builder.builder().withId(1L).withKeyResultType(KEY_RESULT_TYPE_METRIC).build());
        BDDMockito.given(checkInBusinessService.createCheckIn(any(), any())).willReturn(checkInMetric);

        mvc.perform(post(CHECK_IN_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content(JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_CHANGE_INFO, Is.is(CHANGE_INFO_1)))
                .andExpect(jsonPath(JSON_PATH_INITIATIVES, Is.is(INITIATIVES_1)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(6)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(1)))
                .andExpect(jsonPath(JSON_PATH_VALUE_METRIC, Is.is(46D)));
    }

    @Test
    void shouldCreateCheckInOrdinal() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(anyLong())).willReturn(
                KeyResultMetric.Builder.builder().withId(1L).withKeyResultType(KEY_RESULT_TYPE_ORDINAL).build());
        BDDMockito.given(checkInBusinessService.createCheckIn(any(), any())).willReturn(checkInOrdinal);

        mvc.perform(post(CHECK_IN_BASE_URL).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content(JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath(JSON_PATH_ID, Is.is(4)))
                .andExpect(jsonPath(JSON_PATH_CHANGE_INFO, Is.is(CHANGE_INFO_2)))
                .andExpect(jsonPath(JSON_PATH_INITIATIVES, Is.is(INITIATIVES_2)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(2)))
                .andExpect(jsonPath(JSON_PATH_ZONE, Is.is(Zone.COMMIT.toString())));
    }
}
