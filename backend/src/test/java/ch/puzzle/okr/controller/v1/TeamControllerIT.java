package ch.puzzle.okr.controller.v1;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.RegisterNewUserService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeamController.class)
@Deprecated
class TeamControllerIT {
    private static final Logger logger = LoggerFactory.getLogger(TeamControllerIT.class);

    static Team teamPuzzle = Team.Builder.builder().withId(5L).withName("Puzzle").build();
    static Team teamOKR = Team.Builder.builder().withId(7L).withName("OKR").build();
    static Team teamTestCreating = Team.Builder.builder().withId(1L).withName("TestTeam").build();
    static List<Team> teamList = Arrays.asList(teamPuzzle, teamOKR);
    static Objective objective1 = Objective.Builder.builder().withId(5L).withTitle("Objective 1").build();
    static Objective objective2 = Objective.Builder.builder().withId(7L).withTitle("Objective 2").build();
    static List<Objective> objectiveList = Arrays.asList(objective1, objective2);
    static ObjectiveDto objective1Dto = new ObjectiveDto(5L, "Objective 1", 1L, "Alice", "Wunderland", 1L, "Puzzle", 2L,
            "GJ 22/23-Q2", "This is a description", 20L);
    static ObjectiveDto objective2Dto = new ObjectiveDto(7L, "Objective 2", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1L,
            "GJ 22/23-Q2", "This is a description", 20L);
    static TeamDto teamPuzzleDto = new TeamDto(5L, "Puzzle");
    static TeamDto teamOkrDto = new TeamDto(7L, "OKR");
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TeamBusinessService teamBusinessService;
    @MockBean
    private TeamMapper teamMapper;
    @MockBean
    private ObjectiveBusinessService objectiveBusinessService;
    @MockBean
    private RegisterNewUserService registerNewUserService;

    @MockBean
    private ObjectiveMapper objectiveMapper;

    @BeforeEach
    void setUp() {
        BDDMockito.given(teamMapper.toDto(teamPuzzle)).willReturn(teamPuzzleDto);
        BDDMockito.given(teamMapper.toDto(teamOKR)).willReturn(teamOkrDto);
        BDDMockito.given(objectiveMapper.toDto(objective1)).willReturn(objective1Dto);
        BDDMockito.given(objectiveMapper.toDto(objective2)).willReturn(objective2Dto);
        Mockito.doNothing().when(registerNewUserService).registerNewUser(ArgumentMatchers.any());
    }

    @Test
    void shouldGetAllTeams() throws Exception {
        BDDMockito.given(teamBusinessService.getAllTeams()).willReturn(teamList);

        mvc.perform(get("/api/v1/teams").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2))).andExpect(jsonPath("$[0].id", Is.is(5)))
                .andExpect(jsonPath("$[0].name", Is.is("Puzzle"))).andExpect(jsonPath("$[1].id", Is.is(7)))
                .andExpect(jsonPath("$[1].name", Is.is("OKR")));
    }

    @Test
    void shouldGetAllTeamsIfNoTeamsExists() throws Exception {
        BDDMockito.given(teamBusinessService.getAllTeams()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/teams").contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnTeamWhenCreatingNewTeam() throws Exception {
        TeamDto testTeam = new TeamDto(1L, "TestTeam");

        BDDMockito.given(teamBusinessService.createTeam(any())).willReturn(teamTestCreating);
        BDDMockito.given(teamMapper.toDto(any())).willReturn(testTeam);

        mvc.perform(post("/api/v1/teams").contentType(MediaType.APPLICATION_JSON).content("{\"name\":\" TestTeam \"}")
                .with(SecurityMockMvcRequestPostProcessors.csrf())).andExpect(status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content()
                        .string("{\"id\":1,\"name\":\"TestTeam\",\"activeObjectives\":null}"));
        verify(teamBusinessService, times(1)).createTeam(any());
    }

    @Test
    void shouldReturnResponseStatusExceptionWhenCreatingTeamNullName() throws Exception {
        BDDMockito.given(teamBusinessService.createTeam(any())).willThrow(
                new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute name when creating team"));

        mvc.perform(post("/api/v1/teams").contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 22, \"name\": null}").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnChangedEntity() throws Exception {
        BDDMockito.given(teamBusinessService.updateTeam(anyLong(), any())).willReturn(teamPuzzle);

        mvc.perform(put("/api/v1/teams/5").contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\": 5, \"name\": \"Puzzle\"}").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk()).andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.name", Is.is("Puzzle")));
    }

    @Test
    void shouldReturnNotFound() throws Exception {
        BDDMockito.given(teamBusinessService.updateTeam(anyLong(), any()))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with id 5 not found"));

        mvc.perform(put("/api/v1/teams/5").contentType(MediaType.APPLICATION_JSON)
                .with(SecurityMockMvcRequestPostProcessors.csrf()).content("{\"id\":42,\"title\":\"FullObjective\"}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnListOfObjectives() throws Exception {
        BDDMockito.given(objectiveBusinessService.getObjectivesByTeam(anyLong())).willReturn(objectiveList);

        mvc.perform(get("/api/v1/teams/1/objectives").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5))).andExpect(jsonPath("$[1].id", Is.is(7)));
    }

    @Test
    void shouldDeleteTeam() throws Exception {
        mvc.perform(delete("/api/v1/teams/1").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isOk());
    }

    @Test
    void throwExceptionWhenTeamWithIdCantBeFoundWhileDeleting() throws Exception {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Team not found")).when(teamBusinessService)
                .deleteTeamById(anyLong());

        mvc.perform(delete("/api/v1/teams/10000000").with(SecurityMockMvcRequestPostProcessors.csrf()))
                .andExpect(status().isNotFound());
    }
}
