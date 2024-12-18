package ch.puzzle.okr.controller;

import ch.puzzle.okr.deserializer.DeserializerHelper;
import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.mapper.keyresult.KeyResultMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.authorization.ActionAuthorizationService;
import ch.puzzle.okr.service.authorization.AuthorizationService;
import ch.puzzle.okr.service.authorization.ObjectiveAuthorizationService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
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

import static ch.puzzle.okr.test.KeyResultTestHelpers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(ObjectiveController.class)
class ObjectiveControllerIT {
    private static final String OBJECTIVE_TITLE_1 = "Objective 1";
    private static final String OBJECTIVE_TITLE_2 = "Objective 2";
    private static final String DESCRIPTION = "This is our description";
    private static final String UPDATED_DESCRIPTION = "This is a updated description";
    private static final String TITLE = "Hunting";
    private static final String URL_BASE_OBJECTIVE = "/api/v2/objectives";
    private static final String URL_OBJECTIVE_5 = "/api/v2/objectives/5";
    private static final String URL_OBJECTIVE_10 = "/api/v2/objectives/10";
    private static final String URL_DUPLICATE_OBJECTIVE_5 = "/api/v2/objectives/5";
    private static final String JSON = """
            {
               "title": "FullObjective", "ownerId": 1, "ownerFirstname": "Bob", "ownerLastname": "Kaufmann",
               "teamId": 1, "teamName": "Team1", "quarterId": 1, "quarterNumber": 3, "quarterYear": 2020,
               "description": "This is our description", "progress": 33.3
            }
            """;
    private static final String DUPLICATE_OBJECTIVE = """
            {
                "objective": %s,
                "keyResults": [%s,%s]
            }
            """.formatted(JSON, KEY_RESULT_METRIC_JSON, KEY_RESULT_ORDINAL_JSON);

    private static final String CREATE_NEW_OBJECTIVE = """
            {
               "title": "FullObjective", "ownerId": 1, "teamId": 1, "teamName": "Team1",
               "quarterId": 1, "quarterNumber": 3, "quarterYear": 2020, "description": "This is our description"
            }
            """;
    private static final String CREATE_NEW_OBJECTIVE_WITH_NULL_VALUES = """
            {
               "title": null, "ownerId": 1, "ownerFirstname": "Bob", "ownerLastname": "Kaufmann",
               "teamId": 1, "teamName": "Team1", "quarterId": 1, "quarterNumber": 3, "quarterYear": 2020,
               "description": "This is our description", "progress": 33.3
            }
            """;
    private static final String RESPONSE_NEW_OBJECTIVE = """
            {"id":null,"version":1,"title":"Program Faster","teamId":1,"quarterId":1,"quarterLabel":"GJ 22/23-Q2","description":"Just be faster","state":"DRAFT","createdOn":null,"modifiedOn":null,"isWriteable":true}""";
    private static final String JSON_PATH_TITLE = "$.title";
    private static final Objective objective1 = Objective.Builder.builder().withId(5L).withTitle(OBJECTIVE_TITLE_1)
            .build();
    private static final Objective objective2 = Objective.Builder.builder().withId(7L).withTitle(OBJECTIVE_TITLE_2)
            .build();
    private static final User user = User.Builder.builder().withId(1L).withFirstName("Bob").withLastName("Kaufmann")
            .withEmail("kaufmann@puzzle.ch").build();
    private static final Team team = Team.Builder.builder().withId(1L).withName("Team1").build();
    private static final Quarter quarter = Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build();
    private static final Objective fullObjective = Objective.Builder.builder().withId(42L).withTitle("FullObjective")
            .withCreatedBy(user).withTeam(team).withQuarter(quarter).withDescription(DESCRIPTION)
            .withModifiedOn(LocalDateTime.MAX).build();
    private static final ObjectiveDto objective1Dto = new ObjectiveDto(5L, 1, OBJECTIVE_TITLE_1, 1L, 1L, "GJ 22/23-Q2",
            DESCRIPTION, State.DRAFT, LocalDateTime.MAX, LocalDateTime.MAX, true);
    private static final ObjectiveDto objective2Dto = new ObjectiveDto(7L, 1, OBJECTIVE_TITLE_2, 1L, 1L, "GJ 22/23-Q2",
            DESCRIPTION, State.DRAFT, LocalDateTime.MIN, LocalDateTime.MIN, true);

