package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.TeamService;
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
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.server.ResponseStatusException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeamController.class)
class TeamControllerIT {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeamService teamService;
    @MockBean
    private TeamMapper teamMapper;

    static Team teamPuzzle = Team.Builder.builder().withId(5L).withName("Puzzle").build();
    static Team teamOKR = Team.Builder.builder().withId(7L).withName("OKR").build();
    static List<Team> teamList = Arrays.asList(teamPuzzle, teamOKR);

    static TeamDto teamPuzzleDto = new TeamDto(5L, "Puzzle");
    static TeamDto teamOkrDto = new TeamDto(7L, "OKR");

    @BeforeEach
    void setUp() {
        // setup team mapper
        BDDMockito.given(teamMapper.toDto(teamPuzzle)).willReturn(teamPuzzleDto);
        BDDMockito.given(teamMapper.toDto(teamOKR)).willReturn(teamOkrDto);
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
        BDDMockito.given(teamService.getTeamById(55))
                .willThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Team with id 55 not found"));

        mvc.perform(get("/api/v1/teams/55").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(status().isNotFound())
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

    @Test
    void shouldGetAllTeamsIfNoTeamsExists() throws Exception {
        BDDMockito.given(teamService.getAllTeams()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/teams").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$", Matchers.hasSize(0)))
        ;
    }
}