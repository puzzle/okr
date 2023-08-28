package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.keyResult.KeyResult;
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

import java.time.Instant;
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
    static KeyResult keyResult1 = KeyResult.Builder.builder().withId(5L).withTitle("Keyresult 1").build();
    static Measure measure1 = Measure.Builder.builder().withId(1L).withKeyResult(keyResult1).withCreatedBy(user)
            .withCreatedOn(LocalDateTime.MAX).withChangeInfo("Changeinfo1").withInitiatives("Initiatives1")
            .withValue(23D).withMeasureDate(Instant.parse("2021-11-03T05:55:00.00Z")).build();
    static Measure measure2 = Measure.Builder.builder().withId(4L).withKeyResult(keyResult1).withCreatedBy(user)
            .withCreatedOn(LocalDateTime.MAX).withChangeInfo("Changeinfo2").withInitiatives("Initiatives2")
            .withValue(12D).withMeasureDate(Instant.parse("2022-08-29T22:44:00.00Z")).build();
    static List<Measure> measureList = Arrays.asList(measure1, measure2);
    static MeasureDto measureDto1 = new MeasureDto(1L, 5L, 34D, "Changeinfo1", "Ininitatives1", 1L, LocalDateTime.MAX,
            Instant.parse("2022-03-24T18:45:00.00Z"));
    static MeasureDto measureDto2 = new MeasureDto(4L, 5L, 12D, "Changeinfo2", "Ininitatives2", 1L, LocalDateTime.MAX,
            Instant.parse("2022-10-18T10:33:00.00Z"));
    static KeyResultDto keyResultDto = new KeyResultDto(5L, 5L, "Keyresult", "", 5L, "", "", ExpectedEvolution.INCREASE,
            Unit.PERCENT, 0D, 1D, 0L);
    static Objective objective = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    static KeyResult keyResult = KeyResult.Builder.builder().withId(5L).withTitle("test").withObjective(objective)
            .withOwner(user).build();

    static String createBody = """
            {
                "id":null,
                "objectiveId":5,
                "title":"",
                "description":"",
                "ownerId":5,
                "ownerFirstname":"",
                "ownerLastname":"",
                "quarterId":5,
                "quarterLabel": "GJ 22/23-Q1",
                "expectedEvolution":"INCREASE",
                "unit":"PERCENT",
                "basicValue":0,
                "targetValue":1
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
                "quarterId":5,
                "quarterLabel": "GJ 22/23-Q1",
                "expectedEvolution": 0,
                "unit": 0,
                "basicValue":0,
                "targetValue":1
            }
            """;

    static String putBody = """
            {
                "id":1,
                "objectiveId":5,
                "title":"Updated Keyresult",
                "description":"",
                "ownerId":5,
                "ownerFirstname":"",
                "ownerLastname":"",
                "quarterId":5,
                "quarterLabel": "GJ 22/23-Q1",
                "expectedEvolution":"INCREASE",
                "unit":"PERCENT",
                "basicValue":0,
                "targetValue":1
            }
            """;
    @MockBean
    KeyResultMapper keyResultMapper;
    @MockBean
    MeasureMapper measureMapper;
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
        BDDMockito.given(keyResultMapper.toKeyResult(keyResultDto)).willReturn(keyResult);
        BDDMockito.given(measureMapper.toDto(measure1)).willReturn(measureDto1);
        BDDMockito.given(measureMapper.toDto(measure2)).willReturn(measureDto2);
    }

    @Test
    void shouldGetKeyresultWithId() throws Exception {
        KeyResultDto testKeyResult = new KeyResultDto(1L, 1L, "Program Faster", "Just be faster", 1L, "Rudi", "Grochde",
                ExpectedEvolution.INCREASE, Unit.PERCENT, 4D, 12D, 0L);
        BDDMockito.given(keyResultBusinessService.getKeyResultById(1L)).willReturn(keyResult1);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(testKeyResult);

        mvc.perform(get("/api/v1/keyresults/1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.id", Is.is(1)))
                .andExpect(jsonPath("$.objectiveId", Is.is(1)))
                .andExpect(jsonPath("$.expectedEvolution", Is.is("INCREASE")))
                .andExpect(jsonPath("$.unit", Is.is("PERCENT")));
    }

    @Test
    void shouldNotFindTheKeyresultWithId() throws Exception {
        BDDMockito.given(keyResultBusinessService.getKeyResultById(55L))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 55 not found"));

        mvc.perform(get("/api/v1/keyresults/55").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedKeyResult() throws Exception {
        KeyResult keyResult = KeyResult.Builder.builder().withId(1L).withTitle("Updated Keyresult 1").build();
        KeyResultDto testKeyResult = new KeyResultDto(1L, 1L, "Program Faster", "Just be faster", 1L, "Rudi", "Grochde",
                ExpectedEvolution.INCREASE, Unit.PERCENT, 4D, 12D, 0L);

        BDDMockito.given(keyResultBusinessService.updateKeyResult(any())).willReturn(keyResult);
        BDDMockito.given(keyResultMapper.toDto(any())).willReturn(testKeyResult);
        BDDMockito.given(keyResultMapper.toKeyResult(any())).willReturn(keyResult);

        mvc.perform(put("/api/v1/keyresults/1").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"title\":  \"Updated Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.title", Is.is("Program Faster")))
                .andExpect(jsonPath("$.ownerFirstname", Is.is("Rudi")))
                .andExpect(jsonPath("$.unit", Is.is(Unit.PERCENT.toString()))).andReturn();
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(keyResultBusinessService.updateKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, any()));
        mvc.perform(put("/api/v1/keyresults/10").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"title\":  \"Updated Keyresult 1\"}"))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void createKeyResult() throws Exception {
        KeyResultDto testKeyResult = new KeyResultDto(5L, 1L, "Program Faster", "Just be faster", 1L, "Rudi", "Grochde",
                ExpectedEvolution.INCREASE, Unit.PERCENT, 4D, 12D, 0L);

        BDDMockito.given(this.userPersistenceService.findById(5L)).willReturn(user);
        BDDMockito.given(this.objectivePersistenceService.findById(5L)).willReturn(objective);
        BDDMockito.given(this.keyResultBusinessService.saveKeyResult(any())).willReturn(keyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(testKeyResult);
        BDDMockito.given(keyResultMapper.toKeyResult(any())).willReturn(keyResult);

        mvc.perform(post("/api/v1/keyresults").content(createBody).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.unit", Is.is("PERCENT")))
                .andExpect(jsonPath("$.expectedEvolution", Is.is("INCREASE")));
    }

    @Test
    void createKeyResultWithEnumKeys() throws Exception {
        KeyResultDto testKeyResult = new KeyResultDto(5L, 1L, "Program Faster", "Just be faster", 1L, "Rudi", "Grochde",
                ExpectedEvolution.INCREASE, Unit.PERCENT, 4D, 12D, 0L);

        BDDMockito.given(this.userPersistenceService.findById(5L)).willReturn(user);
        BDDMockito.given(this.objectivePersistenceService.findById(5L)).willReturn(objective);
        BDDMockito.given(this.keyResultBusinessService.saveKeyResult(any())).willReturn(keyResult);
        BDDMockito.given(this.keyResultMapper.toDto(any())).willReturn(testKeyResult);
        BDDMockito.given(keyResultMapper.toKeyResult(any())).willReturn(keyResult);

        mvc.perform(post("/api/v1/keyresults").content(createBodyWithEnumKeys).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.unit", Is.is("PERCENT")))
                .andExpect(jsonPath("$.expectedEvolution", Is.is("INCREASE")));
    }

    @Test
    void invalidDTO() throws Exception {
        BDDMockito.given(this.keyResultMapper.toKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Error"));

        mvc.perform(post("/api/v1/keyresults").content(createBody).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnMeasuresFromKeyResult() throws Exception {
        BDDMockito.given(keyResultBusinessService.getAllMeasuresByKeyResult(5)).willReturn(measureList);

        mvc.perform(get("/api/v1/keyresults/5/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(1))).andExpect(jsonPath("$[0].value", Is.is(34D)))
                .andExpect(jsonPath("$[0].keyResultId", Is.is(5))).andExpect(jsonPath("$[0].createdById", Is.is(1)))
                .andExpect(jsonPath("$[0].changeInfo", Is.is("Changeinfo1")))
                .andExpect(jsonPath("$[0].initiatives", Is.is("Ininitatives1")))
                .andExpect(jsonPath("$[1].id", Is.is(4))).andExpect(jsonPath("$[1].value", Is.is(12D)))
                .andExpect(jsonPath("$[1].createdById", Is.is(1)))
                .andExpect(jsonPath("$[1].changeInfo", Is.is("Changeinfo2")))
                .andExpect(jsonPath("$[1].initiatives", Is.is("Ininitatives2")))
                .andExpect(jsonPath("$[1].keyResultId", Is.is(5)));
    }

    @Test
    void shouldGetAllMeasuresIfNoMeasureExistsInKeyResult() throws Exception {
        BDDMockito.given(keyResultBusinessService.getAllMeasuresByKeyResult(1)).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/keyresults/1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnErrorWhenKeyResultDoesntExistWhenGettingMeasuresFromKeyResult() throws Exception {
        BDDMockito.given(keyResultBusinessService.getAllMeasuresByKeyResult(1))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "KeyResult with id 1 not found"));

        mvc.perform(get("/api/v1/keyresults/1/measures").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound()).andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnUpdatedKeyresult() throws Exception {
        BDDMockito.given(keyResultMapper.toDto(any())).willReturn(keyResultDto);
        BDDMockito.given(keyResultBusinessService.updateKeyResult(any())).willReturn(keyResult);

        mvc.perform(put("/api/v1/keyresults/10").content(putBody).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(5))).andExpect(jsonPath("$.title", Is.is("Keyresult")))
                .andExpect(jsonPath("$.expectedEvolution", Is.is("INCREASE")))
                .andExpect(jsonPath("$.unit", Is.is("PERCENT")));
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingKeyresult() throws Exception {
        BDDMockito.given(keyResultBusinessService.updateKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Keyresult not found"));

        mvc.perform(put("/api/v1/keyresults/1000").content(putBody).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestWhenUpdatingKeyresult() throws Exception {
        BDDMockito.given(keyResultBusinessService.updateKeyResult(any()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request while updating keyresult"));

        mvc.perform(put("/api/v1/keyresults/10").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldDeleteKeyResult() throws Exception {
        mvc.perform(delete("/api/v1/keyresults/10").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }
}
