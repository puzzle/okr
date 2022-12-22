package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.*;
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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(OverviewController.class)
public class OverviewControllerIT {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private TeamService teamService;
    @MockBean
    private OverviewMapper overviewMapper;

    static Team teamPuzzle = Team.Builder.builder().withId(1L).withName("Puzzle").build();
    static Team teamOKR = Team.Builder.builder().withId(2L).withName("OKR").build();
    static List<Team> teamList = Arrays.asList(teamPuzzle, teamOKR);

    static OverviewDto overviewDtoPuzzle = new OverviewDto(new TeamDto(1L, "Puzzle"),
            List.of(new ObjectiveDto(1L, "Objective 1", 1L, "Alice", "Wunderland", 1L, "Puzzle", 2L,
                    "GJ 22/23-Q2", "This is a description", 20L), new ObjectiveDto(2L, "Objective 2", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1L,
                    "GJ 22/23-Q2", "This is a description", 20L)));

    static OverviewDto overviewDtoOKR = new OverviewDto(new TeamDto(2L, "OKR"),
            List.of(new ObjectiveDto(5L, "Objective 5", 1L, "Alice", "Wunderland", 1L, "Puzzle", 2L,
                    "GJ 22/23-Q2", "This is a description", 20L), new ObjectiveDto(7L, "Objective 7", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1L,
                    "GJ 22/23-Q2", "This is a description", 20L)));

    @BeforeEach
    void setUp() {
        // setup overview mapper
        BDDMockito.given(overviewMapper.toDto(teamPuzzle)).willReturn(overviewDtoPuzzle);
        BDDMockito.given(overviewMapper.toDto(teamOKR)).willReturn(overviewDtoOKR);
    }

    @Test
    void shouldGetAllTeamsWithObjective() throws Exception {
        BDDMockito.given(teamService.getAllTeams()).willReturn(teamList);

        mvc.perform(get("/api/v1/overview").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].team.id", Is.is(1))).andExpect(jsonPath("$[0].team.name", Is.is("Puzzle")))
                .andExpect(jsonPath("$[0].objectives[0].id", Is.is(1))).andExpect(jsonPath("$[0].objectives[1].id", Is.is(2)))
                .andExpect(jsonPath("$[1].team.id", Is.is(2))).andExpect(jsonPath("$[1].team.name", Is.is("OKR")))
                .andExpect(jsonPath("$[1].objectives[0].id", Is.is(5))).andExpect(jsonPath("$[1].objectives[1].id", Is.is(7)));
    }

    @Test
    void shouldGetAllTeamsWithObjectiveIfNoTeamsExists() throws Exception {
        BDDMockito.given(teamService.getAllTeams()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/overview").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }
}
