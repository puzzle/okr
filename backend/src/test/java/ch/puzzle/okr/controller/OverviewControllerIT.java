package ch.puzzle.okr.controller;

import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.authorization.OverviewAuthorizationService;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.models.State.DRAFT;
import static ch.puzzle.okr.models.State.ONGOING;
import static org.mockito.ArgumentMatchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WithMockUser(value = "spring")
@ExtendWith(MockitoExtension.class)
@WebMvcTest(OverviewController.class)
class OverviewControllerIT {
    @Autowired
    private MockMvc mvc;
    @MockBean
    private OverviewAuthorizationService overviewAuthorizationService;
    // Dashboard and OverviewMapper are required for testing
    @SpyBean
    private OverviewMapper overviewMapper;

    public static final String PUZZLE = "Puzzle";
    public static final String DESCRIPTION = "This is a description";
    public static final String TEAM_KUCHEN = "Kuchen";
    public static final String QUARTER_LABEL = "GJ 22/23-Q2";
    public static final String CHF = "CHF";
    public static final String EUR = "EUR";
    public static final String FTE = "FTE";
    public static final String JSON_PATH_TEAM_NAME = "$[0].team.name";
    public static final String JSON_PATH_TEAM_ID = "$[0].team.id";
    public static final String JSON_PATH_ROOT = "$";

    static List<Overview> overviewPuzzle = List.of(
            Overview.Builder.builder().withOverviewId(OverviewId.of(1L, 1L, 20L, 20L)).withTeamName(PUZZLE)
                    .withObjectiveTitle("Objective 1").withObjectiveState(DRAFT).withQuarterId(1L)
                    .withQuarterLabel(QUARTER_LABEL).withKeyResultTitle(DESCRIPTION)
                    .withKeyResultType(KEY_RESULT_TYPE_METRIC).withUnit(CHF).withBaseline(5.0).withStretchGoal(20.0)
                    .withCheckInValue(15.0).withConfidence(5).withCheckInCreatedOn(LocalDateTime.now()).build(),
            Overview.Builder.builder().withOverviewId(OverviewId.of(1L, 2L, 21L, 41L)).withTeamName(PUZZLE)
                    .withObjectiveTitle("Objective 1").withObjectiveState(DRAFT).withQuarterId(1L)
                    .withQuarterLabel(QUARTER_LABEL).withKeyResultTitle(DESCRIPTION)
                    .withKeyResultType(KEY_RESULT_TYPE_METRIC).withUnit(EUR).withBaseline(5.0).withStretchGoal(20.0)
                    .withCheckInValue(15.0).withConfidence(5).withCheckInCreatedOn(LocalDateTime.now()).build());
    static List<Overview> overviewOKR = List.of(
            Overview.Builder.builder().withOverviewId(OverviewId.of(2L, 5L, 20L, 40L)).withTeamName("OKR")
                    .withObjectiveTitle("Objective 5").withObjectiveState(DRAFT).withQuarterId(1L)
                    .withQuarterLabel(QUARTER_LABEL).withKeyResultTitle(DESCRIPTION)
                    .withKeyResultType(KEY_RESULT_TYPE_METRIC).withUnit(CHF).withBaseline(5.0).withStretchGoal(20.0)
                    .withCheckInValue(15.0).withConfidence(5).withCheckInCreatedOn(LocalDateTime.now()).build(),
            Overview.Builder.builder().withOverviewId(OverviewId.of(2L, 7L, 21L, 41L)).withTeamName("OKR")
                    .withObjectiveTitle("Objective 7").withObjectiveState(ONGOING).withQuarterId(1L)
                    .withQuarterLabel(QUARTER_LABEL).withKeyResultTitle(DESCRIPTION)
                    .withKeyResultType(KEY_RESULT_TYPE_METRIC).withUnit(FTE).withBaseline(5.0).withStretchGoal(20.0)
                    .withCheckInValue(15.0).withConfidence(5).withCheckInCreatedOn(LocalDateTime.now()).build());

