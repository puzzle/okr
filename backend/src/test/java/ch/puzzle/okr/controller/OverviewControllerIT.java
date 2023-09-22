package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.overview.*;
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

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static ch.puzzle.okr.models.State.DRAFT;
import static ch.puzzle.okr.models.State.ONGOING;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OverviewController.class)
public class OverviewControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private OverviewService overviewService;

    public static final String PUZZLE = "Puzzle";
    public static final String DESCRIPTION = "This is a description";
    public static final String TEAM_KUCHEN = "Kuchen";
    public static final String QUARTER_LABEL = "GJ 22/23-Q2";
    public static final String KEY_RESULT_TYPE_METRIC = "metric";
    public static final String CHF = "CHF";
    public static final String JSON_PATH_TEAM_NAME = "$[0].team.name";
    public static final String JSON_PATH_TEAM_ID = "$[0].team.id";

    static OverviewDto overviewDtoPuzzle = new OverviewDto(new OverviewTeamDto(1L, PUZZLE), List.of(
            new OverviewObjectiveDto(1L, "Objective 1", DRAFT, new OverviewQuarterDto(1L, QUARTER_LABEL),
                    List.of(new OverviewKeyResultMetricDto(20L, DESCRIPTION, KEY_RESULT_TYPE_METRIC, CHF, 5.0, 20.0,
                            new OverviewLastCheckInMetricDto(40L, 15.0, 5, LocalDateTime.now())))),
            new OverviewObjectiveDto(2L, "Objective 2", ONGOING, new OverviewQuarterDto(1L, QUARTER_LABEL),
                    List.of(new OverviewKeyResultMetricDto(21L, DESCRIPTION, KEY_RESULT_TYPE_METRIC, CHF, 5.0, 20.0,
                            new OverviewLastCheckInMetricDto(41L, 15.0, 5, LocalDateTime.now()))))));
    static OverviewDto overviewDtoOKR = new OverviewDto(new OverviewTeamDto(2L, "OKR"), List.of(
            new OverviewObjectiveDto(5L, "Objective 5", DRAFT, new OverviewQuarterDto(1L, QUARTER_LABEL),
                    List.of(new OverviewKeyResultMetricDto(20L, DESCRIPTION, KEY_RESULT_TYPE_METRIC, CHF, 5.0, 20.0,
                            new OverviewLastCheckInMetricDto(40L, 15.0, 5, LocalDateTime.now())))),
            new OverviewObjectiveDto(7L, "Objective 7", ONGOING, new OverviewQuarterDto(1L, QUARTER_LABEL),
                    List.of(new OverviewKeyResultMetricDto(21L, DESCRIPTION, KEY_RESULT_TYPE_METRIC, CHF, 5.0, 20.0,
                            new OverviewLastCheckInMetricDto(41L, 15.0, 5, LocalDateTime.now()))))));
    static OverviewDto overviewDtoKuchen = new OverviewDto(new OverviewTeamDto(3L, TEAM_KUCHEN),
            List.of(new OverviewObjectiveDto(8L, "Objective 8", ONGOING, new OverviewQuarterDto(1L, QUARTER_LABEL),
                    List.of(new OverviewKeyResultMetricDto(20L, DESCRIPTION, KEY_RESULT_TYPE_METRIC, CHF, 5.0, 20.0,
                            new OverviewLastCheckInMetricDto(40L, 15.0, 5, LocalDateTime.now()))))));
    static OverviewDto overviewDtoFindus = new OverviewDto(new OverviewTeamDto(4L, "Findus"), Collections.emptyList());

    @Test
    void shouldGetAllTeamsWithObjective() throws Exception {
        BDDMockito.given(overviewService.getOverviewByQuarterIdAndTeamIds(2L, List.of(1L, 2L, 3L, 4L)))
                .willReturn(List.of(overviewDtoPuzzle, overviewDtoOKR, overviewDtoKuchen));

        mvc.perform(get("/api/v2/overview?quarter=2&team=1,2,3,4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(3)))
                .andExpect(jsonPath(JSON_PATH_TEAM_ID, Is.is(1)))
                .andExpect(jsonPath(JSON_PATH_TEAM_NAME, Is.is(PUZZLE)))
                .andExpect(jsonPath("$[0].objectives[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].objectives[1].id", Is.is(2))).andExpect(jsonPath("$[1].team.id", Is.is(2)))
                .andExpect(jsonPath("$[1].team.name", Is.is("OKR")))
                .andExpect(jsonPath("$[1].objectives[0].id", Is.is(5)))
                .andExpect(jsonPath("$[1].objectives[1].id", Is.is(7))).andExpect(jsonPath("$[2].team.id", Is.is(3)))
                .andExpect(jsonPath("$[2].team.name", Is.is(TEAM_KUCHEN)))
                .andExpect(jsonPath("$[2].objectives[0].id", Is.is(8)));
    }

    @Test
    void shouldGetAllTeamsWithObjectiveIfNoTeamsExists() throws Exception {
        BDDMockito.given(overviewService.getOverviewByQuarterIdAndTeamIds(any(), any()))
                .willReturn(Collections.emptyList());

        mvc.perform(get("/api/v2/overview").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(0)));
    }

    @Test
    void shouldReturnOnlyFilteredObjecticesByQuarterAndTeam() throws Exception {
        BDDMockito.given(overviewService.getOverviewByQuarterIdAndTeamIds(2L, List.of(1L, 3L)))
                .willReturn(List.of(overviewDtoPuzzle, overviewDtoKuchen));

        mvc.perform(get("/api/v2/overview?quarter=2&team=1,3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TEAM_ID, Is.is(1)))
                .andExpect(jsonPath(JSON_PATH_TEAM_NAME, Is.is(PUZZLE)))
                .andExpect(jsonPath("$[0].objectives[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].objectives[1].id", Is.is(2))).andExpect(jsonPath("$[1].team.id", Is.is(3)))
                .andExpect(jsonPath("$[1].team.name", Is.is(TEAM_KUCHEN)))
                .andExpect(jsonPath("$[1].objectives[0].id", Is.is(8)));
    }

    @Test
    void shouldReturnTeamWithEmptyObjectiveListWhenNoObjectiveInFilteredQuarter() throws Exception {
        BDDMockito.given(overviewService.getOverviewByQuarterIdAndTeamIds(2L, List.of(4L)))
                .willReturn(List.of(overviewDtoFindus));

        mvc.perform(get("/api/v2/overview?quarter=2&team=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk()).andExpect(jsonPath("$", Matchers.hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TEAM_ID, Is.is(4)))
                .andExpect(jsonPath(JSON_PATH_TEAM_NAME, Is.is("Findus")))
                .andExpect(jsonPath("$[0].objectives.size()", Is.is(0)));
    }
}
