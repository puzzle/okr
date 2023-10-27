package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import ch.puzzle.okr.service.validation.OverviewValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.OverviewTestHelper.QUARTER_ID;
import static ch.puzzle.okr.OverviewTestHelper.teamIds;
import static ch.puzzle.okr.TestHelper.defaultAuthorizationUser;
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
    QuarterBusinessService quarterBusinessService;

    @Mock
    OverviewValidationService overviewValidationService;

    private static Overview createOverview() {
        return Overview.Builder.builder().withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).build())
                .withObjectiveTitle("Objective 1").build();
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnListOfOverviews() {
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, "Objective", authorizationUser))
                .thenReturn(List.of(createOverview()));

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, "Objective",
                authorizationUser);

        assertEquals(1, overviews.size());
        verify(overviewPersistenceService, times(1)).getFilteredOverview(QUARTER_ID, teamIds, "Objective",
                authorizationUser);
        verify(quarterBusinessService, times(0)).getCurrentQuarter();
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, times(1)).getFilteredOverview(QUARTER_ID, teamIds, "Objective",
                authorizationUser);
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnListOfOverviewsWhenQuarterIsNull() {
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser))
                .thenReturn(List.of(createOverview()));
        when(quarterBusinessService.getCurrentQuarter())
                .thenReturn(Quarter.Builder.builder().withId(QUARTER_ID).withLabel("GJ 22/23-Q2").build());

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(null, teamIds, "", authorizationUser);

        assertEquals(1, overviews.size());

        verify(quarterBusinessService, times(1)).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, times(1)).getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser);
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnListOfOverviewsWhenTeamIdsAreNull() {
        when(overviewPersistenceService.getFilteredOverview(QUARTER_ID, List.of(5L), "", authorizationUser))
                .thenReturn(List.of(createOverview()));

        List<Overview> overviews = overviewBusinessService.getFilteredOverview(QUARTER_ID, null, "", authorizationUser);

        assertEquals(1, overviews.size());
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewPersistenceService, times(1)).getFilteredOverview(QUARTER_ID, List.of(5L), "",
                authorizationUser);
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, List.of(5L));
        verify(overviewPersistenceService, times(1)).getFilteredOverview(anyLong(), anyList(), anyString(),
                eq(authorizationUser));
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnExceptionWhenQuarterIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(eq(QUARTER_ID), anyList());
        assertThrows(ResponseStatusException.class, () -> {
            overviewBusinessService.getFilteredOverview(QUARTER_ID, null, "", authorizationUser);
        });

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getFilteredOverview(anyLong(), anyList(), anyString(),
                eq(authorizationUser));
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldReturnExceptionWhenTeamIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(QUARTER_ID, teamIds);

        assertThrows(ResponseStatusException.class, () -> {
            overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser);
        });

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateQuarter(QUARTER_ID);
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getFilteredOverview(anyLong(), anyList(), any(),
                eq(authorizationUser));
    }

    @Test
    void getOverviewByQuarterIdAndTeamIdsShouldThrowExceptionWhenTeamIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(QUARTER_ID, teamIds);
        assertThrows(ResponseStatusException.class,
                () -> overviewBusinessService.getFilteredOverview(QUARTER_ID, teamIds, "", authorizationUser));

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getFilteredOverview(anyLong(), anyList(), anyString(),
                eq(authorizationUser));
    }
}