    static Overview overviewKuchen = Overview.Builder.builder().withOverviewId(OverviewId.of(3L, 8L, 20L, 40L))
            .withTeamName(TEAM_KUCHEN).withObjectiveTitle("Objective 8").withObjectiveState(ONGOING).withQuarterId(1L)
            .withQuarterLabel(QUARTER_LABEL).withKeyResultTitle(DESCRIPTION).withKeyResultType(KEY_RESULT_TYPE_METRIC)
            .withUnit(EUR).withBaseline(5.0).withStretchGoal(20.0).withCheckInValue(15.0).withConfidence(5)
            .withCheckInCreatedOn(LocalDateTime.now()).build();

    static Overview simpleOverview = Overview.Builder.builder().withOverviewId(OverviewId.of(4L, -1L, -1L, -1L))
            .withTeamName(TEAM_KUCHEN).build();

    @DisplayName("Should get all teams with their objectives")
    @Test
    void shouldGetAllTeamsWithObjective() throws Exception {
        List<Overview> overviews = new ArrayList<>();
        overviews.addAll(overviewPuzzle);
        overviews.addAll(overviewOKR);
        overviews.add(overviewKuchen);
        BDDMockito.given(overviewAuthorizationService.getFilteredOverview(anyLong(), anyList(), anyString()))
                .willReturn(overviews);
        BDDMockito.given(overviewAuthorizationService.hasWriteAllAccess()).willReturn(true);

        mvc.perform(get("/api/v2/overview?quarter=2&team=1,2,3,4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT, Matchers.hasSize(3)))
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

    @DisplayName("Should return empty list if no teams exist")
    @Test
    void shouldGetAllTeamsWithObjectiveIfNoTeamsExists() throws Exception {
        BDDMockito.given(overviewAuthorizationService.getFilteredOverview(anyLong(), anyList(), anyString()))
                .willReturn(Collections.emptyList());
        BDDMockito.given(overviewAuthorizationService.hasWriteAllAccess()).willReturn(true);

        mvc.perform(get("/api/v2/overview").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT, Matchers.hasSize(0)));
    }

    @DisplayName("Should return only objectives filtered by quarter and team")
    @Test
    void shouldReturnOnlyFilteredObjectivesByQuarterAndTeam() throws Exception {
        List<Overview> overviews = new ArrayList<>(overviewPuzzle);
        overviews.add(overviewKuchen);
        BDDMockito.given(overviewAuthorizationService.getFilteredOverview(anyLong(), anyList(), anyString()))
                .willReturn(overviews);

        mvc.perform(get("/api/v2/overview?quarter=2&team=1,3").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT, Matchers.hasSize(2)))
                .andExpect(jsonPath(JSON_PATH_TEAM_ID, Is.is(1)))
                .andExpect(jsonPath(JSON_PATH_TEAM_NAME, Is.is(PUZZLE)))
                .andExpect(jsonPath("$[0].objectives[0].id", Is.is(1)))
                .andExpect(jsonPath("$[0].objectives[1].id", Is.is(2))).andExpect(jsonPath("$[1].team.id", Is.is(3)))
                .andExpect(jsonPath("$[1].team.name", Is.is(TEAM_KUCHEN)))
                .andExpect(jsonPath("$[1].objectives[0].id", Is.is(8)));
    }

    @DisplayName("Should return team with empty objective list when no objectives exist in the filtered quarter")
    @Test
    void shouldReturnTeamWithEmptyObjectiveListWhenNoObjectiveInFilteredQuarter() throws Exception {
        BDDMockito.given(overviewAuthorizationService.getFilteredOverview(anyLong(), anyList(), anyString()))
                .willReturn(List.of(simpleOverview));

        mvc.perform(get("/api/v2/overview?quarter=2&team=4").contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath(JSON_PATH_ROOT, Matchers.hasSize(1)))
                .andExpect(jsonPath(JSON_PATH_TEAM_ID, Is.is(4)))
                .andExpect(jsonPath(JSON_PATH_TEAM_NAME, Is.is(TEAM_KUCHEN)))
                .andExpect(jsonPath("$[0].objectives.size()", Is.is(0)));
    }
}
