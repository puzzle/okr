package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.authorization.TeamAuthorizationService;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeamController.class)
class TeamControllerIT {

    private static final String BASE_URL = "/api/v2/teams";
    private static final String URL_TEAM_1 = "/api/v2/teams/1";
    public static final String PUZZLE = "Puzzle";
    static Team teamPuzzle = Team.Builder.builder().withId(5L).withName(PUZZLE).build();
    static Team teamOKR = Team.Builder.builder().withId(7L).withName("OKR").build();
    static List<Team> teamList = Arrays.asList(teamPuzzle, teamOKR);
    static TeamDto teamPuzzleDto = new TeamDto(5L, 3, PUZZLE, 1, new ArrayList<>());
    static TeamDto teamOkrDto = new TeamDto(7L, 4, "OKR", 0, new ArrayList<>());

    private static final String CREATE_NEW_TEAM = """
            {
               "id": null, "name": "OKR-Team", "organisations": [], "activeObjectives": 0
            }
            """;
    private static final String CREATE_NEW_TEAM_WITH_NULL_VALUES = """
            {
               "id": null, "name": null, "organisations": null, "activeObjectives": null
            }
            """;
    private static final String RESPONSE_NEW_TEAM = """
            {"id":7,"version":4,"name":"OKR","activeObjectives":0,"organisations":[]}""";

    private static final String UPDATE_TEAM = """
            {
                "id": 1, "name": "OKR-Team", "organisations": [], "activeObjectives": 0
            }
            """;

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TeamAuthorizationService teamAuthorizationService;
    @MockBean
    private TeamMapper teamMapper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(teamMapper.toDto(teamPuzzle, null)).willReturn(teamPuzzleDto);
        BDDMockito.given(teamMapper.toDto(teamOKR, null)).willReturn(teamOkrDto);

        BDDMockito.given(teamMapper.toDto(teamPuzzle, 1L)).willReturn(teamPuzzleDto);
        BDDMockito.given(teamMapper.toDto(teamOKR, 1L)).willReturn(teamOkrDto);
    }

    @Test
    void shouldGetAllTeams() throws Exception {
        BDDMockito.given(teamAuthorizationService.getAllTeams()).willReturn(teamList);

        mvc.perform(get("/api/v2/teams?quarterId=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5))).andExpect(jsonPath("$[0].name", Is.is(PUZZLE)))
                .andExpect(jsonPath("$[0].activeObjectives", Is.is(1))).andExpect(jsonPath("$[1].id", Is.is(7)))
                .andExpect(jsonPath("$[1].name", Is.is("OKR"))).andExpect(jsonPath("$[1].activeObjectives", Is.is(0)));
    }

    @Test
    void shouldGetAllTeamsWhenNoQuarterParamIsPassed() throws Exception {
        BDDMockito.given(teamAuthorizationService.getAllTeams()).willReturn(teamList);
        mvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON)).andExpectAll();
        BDDMockito.verify(teamMapper).toDto(teamOKR, null);
        BDDMockito.verify(teamMapper).toDto(teamPuzzle, null);
    }

    @Test
    void shouldGetAllTeamsIfTeamModelIsNull() throws Exception {
        BDDMockito.given(teamAuthorizationService.getAllTeams()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v2/teams?quarterId=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnCreatedTeam() throws Exception {
        BDDMockito.given(teamAuthorizationService.createEntity(any())).willReturn(teamOKR);

        mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(CREATE_NEW_TEAM)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().string(RESPONSE_NEW_TEAM));
    }

    @Test
    void shouldReturnResponseStatusExceptionWhenCreatingObjectiveWithNullValues() throws Exception {
        BDDMockito.given(teamAuthorizationService.createEntity(any())).willThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute name when creating team"));

        mvc.perform(post(BASE_URL).contentType(MediaType.APPLICATION_JSON).content(CREATE_NEW_TEAM_WITH_NULL_VALUES)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldReturnUpdatedTeam() throws Exception {
        TeamDto teamDto = new TeamDto(1L, 3, "OKR-Team", 0, new ArrayList<>());
        Team team = Team.Builder.builder().withId(1L).withVersion(4).withName("OKR-Team")
                .withAuthorizationOrganisation(new ArrayList<>()).build();

        BDDMockito.given(teamMapper.toDto(any(), any())).willReturn(teamDto);
        BDDMockito.given(teamAuthorizationService.updateEntity(any(), anyLong())).willReturn(team);

        mvc.perform(put(URL_TEAM_1).contentType(MediaType.APPLICATION_JSON).content(UPDATE_TEAM)
                .with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(teamDto.id().intValue())))
                .andExpect(jsonPath("$.version", Is.is(teamDto.version())))
                .andExpect(jsonPath("$.name", Is.is(teamDto.name())))
                .andExpect(jsonPath("$.activeObjectives", Is.is(teamDto.activeObjectives())));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(teamAuthorizationService.updateEntity(any(), anyLong()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Failed team -> Attribut is invalid"));

        mvc.perform(put(URL_TEAM_1).contentType(MediaType.APPLICATION_JSON).content(UPDATE_TEAM)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void shouldReturnBadRequest() throws Exception {
        BDDMockito.given(teamAuthorizationService.updateEntity(any(), anyLong()))
                .willThrow(new ResponseStatusException(HttpStatus.BAD_REQUEST, "Failed team -> Attribut is invalid"));

        mvc.perform(put(URL_TEAM_1).contentType(MediaType.APPLICATION_JSON).content(UPDATE_TEAM)
                .with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    void shouldDeleteTeam() throws Exception {
        mvc.perform(delete(URL_TEAM_1).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void throwExceptionWhenOTeamWithIdCantBeFoundWhileDeleting() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")).when(teamAuthorizationService)
                .deleteEntity(anyLong());
        mvc.perform(delete(URL_TEAM_1).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}