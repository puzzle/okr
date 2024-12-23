package ch.puzzle.okr.controller;

import static ch.puzzle.okr.test.CheckInTestHelpers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import ch.puzzle.okr.deserializer.DeserializerHelper;
import ch.puzzle.okr.mapper.checkin.CheckInMapper;
import ch.puzzle.okr.models.checkin.Zone;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.authorization.CheckInAuthorizationService;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import java.time.LocalDateTime;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(CheckInController.class)
class CheckInControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private CheckInAuthorizationService checkInAuthorizationService;
    @MockBean
    private CheckInMapper checkInMapper;
    @MockBean
    private KeyResultBusinessService keyResultBusinessService;
    @SpyBean
    DeserializerHelper deserializerHelper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(checkInMapper.toDto(checkInMetric)).willReturn(checkInMetricDto);
        BDDMockito.given(checkInMapper.toDto(checkInOrdinal)).willReturn(checkInOrdinalDto);
    }

    @DisplayName("Should get metric check-in by id")
    @Test
    void shouldGetMetricCheckInById() throws Exception {
        BDDMockito.given(checkInAuthorizationService.getEntityById(anyLong())).willReturn(checkInMetric);

        mvc
                .perform(get(CHECK_IN_5_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_INITIATIVES, Is.is(INITIATIVES_1)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(6)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(1)))
                .andExpect(jsonPath(JSON_PATH_MODIFIED_ON, Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath(JSON_PATH_CREATED_ON, Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath(JSON_PATH_VALUE, Is.is(46D)));
    }

    @DisplayName("Should get ordinal check-in by id")
    @Test
    void shouldGetOrdinalCheckInById() throws Exception {
        BDDMockito.given(checkInAuthorizationService.getEntityById(anyLong())).willReturn(checkInOrdinal);

        mvc
                .perform(get(CHECK_IN_5_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID, Is.is(4)))
                .andExpect(jsonPath(JSON_PATH_INITIATIVES, Is.is(INITIATIVES_2)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(2)))
                .andExpect(jsonPath(JSON_PATH_MODIFIED_ON, Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath(JSON_PATH_CREATED_ON, Is.is(LocalDateTime.MAX.toString())))
                .andExpect(jsonPath(JSON_PATH_ZONE, Is.is(Zone.COMMIT.toString())));
    }

    @DisplayName("Should throw not found exception when getting a check-in with a non existing id")
    @Test
    void shouldThrowNotFoundWhenGettingCheckInByNonExistingId() throws Exception {
        BDDMockito
                .given(checkInAuthorizationService.getEntityById(anyLong()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mvc
                .perform(get(CHECK_IN_5_URL).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName("Should return updated check-in after successfully updating it")
    @Test
    void shouldReturnUpdatedCheckInAfterSuccessfulUpdate() throws Exception {
        BDDMockito
                .given(keyResultBusinessService.getEntityById(anyLong()))
                .willReturn(KeyResultMetric.Builder.builder().withId(1L).build());
        BDDMockito.given(checkInAuthorizationService.updateEntity(anyLong(), any())).willReturn(checkInMetric);
        BDDMockito.given(checkInMapper.toCheckIn(any())).willReturn(checkInMetric);

        mvc
                .perform(put(CHECK_IN_5_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_CHANGE_INFO, Is.is(CHANGE_INFO_1)))
                .andExpect(jsonPath(JSON_PATH_INITIATIVES, Is.is(INITIATIVES_1)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(6)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(1)));
    }

    @DisplayName("Should throw not found exception when trying to update a non existent check-in")
    @Test
    void shouldThrowNotFoundWhenTryingToUpdateNonExistentCheckIn() throws Exception {
        BDDMockito
                .given(keyResultBusinessService.getEntityById(anyLong()))
                .willReturn(KeyResultMetric.Builder.builder().withId(1L).build());
        BDDMockito.given(checkInMapper.toCheckIn(any())).willReturn(checkInMetric);
        BDDMockito
                .given(checkInAuthorizationService.updateEntity(anyLong(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Not found"));

        mvc
                .perform(put(CHECK_IN_5_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @DisplayName("Should successfully create a new metric key-result")
    @Test
    void shouldSuccessfullyCreateKeyResultMetric() throws Exception {
        BDDMockito
                .given(keyResultBusinessService.getEntityById(anyLong()))
                .willReturn(KeyResultMetric.Builder.builder().withId(1L).build());
        BDDMockito.given(checkInAuthorizationService.createEntity(any())).willReturn(checkInMetric);

        mvc
                .perform(post(CHECK_IN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath(JSON_PATH_ID, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_CHANGE_INFO, Is.is(CHANGE_INFO_1)))
                .andExpect(jsonPath(JSON_PATH_INITIATIVES, Is.is(INITIATIVES_1)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(6)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(1)))
                .andExpect(jsonPath(JSON_PATH_VALUE, Is.is(46D)));
    }

    @DisplayName("Should successfully create a new metric key-result")
    @Test
    void shouldSuccessfullyCreateKeyResultOrdinal() throws Exception {
        BDDMockito
                .given(keyResultBusinessService.getEntityById(anyLong()))
                .willReturn(KeyResultOrdinal.Builder.builder().withId(1L).build());
        BDDMockito.given(checkInAuthorizationService.createEntity(any())).willReturn(checkInOrdinal);

        mvc
                .perform(post(CHECK_IN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath(JSON_PATH_ID, Is.is(4)))
                .andExpect(jsonPath(JSON_PATH_CHANGE_INFO, Is.is(CHANGE_INFO_2)))
                .andExpect(jsonPath(JSON_PATH_INITIATIVES, Is.is(INITIATIVES_2)))
                .andExpect(jsonPath(JSON_PATH_CONFIDENCE, Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_KEY_RESULT_ID, Is.is(2)))
                .andExpect(jsonPath(JSON_PATH_ZONE, Is.is(Zone.COMMIT.toString())));
    }

    @DisplayName("Should throw client error when creating a check-in but the key-result id is missing")
    @Test
    void shouldThrowClientErrorWhenCreatingCheckInButKeyResultIdMissing() throws Exception {
        BDDMockito
                .given(keyResultBusinessService.getEntityById(anyLong()))
                .willReturn(KeyResultMetric.Builder.builder().withId(1L).build());
        BDDMockito.given(checkInAuthorizationService.createEntity(any())).willReturn(checkInOrdinal);

        mvc
                .perform(post(CHECK_IN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(JSON_WITHOUT_KEY_RESULT_ID))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @DisplayName("Should throw client error for KeyResult not of type metric or ordinal")
    @Test
    void shouldThrowClientErrorForKeyResultNotOfTypeMetricOrOrdinal() throws Exception {
        class NonMetricOrOrdinalKeyResult extends KeyResult {
        }

        BDDMockito
                .given(keyResultBusinessService.getEntityById(anyLong()))
                .willReturn(new NonMetricOrOrdinalKeyResult());

        mvc
                .perform(post(CHECK_IN_BASE_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(SecurityMockMvcRequestPostProcessors.csrf())
                        .content(JSON))
                .andExpect(MockMvcResultMatchers.status().is4xxClientError());
    }

    @DisplayName("Should successfully delete check-in by id")
    @Test
    void shouldSuccessfullyDeleteCheckInById() throws Exception {
        mvc
                .perform(delete(CHECK_IN_5_URL).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
