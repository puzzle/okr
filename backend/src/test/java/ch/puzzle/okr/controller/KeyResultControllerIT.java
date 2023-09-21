package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.checkin.CheckInMetricDto;
import ch.puzzle.okr.dto.keyresult.*;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.checkin.CheckInMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.checkin.CheckInMetric;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    static User user = User.Builder.builder().withId(1L).withFirstname("Bob").withLastname("Kaufmann")
            .withUsername("bkaufmann").withEmail("kaufmann@puzzle.ch").build();
    static KeyResult metricKeyResult = KeyResultMetric.Builder.builder().withId(5L).withTitle("Keyresult 1").build();
    static CheckIn checkIn1 = CheckInMetric.Builder.builder().withValue(23D).withId(1L).withKeyResult(metricKeyResult)
            .withCreatedBy(user).withCreatedOn(LocalDateTime.MAX).withChangeInfo("Changeinfo1")
            .withInitiatives("Initiatives1").build();
    static CheckIn checkIn2 = CheckInMetric.Builder.builder().withValue(12D).withId(4L).withKeyResult(metricKeyResult)
            .withCreatedBy(user).withCreatedOn(LocalDateTime.MAX).withChangeInfo("Changeinfo2")
            .withInitiatives("Initiatives2").build();
    static List<CheckIn> checkInList = Arrays.asList(checkIn1, checkIn2);

    static CheckInDto checkInDto1 = new CheckInMetricDto(1L, "Changeinfo1", "Initiatives1", 6, metricKeyResult.getId(),
            LocalDateTime.MAX, LocalDateTime.MAX, 23D);
    static CheckInDto checkInDto2 = new CheckInMetricDto(4L, "Changeinfo2", "Initiatives2", 5, metricKeyResult.getId(),
            LocalDateTime.MAX, LocalDateTime.MAX, 12D);

    static KeyResultUserDto keyResultUserDto = new KeyResultUserDto(1L, "Johnny", "Appleseed");
    static KeyResultQuarterDto keyResultQuarterDto = new KeyResultQuarterDto(1L, "GJ 22/23-Q4", LocalDate.MIN,
            LocalDate.MAX);
    static KeyResultLastCheckInMetricDto keyResultLastCheckInDto = new KeyResultLastCheckInMetricDto(1L, 4.0, 6,
            LocalDateTime.MIN, "ChangeInfo1", "Initiatives1");
    static KeyResultLastCheckInOrdinalDto keyResultLastCheckInOrdinalDto = new KeyResultLastCheckInOrdinalDto(1L,
            "Baum", 6, LocalDateTime.MIN, "Changeinfo2", "Initiatives2");
    static KeyResultObjectiveDto keyResultObjectiveDto = new KeyResultObjectiveDto(1L, "ONGOING", keyResultQuarterDto);

    static KeyResultMetricDto keyResultMetricDto = new KeyResultMetricDto(5L, "metric", "Keyresult 1", "Description",
            1.0, 5.0, "ECTS", keyResultUserDto, keyResultObjectiveDto, keyResultLastCheckInDto, LocalDateTime.MIN,
            LocalDateTime.MAX);
    static KeyResultOrdinalDto keyResultOrdinalDto = new KeyResultOrdinalDto(5L, "ordinal", "Keyresult 1",
            "Description", "Eine Pflanze", "Ein Baum", "Ein Wald", keyResultUserDto, keyResultObjectiveDto,
            keyResultLastCheckInOrdinalDto, LocalDateTime.MIN, LocalDateTime.MAX);
    static Objective objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    static KeyResult ordinalKeyResult = KeyResultOrdinal.Builder.builder().withId(3L).withTitle("Keyresult 2")
            .withOwner(user).withObjective(objective).build();

    static String createBodyMetric = """
            {
                "id":null,
                "objectiveId":5,
                "title":"",
                "description":"",
                "ownerId":5,
               "createdById":5,
               "createdOn":null,
               "modifiedOn":null,
               "baseline":2.0,
               "stretchGoal":5.0,
               "unit":"PT"
            }
            """;

    static String createBodyOrdinal = """
            {
                "id":null,
                "objectiveId":5,
                "title":"",
                "description":"",
                "ownerId":5,
               "createdById":5,
               "createdOn":null,
               "modifiedOn":null,
               "commitZone":"Eine Pflanze",
               "targetZone":"Ein Baum",
               "stretchZone":"Ein Wald"
            }
            """;

    static String createBodyWithEnumKeys = """
            {
                "id":null,
                "objectiveId":5,
                "title":"",
                "description":"",
                "ownerId":5,
                "ownerFirstname":"",
                "ownerLastname":"",
                "createdById":5,
               "createdByFirstname":"",
               "createdByLastname":"",
               "createdOn":null,
               "modifiedOn":null,
               "baseline":2.0,
               "stretchGoal":5.0,
               "unit":""
            }
            """;

    static String putBodyMetric = """
            {
                "id":1,
                "title":"Updated Keyresult",
                "description":"",
                "baseline":2.0,
                "stretchGoal":5.0,
                "unit":"ECTS",
                "ownerId":5,
                "ownerFirstname":"",
                "ownerLastname":"",
                "objectiveId":5,
                "objectiveState":"INPROGRESS",
                "objectiveQuarterId":1,
                "objectiveQuarterLabel":"GJ 22/23-Q3",
                "objectiveQuarterStartDate":null,
                "objectiveQuarterEndDate":null,
                "lastCheckInId":1,
                "lastCheckInValue":4.0,
                "lastCheckInConfidence":6,
                "lastCheckInCreatedOn":null,
                "lastCheckInComment":"",
                "createdById":5,
                "createdByFirstname":"",
                "createdByLastname":"",
                "createdOn":null,
                "modifiedOn":null
            }
            """;
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

        mvc.perform(get("/api/v2/keyresults/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.objective.id", Is.is(1)))
                .andExpect(jsonPath("$.description", Is.is("Description")))
                .andExpect(jsonPath("$.keyResultType", Is.is("metric"))).andExpect(jsonPath("$.baseline", Is.is(1.0)))
                .andExpect(jsonPath("$.stretchGoal", Is.is(5.0))).andExpect(jsonPath("$.unit", Is.is("ECTS")))
                .andExpect(jsonPath("$.owner.firstname", Is.is("Johnny")))
                .andExpect(jsonPath("$.objective.state", Is.is("ONGOING")))
                .andExpect(jsonPath("$.lastCheckIn.value", Is.is(4.0)))
                .andExpect(jsonPath("$.lastCheckIn.confidence", Is.is(6)))
                .andExpect(jsonPath("$.createdOn", Is.is("-999999999-01-01T00:00:00")));
    }

    @Test
    void shouldGetOrdinalKeyResultWithId() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(1L)).willReturn(ordinalKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultOrdinalDto);

        mvc.perform(get("/api/v2/keyresults/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.description", Is.is("Description")))
                .andExpect(jsonPath("$.keyResultType", Is.is("ordinal")))
                .andExpect(jsonPath("$.owner.firstname", Is.is("Johnny")))
                .andExpect(jsonPath("$.objective.id", Is.is(1)))
                .andExpect(jsonPath("$.objective.state", Is.is("ONGOING")))
                .andExpect(jsonPath("$.objective.keyResultQuarterDto.label", Is.is("GJ 22/23-Q4")))
                .andExpect(jsonPath("$.lastCheckIn.zone", Is.is("Baum")))
                .andExpect(jsonPath("$.lastCheckIn.confidence", Is.is(6)))
                .andExpect(jsonPath("$.createdOn", Is.is("-999999999-01-01T00:00:00")))
                .andExpect(jsonPath("$.commitZone", Is.is("Eine Pflanze")))
                .andExpect(jsonPath("$.targetZone", Is.is("Ein Baum")))
                .andExpect(jsonPath("$.stretchZone", Is.is("Ein Wald")));
    }

    @Test
    void shouldNotFindTheKeyResultWithGivenId() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(55L))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 55 not found"));

        mvc.perform(get("/api/v2/keyresults/55").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnCheckInsFromKeyResult() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.getAllCheckInsByKeyResult(5)).willReturn(checkInList);
        BDDMockito.given(this.checkInMapper.toDto(checkIn1)).willReturn(checkInDto1);
        BDDMockito.given(this.checkInMapper.toDto(checkIn2)).willReturn(checkInDto2);

        mvc.perform(get("/api/v2/keyresults/5/checkins").contentType(MediaType.APPLICATION_JSON))
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

        mvc.perform(get("/api/v2/keyresults/1/checkins").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnErrorWhenKeyResultDoesntExistWhenGettingCheckInsFromKeyResult() throws Exception {
        BDDMockito.given(keyResultBusinessService.getAllCheckInsByKeyResult(1))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));

        mvc.perform(get("/api/v2/keyresults/1/checkins").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(status().isNotFound());
    }

    @Test
    void createMetricKeyResult() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.createKeyResult(any(), any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultMetricDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(metricKeyResult);

        mvc.perform(post("/api/v2/keyresults").content(createBodyMetric).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.unit", Is.is("ECTS"))).andExpect(jsonPath("$.description", Is.is("Description")))
                .andExpect(jsonPath("$.keyResultType", Is.is("metric"))).andExpect(jsonPath("$.baseline", Is.is(1.0)))
                .andExpect(jsonPath("$.stretchGoal", Is.is(5.0)))
                .andExpect(jsonPath("$.owner.firstname", Is.is("Johnny")))
                .andExpect(jsonPath("$.objective.id", Is.is(1)))
                .andExpect(jsonPath("$.objective.state", Is.is("ONGOING")))
                .andExpect(jsonPath("$.objective.keyResultQuarterDto.startDate", Is.is("-999999999-01-01")))
                .andExpect(jsonPath("$.lastCheckIn.value", Is.is(4.0)))
                .andExpect(jsonPath("$.lastCheckIn.confidence", Is.is(6)));
    }

    @Test
    void createOrdinalKeyResult() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.createKeyResult(any(), any())).willReturn(ordinalKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultOrdinalDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(ordinalKeyResult);

        mvc.perform(post("/api/v2/keyresults").content(createBodyOrdinal).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.commitZone", Is.is("Eine Pflanze")))
                .andExpect(jsonPath("$.description", Is.is("Description")))
                .andExpect(jsonPath("$.keyResultType", Is.is("ordinal")))
                .andExpect(jsonPath("$.targetZone", Is.is("Ein Baum")))
                .andExpect(jsonPath("$.stretchZone", Is.is("Ein Wald")))
                .andExpect(jsonPath("$.owner.firstname", Is.is("Johnny")))
                .andExpect(jsonPath("$.objective.id", Is.is(1)))
                .andExpect(jsonPath("$.objective.state", Is.is("ONGOING")))
                .andExpect(jsonPath("$.objective.keyResultQuarterDto.startDate", Is.is("-999999999-01-01")))
                .andExpect(jsonPath("$.lastCheckIn.confidence", Is.is(6)));
    }

    @Test
    void createKeyResultWithEnumKeys() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.createKeyResult(any(), any())).willReturn(ordinalKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultOrdinalDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(ordinalKeyResult);

        mvc.perform(post("/api/v2/keyresults").content(createBodyWithEnumKeys).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.objective.id", Is.is(1)))
                .andExpect(jsonPath("$.description", Is.is("Description")))
                .andExpect(jsonPath("$.keyResultType", Is.is("ordinal")))
                .andExpect(jsonPath("$.owner.firstname", Is.is("Johnny")))
                .andExpect(jsonPath("$.objective.state", Is.is("ONGOING")))
                .andExpect(jsonPath("$.lastCheckIn.zone", Is.is("Baum")))
                .andExpect(jsonPath("$.lastCheckIn.confidence", Is.is(6)))
                .andExpect(jsonPath("$.createdOn", Is.is("-999999999-01-01T00:00:00")))
                .andExpect(jsonPath("$.commitZone", Is.is("Eine Pflanze")))
                .andExpect(jsonPath("$.targetZone", Is.is("Ein Baum")));
    }

    @Test
    void createKeyResultShouldThrowErrorWhenInvalidDto() throws Exception {
        BDDMockito.given(this.keyResultMapper.toKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Error"));

        mvc.perform(post("/api/v2/keyresults").content(createBodyOrdinal).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedKeyResult() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.updateKeyResult(any(), any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultMetricDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultBusinessService.isImUsed(any(), any())).willReturn(false);

        mvc.perform(put("/api/v2/keyresults/1").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"title\":  \"Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.title", Is.is("Keyresult 1")))
                .andExpect(jsonPath("$.owner.firstname", Is.is("Johnny")))
                .andExpect(jsonPath("$.keyResultType", Is.is("metric"))).andExpect(jsonPath("$.baseline", Is.is(1.0)))
                .andExpect(jsonPath("$.stretchGoal", Is.is(5.0))).andExpect(jsonPath("$.objective.id", Is.is(1)))
                .andExpect(jsonPath("$.lastCheckIn.id", Is.is(1)))
                .andExpect(jsonPath("$.lastCheckIn.confidence", Is.is(6))).andExpect(jsonPath("$.unit", Is.is("ECTS")))
                .andReturn();
    }

    @Test
    void shouldReturnUpdatedKeyResultWithImUsed() throws Exception {
        BDDMockito.given(this.keyResultBusinessService.updateKeyResult(any(), any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(keyResultMetricDto);
        BDDMockito.given(this.keyResultMapper.toKeyResult(any())).willReturn(metricKeyResult);
        BDDMockito.given(this.keyResultBusinessService.isImUsed(any(), any())).willReturn(true);

        mvc.perform(put("/api/v2/keyresults/1").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"title\":  \"Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isImUsed())
                .andExpect(jsonPath("$.title", Is.is("Keyresult 1")))
                .andExpect(jsonPath("$.owner.firstname", Is.is("Johnny")))
                .andExpect(jsonPath("$.keyResultType", Is.is("metric"))).andExpect(jsonPath("$.baseline", Is.is(1.0)))
                .andExpect(jsonPath("$.stretchGoal", Is.is(5.0))).andExpect(jsonPath("$.objective.id", Is.is(1)))
                .andExpect(jsonPath("$.lastCheckIn.id", Is.is(1)))
                .andExpect(jsonPath("$.lastCheckIn.confidence", Is.is(6))).andExpect(jsonPath("$.unit", Is.is("ECTS")))
                .andReturn();
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingKeyResult() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Keyresult not found")).when(keyResultBusinessService)
                .updateKeyResult(any(), any());

        mvc.perform(put("/api/v2/keyresults/1000").content(putBodyMetric).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingKeyResult() throws Exception {
        BDDMockito.given(keyResultBusinessService.updateKeyResult(any(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request while updating keyresult"));

        mvc.perform(put("/api/v2/keyresults/10").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldDeleteKeyResult() throws Exception {
        mvc.perform(delete("/api/v2/keyresults/10").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void throwExceptionWhenKeyResultWithIdCantBeFoundWhileDeleting() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Keyresult not found")).when(keyResultBusinessService)
                .deleteKeyResultById(anyLong());

        mvc.perform(delete("/api/v2/keyresults/1000").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}
