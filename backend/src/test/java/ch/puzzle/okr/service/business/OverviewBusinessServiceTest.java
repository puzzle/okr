package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
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

import static ch.puzzle.okr.OverviewTestHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OverviewBusinessServiceTest {

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
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnListOfOverviews() {
        when(overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(QUARTER_ID, teamIds))
                .thenReturn(List.of(createOverview()));

        List<Overview> overviews = overviewBusinessService.getOverviewByQuarterIdAndTeamIds(QUARTER_ID, teamIds);

        assertEquals(1, overviews.size());
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewPersistenceService, never()).getOverviewByQuarterId(QUARTER_ID);
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterIdAndTeamIds(QUARTER_ID, teamIds);
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnListOfOverviewsWhenQuarterIsNull() {
        when(overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(QUARTER_ID, teamIds))
                .thenReturn(List.of(createOverview()));
        when(quarterBusinessService.getCurrentQuarter())
                .thenReturn(Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build());

        List<Overview> overviews = overviewBusinessService.getOverviewByQuarterIdAndTeamIds(null, teamIds);

        assertEquals(1, overviews.size());
        verify(quarterBusinessService, times(1)).getCurrentQuarter();
        verify(overviewPersistenceService, never()).getOverviewByQuarterId(QUARTER_ID);
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterIdAndTeamIds(QUARTER_ID, teamIds);
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnListOfOverviewsWhenTeamIdsAreNull() {
        when(overviewPersistenceService.getOverviewByQuarterId(QUARTER_ID)).thenReturn(List.of(createOverview()));

        List<Overview> overviews = overviewBusinessService.getOverviewByQuarterIdAndTeamIds(QUARTER_ID, null);

        assertEquals(1, overviews.size());
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateQuarter(QUARTER_ID);
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterId(QUARTER_ID);
        verify(overviewValidationService, never()).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterIdAndTeamIds(anyLong(), anyList());
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnExceptionWhenQuarterIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateQuarter(QUARTER_ID);
        assertThrows(ResponseStatusException.class, () -> {
            overviewBusinessService.getOverviewByQuarterIdAndTeamIds(QUARTER_ID, null);
        });

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateQuarter(QUARTER_ID);
        verify(overviewPersistenceService, never()).getOverviewByQuarterId(QUARTER_ID);
        verify(overviewValidationService, never()).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterIdAndTeamIds(anyLong(), anyList());
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnExceptionWhenTeamIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService)
                .validateOnGet(QUARTER_ID, teamIds);
        assertThrows(ResponseStatusException.class, () -> {
            overviewBusinessService.getOverviewByQuarterIdAndTeamIds(QUARTER_ID, teamIds);
        });

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateQuarter(QUARTER_ID);
        verify(overviewValidationService, times(1)).validateOnGet(QUARTER_ID, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterIdAndTeamIds(anyLong(), anyList());
    }
}
