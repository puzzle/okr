package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.service.OverviewService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OverviewController.class)
public class OverviewControllerIT {
    static OverviewDto overviewDtoPuzzle = new OverviewDto(new TeamDto(1L, "Puzzle"),
            List.of(new ObjectiveDto(1L, "Objective 1", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1L, "GJ 22/23-Q2",
                    "This is a description", 20L),
                    new ObjectiveDto(2L, "Objective 2", 1L, "Alice", "Wunderland", 1L, "Puzzle", 1L, "GJ 22/23-Q2",
                            "This is a description", 20L)));
    static OverviewDto overviewDtoOKR = new OverviewDto(new TeamDto(2L, "OKR"),
            List.of(new ObjectiveDto(5L, "Objective 5", 1L, "Alice", "Wunderland", 2L, "OKR", 1L, "GJ 22/23-Q2",
                    "This is a description", 20L),
                    new ObjectiveDto(7L, "Objective 7", 1L, "Alice", "Wunderland", 2L, "OKR", 1L, "GJ 22/23-Q2",
                            "This is a description", 20L)));
    static OverviewDto overviewDtoKuchen = new OverviewDto(new TeamDto(3L, "Kuchen"), List.of(new ObjectiveDto(8L,
            "Objective 8", 1L, "Alice", "Wunderland", 3L, "Kuchen", 1L, "GJ 22/23-Q2", "This is a description", 20L)));
    static OverviewDto overviewDtoFindus = new OverviewDto(new TeamDto(4L, "Findus"), Collections.emptyList());
    @Autowired
    private MockMvc mvc;
    @MockBean
    private OverviewService overviewService;

    @Test
    void shouldGetAllTeamsWithObjective() throws Exception {
        BDDMockito.given(overviewService.getOverview(Collections.emptyList(), null))
                .willReturn(List.of(overviewDtoPuzzle, overviewDtoOKR, overviewDtoKuchen));

        mvc.perform(get("/api/v1/overview").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath("$[0].team.id", Is.is(1))).andExpect(jsonPath("$[0].team.name", Is.is("Puzzle")))
                .andExpect(jsonPath("$[0].objectives[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].objectives[1].id", Is.is(2))).andExpect(jsonPath("$[1].team.id", Is.is(2)))
                .andExpect(jsonPath("$[1].team.name", Is.is("OKR")))
                .andExpect(jsonPath("$[1].objectives[0].id", Is.is(5)))
                .andExpect(jsonPath("$[1].objectives[1].id", Is.is(7))).andExpect(jsonPath("$[2].team.id", Is.is(3)))
                .andExpect(jsonPath("$[2].team.name", Is.is("Kuchen")))
                .andExpect(jsonPath("$[2].objectives[0].id", Is.is(8)));
    }

    @Test
    void shouldGetAllTeamsWithObjectiveIfNoTeamsExists() throws Exception {
        BDDMockito.given(overviewService.getOverview(any(), any())).willReturn(Collections.emptyList());

        mvc.perform(get("/api/v1/overview").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnOnlyFilteredTeams() throws Exception {
        BDDMockito.given(overviewService.getOverview(List.of(1L, 3L), null))
                .willReturn(List.of(overviewDtoPuzzle, overviewDtoKuchen));

        mvc.perform(get("/api/v1/overview?team=1,3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].team.id", Is.is(1))).andExpect(jsonPath("$[0].team.name", Is.is("Puzzle")))
                .andExpect(jsonPath("$[0].objectives[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].objectives[1].id", Is.is(2))).andExpect(jsonPath("$[1].team.id", Is.is(3)))
                .andExpect(jsonPath("$[1].team.name", Is.is("Kuchen")))
                .andExpect(jsonPath("$[1].objectives[0].id", Is.is(8)));
    }

    @Test
    void shouldReturnOnlyFilteredObjectivesByQuarter() throws Exception {
        BDDMockito.given(overviewService.getOverview(Collections.emptyList(), 1L))
                .willReturn(List.of(overviewDtoPuzzle, overviewDtoKuchen));

        mvc.perform(get("/api/v1/overview?quarter=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath("$[0].team.id", Is.is(1))).andExpect(jsonPath("$[0].team.name", Is.is("Puzzle")))
                .andExpect(jsonPath("$[0].objectives.size()", Is.is(2))).andExpect(jsonPath("$[1].team.id", Is.is(3)))
                .andExpect(jsonPath("$[1].team.name", Is.is("Kuchen")))
                .andExpect(jsonPath("$[1].objectives.size()", Is.is(1)));
    }

    @Test
    void shouldReturnTeamWithEmptyObjectiveListWhenNoObjectiveInFilteredQuarter() throws Exception {
        BDDMockito.given(overviewService.getOverview(Collections.emptyList(), 5L))
                .willReturn(List.of(overviewDtoFindus));

        mvc.perform(get("/api/v1/overview?quarter=5").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath("$[0].team.id", Is.is(4))).andExpect(jsonPath("$[0].team.name", Is.is("Findus")))
                .andExpect(jsonPath("$[0].objectives.size()", Is.is(0)));
    }
}