    @Autowired
    private MockMvc mvc;
    @MockBean
    private ObjectiveAuthorizationService objectiveAuthorizationService;
    @Mock
    private AuthorizationService authorizationService;
    @MockBean
    private ActionAuthorizationService actionAuthorizationService;
    @MockBean
    private ObjectiveMapper objectiveMapper;
    @MockBean
    private KeyResultMapper keyResultMapper;
    @MockBean
    private DeserializerHelper deserializerHelper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(objectiveMapper.toDto(objective1)).willReturn(objective1Dto);
        BDDMockito.given(objectiveMapper.toDto(objective2)).willReturn(objective2Dto);
    }

    @Test
    void shouldGetObjectiveById() throws Exception {
        BDDMockito.given(objectiveAuthorizationService.getEntityById(anyLong())).willReturn(objective1);

        mvc.perform(get(URL_OBJECTIVE_5).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath(JSON_PATH_TITLE, Is.is(OBJECTIVE_TITLE_1)));
    }

    @Test
    void shouldThrowNotFoundWhenGettingObjectiveWithNonExistentId() throws Exception {
        BDDMockito.given(objectiveAuthorizationService.getEntityById(anyLong()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mvc.perform(get(URL_OBJECTIVE_10).contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnCreatedObjectiveWhenCreatingNewObjective() throws Exception {
        ObjectiveDto testObjective = new ObjectiveDto(null, 1, "Program Faster", 1L, 1L, "GJ 22/23-Q2",
                "Just be faster", State.DRAFT, null, null, true);

        BDDMockito.given(objectiveMapper.toDto(any())).willReturn(testObjective);
        BDDMockito.given(objectiveAuthorizationService.createEntity(any())).willReturn(fullObjective);

        mvc.perform(post(URL_BASE_OBJECTIVE).contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content(CREATE_NEW_OBJECTIVE))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().string(RESPONSE_NEW_OBJECTIVE));
        verify(objectiveAuthorizationService, times(1)).createEntity(any());
    }

    @Test
    void shouldThrowResponseStatusExceptionWhenCreatingObjectiveWithNullValues() throws Exception {
        BDDMockito.given(objectiveAuthorizationService.createEntity(any())).willThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute title when creating objective"));

        mvc.perform(post(URL_BASE_OBJECTIVE).contentType(MediaType.APPLICATION_JSON)
                .content(CREATE_NEW_OBJECTIVE_WITH_NULL_VALUES).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnUpdatedObjectiveWhenUpdatingObjective() throws Exception {
        ObjectiveDto testObjective = new ObjectiveDto(1L, 1, TITLE, 1L, 1L, "GJ 22/23-Q2", UPDATED_DESCRIPTION,
                State.NOTSUCCESSFUL, LocalDateTime.MIN, LocalDateTime.MAX, true);
        Objective objective = Objective.Builder.builder().withId(1L).withDescription(UPDATED_DESCRIPTION)
                .withTitle(TITLE).build();

        BDDMockito.given(objectiveMapper.toDto(any())).willReturn(testObjective);
        BDDMockito.given(objectiveAuthorizationService.updateEntity(anyLong(), any())).willReturn(objective);
        BDDMockito.given(objectiveAuthorizationService.isImUsed(any())).willReturn(false);

        mvc.perform(put(URL_OBJECTIVE_10).contentType(MediaType.APPLICATION_JSON).content(JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(1))).andExpect(jsonPath("$.description", Is.is(UPDATED_DESCRIPTION)))
                .andExpect(jsonPath(JSON_PATH_TITLE, Is.is(TITLE)));
    }

    @Test
    void shouldReturnImUsed() throws Exception {
        ObjectiveDto testObjectiveDto = new ObjectiveDto(1L, 1, TITLE, 1L, 1L, "GJ 22/23-Q2", UPDATED_DESCRIPTION,
                State.SUCCESSFUL, LocalDateTime.MAX, LocalDateTime.MAX, true);
        Objective objectiveImUsed = Objective.Builder.builder().withId(1L).withDescription(UPDATED_DESCRIPTION)
                .withQuarter(Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build()).withTitle(TITLE)
                .build();

        BDDMockito.given(objectiveMapper.toObjective(any())).willReturn(objectiveImUsed);
        BDDMockito.given(objectiveMapper.toDto(any())).willReturn(testObjectiveDto);
        BDDMockito.given(objectiveAuthorizationService.updateEntity(anyLong(), any())).willReturn(objectiveImUsed);
        BDDMockito.given(objectiveAuthorizationService.isImUsed(any())).willReturn(true);

        mvc.perform(put(URL_OBJECTIVE_10).contentType(MediaType.APPLICATION_JSON).content(JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isImUsed());
    }

    @Test
    void shouldReturnNotFoundWhenUpdatingObjectiveByNonExistentId() throws Exception {
        BDDMockito.given(objectiveAuthorizationService.updateEntity(anyLong(), any())).willThrow(
                new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed objective -> Attribut is invalid"));

        mvc.perform(put(URL_OBJECTIVE_10).contentType(MediaType.APPLICATION_JSON).content(JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        BDDMockito.given(objectiveAuthorizationService.updateEntity(anyLong(), any())).willThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed objective -> Attribut is invalid"));

        mvc.perform(put(URL_OBJECTIVE_10).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldSuccessfullyDeleteObjective() throws Exception {
        mvc.perform(delete(URL_OBJECTIVE_10).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void shouldThrowExceptionWhenObjectiveWithIdCantBeFoundWhileDeleting() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Objective not found"))
                .when(objectiveAuthorizationService).deleteEntityById(anyLong());

        mvc.perform(delete("/api/v2/objectives/1000").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnIsCreatedWhenObjectiveWasDuplicated() throws Exception {
        BDDMockito.given(objectiveAuthorizationService.duplicateEntity(anyLong(), any(), anyList()))
                .willReturn(objective1);
        BDDMockito.given(keyResultMapper.toDto(any(KeyResultMetric.class), any())).willReturn(keyResultMetricDto);
        BDDMockito.given(keyResultMapper.toDto(any(KeyResultOrdinal.class), any())).willReturn(keyResultOrdinalDto);
        BDDMockito.given(objectiveAuthorizationService.getAuthorizationService()).willReturn(authorizationService);
        BDDMockito.given(objectiveMapper.toDto(objective1)).willReturn(objective1Dto);

        mvc.perform(post(URL_DUPLICATE_OBJECTIVE_5).with(SecurityMockMvcRequestPostProcessors.csrf())
                .content(DUPLICATE_OBJECTIVE).contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", Is.is(objective1Dto.id().intValue())))
                .andExpect(jsonPath("$.description", Is.is(objective1Dto.description())))
                .andExpect(jsonPath(JSON_PATH_TITLE, Is.is(objective1Dto.title())));

        verify(objectiveMapper, times(1)).toObjective(any());
        verify(objectiveMapper, times(1)).toDto(any());
    }
}
