package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.RegisterNewUserService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(TeamController.class)
class TeamControllerIT {

    public static final String PUZZLE = "Puzzle";
    static Team teamPuzzle = Team.Builder.builder().withId(5L).withName(PUZZLE).build();
    static Team teamOKR = Team.Builder.builder().withId(7L).withName("OKR").build();
    static List<Team> teamList = Arrays.asList(teamPuzzle, teamOKR);
    static TeamDto teamPuzzleDto = new TeamDto(5L, PUZZLE, 1);
    static TeamDto teamOkrDto = new TeamDto(7L, "OKR", 0);
    @Autowired
    private MockMvc mvc;
    @MockBean
    private TeamBusinessService teamBusinessService;
    @MockBean
    private TeamMapper teamMapper;
    @MockBean
    private RegisterNewUserService registerNewUserService;

    @BeforeEach
    void setUp() {
        BDDMockito.given(teamMapper.toDto(teamPuzzle, 1L)).willReturn(teamPuzzleDto);
        BDDMockito.given(teamMapper.toDto(teamOKR, 1L)).willReturn(teamOkrDto);
        Mockito.doNothing().when(registerNewUserService).registerNewUser(any());
    }

    @Test
    void shouldGetAllTeams() throws Exception {
        BDDMockito.given(teamBusinessService.getAllTeams()).willReturn(teamList);

        mvc.perform(get("/api/v2/teams?quarterId=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].id", Is.is(5))).andExpect(jsonPath("$[0].name", Is.is(PUZZLE)))
                .andExpect(jsonPath("$[0].activeObjectives", Is.is(1))).andExpect(jsonPath("$[1].id", Is.is(7)))
                .andExpect(jsonPath("$[1].name", Is.is("OKR"))).andExpect(jsonPath("$[1].activeObjectives", Is.is(0)));
        System.out.println(jsonPath("$"));
    }

    @Test
    void shouldGetAllTeamsWhenNoQuarterParamIsPassed() throws Exception {
        BDDMockito.given(teamBusinessService.getAllTeams()).willReturn(teamList);
        mvc.perform(get("/api/v2/teams").contentType(MediaType.APPLICATION_JSON)).andExpectAll();
        BDDMockito.verify(teamMapper).toDto(teamOKR, null);
        BDDMockito.verify(teamMapper).toDto(teamPuzzle, null);
    }

    @Test
    void shouldGetAllTeamsIfTeamModelIsNull() throws Exception {
        BDDMockito.given(teamBusinessService.getAllTeams()).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v2/teams?quarterId=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }
}