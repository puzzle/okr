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
    public static final String SUB_URL_USER_5 = "/user/5";
    static Team teamPuzzle = Team.Builder.builder().withId(5L).withName(PUZZLE).build();
    static Team teamOKR = Team.Builder.builder().withId(7L).withName("OKR").build();
    static List<Team> teamList = Arrays.asList(teamPuzzle, teamOKR);
    static TeamDto teamPuzzleDto = new TeamDto(5L, 3, PUZZLE, false);
    static TeamDto teamOkrDto = new TeamDto(7L, 4, "OKR", false);

    private static final String CREATE_NEW_TEAM = """
            {
            "id": null, "name": "OKR-Team", "organisations": []
            }
            """;
    private static final String CREATE_NEW_TEAM_WITH_NULL_VALUES = """
            {
            "id": null, "name": null, "organisations": null
            }
            """;
    private static final String RESPONSE_NEW_TEAM = """
            {"id":7,"version":4,"name":"OKR","writeable":false}""";

    private static final String UPDATE_TEAM = """
            {
            "id": 1, "name": "OKR-Team", "organisations": []
            }
            """;

    private static final String ADD_USERS = """
            [{"id":31,"version":1,"firstname":"Findus","lastname":"Peterson","email":"peterson@puzzle.ch","userTeamList":[{"id":31,"version":1,"team":{"id":8,"version":1,"name":"we are cube.³","isWriteable":false},"isTeamAdmin":true}],"isOkrChampion":false},{"id":41,"version":1,"firstname":"Paco","lastname":"Egiman","email":"egiman@puzzle.ch","userTeamList":[{"id":41,"version":1,"team":{"id":4,"version":1,"name":"/BBT","isWriteable":false},"isTeamAdmin":false}],"isOkrChampion":false}]
            """;

    @Autowired
    private MockMvc mvc;
    @MockBean
    private TeamAuthorizationService teamAuthorizationService;
    @MockBean
    private TeamMapper teamMapper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(teamMapper.toDto(teamPuzzle)).willReturn(teamPuzzleDto);
        BDDMockito.given(teamMapper.toDto(teamOKR)).willReturn(teamOkrDto);
    }

    @Test
    void shouldGetAllTeams() throws Exception {
        BDDMockito.given(teamAuthorizationService.getAllTeams()).willReturn(teamList);

        mvc.perform(get("/api/v2/teams?quarterId=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5))).andExpect(jsonPath("$[0].name", Is.is(PUZZLE)))
                .andExpect(jsonPath("$[1].id", Is.is(7))).andExpect(jsonPath("$[1].name", Is.is("OKR")));
    }

    @Test
    void shouldGetAllTeamsWhenNoQuarterParamIsPassed() throws Exception {
        BDDMockito.given(teamAuthorizationService.getAllTeams()).willReturn(teamList);
        mvc.perform(get(BASE_URL).contentType(MediaType.APPLICATION_JSON)).andExpectAll();
        BDDMockito.verify(teamMapper).toDto(teamOKR);
        BDDMockito.verify(teamMapper).toDto(teamPuzzle);
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
        TeamDto teamDto = new TeamDto(1L, 0, "OKR-Team", false);
        Team team = Team.Builder.builder().withId(1L).withName("OKR-Team").build();

        BDDMockito.given(teamMapper.toDto(any())).willReturn(teamDto);
        BDDMockito.given(teamAuthorizationService.updateEntity(any(), anyLong())).willReturn(team);

        mvc.perform(put(URL_TEAM_1).contentType(MediaType.APPLICATION_JSON).content(UPDATE_TEAM)
                .with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(teamDto.id().intValue())))
                .andExpect(jsonPath("$.version", Is.is(teamDto.version())))
                .andExpect(jsonPath("$.name", Is.is(teamDto.name())));
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

    @Test
    void addUsersToTeam_shouldReturnOk() throws Exception {
        mvc.perform(put(URL_TEAM_1 + "/addusers").contentType(MediaType.APPLICATION_JSON).content(ADD_USERS)
                .with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void removeUserFromTeam_shouldReturnOk() throws Exception {
        mvc.perform(put(URL_TEAM_1 + SUB_URL_USER_5 + "/removeuser").contentType(MediaType.APPLICATION_JSON)
                .content(ADD_USERS).with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    void updateOrAddTeamMembership_shouldReturnOk() throws Exception {
        mvc.perform(put(URL_TEAM_1 + SUB_URL_USER_5 + "/updateaddteammembership/true")
                .contentType(MediaType.APPLICATION_JSON).content(ADD_USERS)
                .with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(MockMvcResultMatchers.status().isOk());
    }
}
