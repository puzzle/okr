package ch.puzzle.okr.controller;

import ch.puzzle.okr.mapper.checkin.CheckInMapper;
import ch.puzzle.okr.mapper.keyresult.KeyResultMapper;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
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

import java.util.Collections;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static ch.puzzle.okr.KeyResultTestHelpers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(KeyResultController.class)
class KeyResultControllerIT {

    @MockBean
    KeyResultMapper keyResultMapper;
    @MockBean
    CheckInMapper checkInMapper;
    @MockBean
    KeyResultBusinessService keyResultBusinessService;
    @MockBean
    UserPersistenceService userPersistenceService;
    @MockBean
    ObjectivePersistenceService objectivePersistenceService;
    @Autowired
    private MockMvc mvc;

    @BeforeEach
    void setUp() {
        BDDMockito.given(checkInMapper.toDto(checkIn1)).willReturn(checkInDto1);
        BDDMockito.given(checkInMapper.toDto(checkIn2)).willReturn(checkInDto2);
    }

    @Test
    void shouldGetMetricKeyResultWithId() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(1L)).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultMetricDto);

        mvc.perform(get(URL_TO_KEY_RESULT_1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_ID, Is.is(1)))
                .andExpect(jsonPath(JSON_PATH_DESCRIPTION, Is.is(DESCRIPTION)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_TYPE, Is.is(KEY_RESULT_TYPE_METRIC)))
                .andExpect(jsonPath(JSON_PATH_BASELINE, Is.is(BASELINE_VALUE)))
                .andExpect(jsonPath(JSON_PATH_STRETCH_GOAL, Is.is(STRETCH_GOAL_VALUE)))
                .andExpect(jsonPath(JSON_PATH_UNIT, Is.is(KEY_RESULT_UNIT.toString())))
                .andExpect(jsonPath(JSON_PATH_OWNER_FIRSTNAME, Is.is(FIRSTNAME)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_STATE, Is.is(OBJECTIVE_STATE_ONGOING)))
                .andExpect(jsonPath(JSON_PATH_ID_LAST_CHECK_IN_VALUE, Is.is(4.0)))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_CONFIDENCE, Is.is(CONFIDENCE)))
                .andExpect(jsonPath(JSON_PATH_CREATED_ON, Is.is(KEY_RESULT_CREATED_ON)));
    }

    @Test
    void shouldGetOrdinalKeyResultWithId() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(1L)).willReturn(ordinalKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultOrdinalDto);

        mvc.perform(get(URL_TO_KEY_RESULT_1).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_DESCRIPTION, Is.is(DESCRIPTION)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_TYPE, Is.is(KEY_RESULT_TYPE_ORDINAL)))
                .andExpect(jsonPath(JSON_PATH_OWNER_FIRSTNAME, Is.is(FIRSTNAME)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_ID, Is.is(OBJECTIVE_ID)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_STATE, Is.is(OBJECTIVE_STATE_ONGOING)))
                .andExpect(jsonPath(JSON_PATH_QUARTER_LABEL, Is.is(QUARTER_LABEL)))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_ZONE, Is.is(LAST_CHECK_IN_ZONE.toString())))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_CONFIDENCE, Is.is(CONFIDENCE)))
                .andExpect(jsonPath(JSON_PATH_CREATED_ON, Is.is(KEY_RESULT_CREATED_ON)))
                .andExpect(jsonPath(JSON_PATH_COMMIT_ZONE, Is.is(COMMIT_ZONE)))
                .andExpect(jsonPath(JSON_PATH_TARGET_ZONE, Is.is(TARGET_ZONE)))
                .andExpect(jsonPath(JSON_PATH_STRETCH_ZONE, Is.is(STRETCH_ZONE)));
    }

    @Test
    void shouldNotFindTheKeyResultWithGivenId() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(55L))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 55 not found"));

        mvc.perform(get(URL_TO_KEY_RESULT_55).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCheckInsFromKeyResult() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.getAllCheckInsByKeyResult(5)).willReturn(checkInList);
        BDDMockito.given(this.checkInMapper.toDto(checkIn1)).willReturn(checkInDto1);
        BDDMockito.given(this.checkInMapper.toDto(checkIn2)).willReturn(checkInDto2);

        mvc.perform(get(URL_TO_KEY_RESULT_5_CHECK_IN).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(1))).andExpect(jsonPath("$[0].value", Is.is(23.0)))
                .andExpect(jsonPath("$[0].keyResultId", Is.is(5)))
                .andExpect(jsonPath("$[0].changeInfo", Is.is("Changeinfo1")))
                .andExpect(jsonPath("$[0].initiatives", Is.is("Initiatives1"))).andExpect(jsonPath("$[1].id", Is.is(4)))
                .andExpect(jsonPath("$[1].value", Is.is(12.0))).andExpect(jsonPath("$[1].keyResultId", Is.is(5)))
                .andExpect(jsonPath("$[1].changeInfo", Is.is("Changeinfo2")))
                .andExpect(jsonPath("$[1].initiatives", Is.is("Initiatives2")));
    }

    @Test
    void shouldGetAllCheckInsIfNoCheckInExistsInKeyResult() throws Exception {
        BDDMockito.given(keyResultBusinessService.getAllCheckInsByKeyResult(1)).willReturn(Collections.emptyList());

        mvc.perform(get(URL_TO_KEY_RESULT_1_CHECK_IN).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnErrorWhenKeyResultDoesntExistWhenGettingCheckInsFromKeyResult() throws Exception {
        BDDMockito.given(keyResultBusinessService.getAllCheckInsByKeyResult(1))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));

        mvc.perform(get(URL_TO_KEY_RESULT_1_CHECK_IN).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(status().isNotFound());
    }

    @Test
    void createMetricKeyResult() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.createKeyResult(any(), any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultMetricDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(metricKeyResult);

        mvc.perform(post(URL_BASE).content(CREATE_BODY_METRIC).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_UNIT, Is.is(KEY_RESULT_UNIT.toString())))
                .andExpect(jsonPath(JSON_PATH_DESCRIPTION, Is.is(DESCRIPTION)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_TYPE, Is.is(KEY_RESULT_TYPE_METRIC)))
                .andExpect(jsonPath(JSON_PATH_BASELINE, Is.is(BASELINE_VALUE)))
                .andExpect(jsonPath(JSON_PATH_STRETCH_GOAL, Is.is(STRETCH_GOAL_VALUE)))
                .andExpect(jsonPath(JSON_PATH_OWNER_FIRSTNAME, Is.is(FIRSTNAME)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_ID, Is.is(OBJECTIVE_ID)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_STATE, Is.is(OBJECTIVE_STATE_ONGOING)))
                .andExpect(jsonPath(JSON_PATH_QUARTER_START_DATE, Is.is(START_DATE)))
                .andExpect(jsonPath(JSON_PATH_ID_LAST_CHECK_IN_VALUE, Is.is(4.0)))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_CONFIDENCE, Is.is(CONFIDENCE)));
    }

    @Test
    void createOrdinalKeyResult() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.createKeyResult(any(), any())).willReturn(ordinalKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultOrdinalDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(ordinalKeyResult);

        mvc.perform(post(URL_BASE).content(CREATE_BODY_ORDINAL).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_COMMIT_ZONE, Is.is(COMMIT_ZONE)))
                .andExpect(jsonPath(JSON_PATH_DESCRIPTION, Is.is(DESCRIPTION)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_TYPE, Is.is(KEY_RESULT_TYPE_ORDINAL)))
                .andExpect(jsonPath(JSON_PATH_TARGET_ZONE, Is.is(TARGET_ZONE)))
                .andExpect(jsonPath(JSON_PATH_STRETCH_ZONE, Is.is(STRETCH_ZONE)))
                .andExpect(jsonPath(JSON_PATH_OWNER_FIRSTNAME, Is.is(FIRSTNAME)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_ID, Is.is(OBJECTIVE_ID)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_STATE, Is.is(OBJECTIVE_STATE_ONGOING)))
                .andExpect(jsonPath(JSON_PATH_QUARTER_START_DATE, Is.is(START_DATE)))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_CONFIDENCE, Is.is(CONFIDENCE)));
    }

    @Test
    void shouldThrowException_WhenKeyResultTypeMissing() throws Exception {
        mvc.perform(post(URL_BASE).content(CREATE_BODY_KEY_RESULT_TYPE_MISSING).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void shouldThrowException_WhenKeyResultTypeUnknown() throws Exception {
        mvc.perform(post(URL_BASE).content(CREATE_BODY_KEY_RESULT_TYPE_UNKNOWN).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @Test
    void createKeyResultWithEnumKeys() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.createKeyResult(any(), any())).willReturn(ordinalKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultOrdinalDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(ordinalKeyResult);

        mvc.perform(post(URL_BASE).content(CREATE_BODY_WITH_ENUM_KEYS).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_ID, Is.is(OBJECTIVE_ID)))
                .andExpect(jsonPath(JSON_PATH_DESCRIPTION, Is.is(DESCRIPTION)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_TYPE, Is.is(KEY_RESULT_TYPE_ORDINAL)))
                .andExpect(jsonPath(JSON_PATH_OWNER_FIRSTNAME, Is.is(FIRSTNAME)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_STATE, Is.is(OBJECTIVE_STATE_ONGOING)))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_ZONE, Is.is(LAST_CHECK_IN_ZONE.toString())))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_CONFIDENCE, Is.is(CONFIDENCE)))
                .andExpect(jsonPath(JSON_PATH_CREATED_ON, Is.is(KEY_RESULT_CREATED_ON)))
                .andExpect(jsonPath(JSON_PATH_COMMIT_ZONE, Is.is(COMMIT_ZONE)))
                .andExpect(jsonPath(JSON_PATH_TARGET_ZONE, Is.is(TARGET_ZONE)));
    }

    @Test
    void createKeyResultShouldThrowErrorWhenInvalidDto() throws Exception {
        BDDMockito.given(this.keyResultMapper.toKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Error"));

        mvc.perform(post(URL_BASE).content(CREATE_BODY_ORDINAL).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedKeyResult() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.updateKeyResult(any(), any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultMetricDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultBusinessService.isImUsed(any(), any())).willReturn(false);

        mvc.perform(put(URL_TO_KEY_RESULT_1).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content(JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath(JSON_PATH_TITLE, Is.is(TITLE)))
                .andExpect(jsonPath(JSON_PATH_OWNER_FIRSTNAME, Is.is(FIRSTNAME)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_TYPE, Is.is(KEY_RESULT_TYPE_METRIC)))
                .andExpect(jsonPath(JSON_PATH_BASELINE, Is.is(BASELINE_VALUE)))
                .andExpect(jsonPath(JSON_PATH_STRETCH_GOAL, Is.is(STRETCH_GOAL_VALUE)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_ID, Is.is(OBJECTIVE_ID)))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_ID, Is.is(LAST_CHECK_IN_ID)))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_CONFIDENCE, Is.is(CONFIDENCE)))
                .andExpect(jsonPath(JSON_PATH_UNIT, Is.is(KEY_RESULT_UNIT.toString()))).andReturn();
    }

    @Test
    void shouldReturnUpdatedKeyResultWithImUsed() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.updateKeyResult(any(), any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultMetricDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultBusinessService.isImUsed(any(), any())).willReturn(true);

        mvc.perform(put(URL_TO_KEY_RESULT_1).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content(JSON))
                .andExpect(MockMvcResultMatchers.status().isImUsed()).andExpect(jsonPath(JSON_PATH_TITLE, Is.is(TITLE)))
                .andExpect(jsonPath(JSON_PATH_OWNER_FIRSTNAME, Is.is(FIRSTNAME)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_TYPE, Is.is(KEY_RESULT_TYPE_METRIC)))
                .andExpect(jsonPath(JSON_PATH_BASELINE, Is.is(BASELINE_VALUE)))
                .andExpect(jsonPath(JSON_PATH_STRETCH_GOAL, Is.is(STRETCH_GOAL_VALUE)))
                .andExpect(jsonPath(JSON_PATH_OBJECTIVE_ID, Is.is(OBJECTIVE_ID)))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_ID, Is.is(LAST_CHECK_IN_ID)))
                .andExpect(jsonPath(JSON_PATH_LAST_CHECK_IN_CONFIDENCE, Is.is(CONFIDENCE)))
                .andExpect(jsonPath(JSON_PATH_UNIT, Is.is(KEY_RESULT_UNIT.toString()))).andReturn();
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingKeyResult() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Keyresult not found")).when(keyResultBusinessService)
                .updateKeyResult(any(), any());

        mvc.perform(put(URL_TO_KEY_RESULT_1000).content(PUT_BODY_METRIC).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingKeyResult() throws Exception {
        BDDMockito.given(keyResultBusinessService.updateKeyResult(any(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request while updating keyresult"));

        mvc.perform(put(URL_TO_KEY_RESULT_10).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldDeleteKeyResult() throws Exception {
        mvc.perform(delete(URL_TO_KEY_RESULT_10).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void throwExceptionWhenKeyResultWithIdCantBeFoundWhileDeleting() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Keyresult not found")).when(keyResultBusinessService)
                .deleteKeyResultById(anyLong());

        mvc.perform(delete(URL_TO_KEY_RESULT_1000).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
