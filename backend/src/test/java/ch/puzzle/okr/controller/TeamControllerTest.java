package ch.puzzle.okr.controller;

import ch.puzzle.okr.common.BusinessException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.TeamService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeamController.class)
class TeamControllerIntegrationTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeamService teamService;
    @MockBean
    TeamRepository teamRepository;

    static Team teamPuzzle = Team.Builder.builder().withId(5L).withName("Puzzle").build();
    static Team teamOKR = Team.Builder.builder().withId(7L).withName("OKR").build();
    static List<Team> teamList = Arrays.asList(teamPuzzle, teamOKR);

    @BeforeAll
    static void setUp() {
    }

    @Test
    void shouldGetTheTeamWithId() throws Exception {
        BDDMockito.given(teamService.getTeamById(5)).willReturn(teamPuzzle);

        mvc.perform(get("/api/v1/teams/5").contentType(MediaType.APPLICATION_JSON))
                // example for display the Response
                .andDo((teams) -> System.out.println(teams.getResponse().getContentAsString()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", Is.is(5)))
                .andExpect(jsonPath("$.name", Is.is("Puzzle")))
        ;
    }

    @Test
    void shouldNotFoundTheTeamWithId() throws Exception {
        BDDMockito.given(teamService.getTeamById(55)).willThrow(new BusinessException(404, "Team with id 55 not found"));

        mvc.perform(get("/api/v1/teams/55").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.code", Is.is(404)))
                .andExpect(jsonPath("$.message", Is.is("Team with id 55 not found")))
        ;
    }

    @Test
    void shouldGetAllTeams() throws Exception {
        BDDMockito.given(teamService.getAllTeams()).willReturn(teamList);

        mvc.perform(get("/api/v1/teams").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5)))
                .andExpect(jsonPath("$[0].name", Is.is("Puzzle")))
                .andExpect(jsonPath("$[1].id", Is.is(7)))
                .andExpect(jsonPath("$[1].name", Is.is("OKR")))
        ;
    }
}