package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
import static ch.puzzle.okr.OverviewTestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OverviewBusinessServiceTest {
    private static final AuthorizationUser authorizationUser = defaultAuthorizationUser();
    @InjectMocks
    OverviewBusinessService overviewBusinessService;
    @Mock
    OverviewPersistenceService overviewPersistenceService;

    @Mock
    TeamBusinessService teamBusinessService;
    @Mock
    QuarterBusinessService quarterBusinessService;

    @Mock
    TeamPersistenceService teamPersistenceService;
    @Mock
    OverviewValidationService overviewValidationService;

    private static Overview createOverview() {
        return Overview.Builder.builder().withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).build())
                .withObjectiveTitle("Objective 1").build();
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnListOfOverviews() {
        when(overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(1L, List.of(4L), "",authorizationUser))
                .thenReturn(List.of(createOverview()));


        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, "Objective");

        assertEquals(1, overviews.size());
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterIdAndTeamIds(1L, List.of(4L),
                authorizationUser);
        verify(quarterBusinessService, times(0)).getCurrentQuarter();
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterAndTeamsAndObjectiveQuery(QUARTER_ID, teamIds,
                "Objective");
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnListOfOverviewsWhenQuarterIsNull() {
        when(overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(1L, List.of(4L), authorizationUser))
        when(overviewPersistenceService.getOverviewByQuarterAndTeamsAndObjectiveQuery(QUARTER_ID, teamIds, ""))
                .thenReturn(List.of(createOverview()));
        when(quarterBusinessService.getCurrentQuarter())
                .thenReturn(Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build());


        List<Overview> overviews = overviewBusinessService.getFilteredOverview(null, teamIds, "");

        assertEquals(1, overviews.size());
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterIdAndTeamIds(1L, List.of(4L),
                authorizationUser);
        verify(quarterBusinessService, times(1)).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterAndTeamsAndObjectiveQuery(QUARTER_ID, teamIds,
                "");
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnListOfOverviewsWhenTeamIdsAreNull() {
        when(overviewPersistenceService.getOverviewByQuarterAndTeamsAndObjectiveQuery(QUARTER_ID, List.of(), ""))
                .thenReturn(List.of(createOverview()));
        when(teamBusinessService.getAllTeams()).thenReturn(List.of());

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, null, "");

        assertEquals(1, overviews.size());
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterAndTeamsAndObjectiveQuery(QUARTER_ID,
                List.of(), "");
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, List.of());
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterAndTeamsAndObjectiveQuery(anyLong(), anyList(),
                anyString());
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnExceptionWhenQuarterIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(eq(QUARTER_ID), anyList());
        assertThrows(ResponseStatusException.class, () -> {
            overviewBusinessService.getFilteredOverview(QUARTER_ID, null, "");
        });

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterAndTeamsAndObjectiveQuery(anyLong(), anyList(),
                anyString());
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnExceptionWhenTeamIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(QUARTER_ID, teamIds);
        assertThrows(ResponseStatusException.class, () -> {
            overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, "");
        });

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateQuarter(QUARTER_ID);
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterAndTeamsAndObjectiveQuery(anyLong(), anyList(),
                anyString());
        verify(overviewValidationService, times(1)).validateOnGet(quarterId, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterIdAndTeamIds(anyLong(), anyList(), any());
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldThrowExceptionWhenTeamIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(quarterId, teamIds);
        assertThrows(ResponseStatusException.class,
                () -> overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds, authorizationUser));

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(quarterId, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterIdAndTeamIds(anyLong(), anyList(), any());
    }

    private List<Team> getAllTeams(List<Long> teamIds) {
        return teamIds.stream().map(id -> Team.Builder.builder().withId(id).build()).toList();
    }
}
