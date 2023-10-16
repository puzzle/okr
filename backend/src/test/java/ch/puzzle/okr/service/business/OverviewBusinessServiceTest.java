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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
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
        when(overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds))
                .thenReturn(List.of(createOverview()));

        List<Overview> overviews = overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds);

        assertEquals(1, overviews.size());
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewPersistenceService, never()).getOverviewByQuarterId(quarterId);
        verify(overviewValidationService, times(1)).validateOnGet(quarterId, teamIds);
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterIdAndTeamIds(quarterId, teamIds);
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnListOfOverviewsWhenQuarterIsNull() {
        when(overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds))
                .thenReturn(List.of(createOverview()));
        when(quarterBusinessService.getCurrentQuarter())
                .thenReturn(Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build());

        List<Overview> overviews = overviewBusinessService.getOverviewByQuarterIdAndTeamIds(null, teamIds);

        assertEquals(1, overviews.size());
        verify(quarterBusinessService, times(1)).getCurrentQuarter();
        verify(overviewPersistenceService, never()).getOverviewByQuarterId(quarterId);
        verify(overviewValidationService, times(1)).validateOnGet(quarterId, teamIds);
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterIdAndTeamIds(quarterId, teamIds);
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnListOfOverviewsWhenTeamIdsAreNull() {
        when(overviewPersistenceService.getOverviewByQuarterId(quarterId)).thenReturn(List.of(createOverview()));

        List<Overview> overviews = overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterId, null);

        assertEquals(1, overviews.size());
        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateQuarter(quarterId);
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterId(quarterId);
        verify(overviewValidationService, never()).validateOnGet(quarterId, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterIdAndTeamIds(anyLong(), anyList());
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnExceptionWhenQuarterIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService).validateQuarter(quarterId);
        assertThrows(ResponseStatusException.class, () -> {
            overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterId, null);
        });

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, times(1)).validateQuarter(quarterId);
        verify(overviewPersistenceService, never()).getOverviewByQuarterId(quarterId);
        verify(overviewValidationService, never()).validateOnGet(quarterId, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterIdAndTeamIds(anyLong(), anyList());
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnExceptionWhenTeamIdIsNonExistent() {
        doThrow(new ResponseStatusException(HttpStatus.NOT_FOUND)).when(overviewValidationService).validateOnGet(quarterId, teamIds);
        assertThrows(ResponseStatusException.class, () -> {
            overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterId, teamIds);
        });

        verify(quarterBusinessService, never()).getCurrentQuarter();
        verify(overviewValidationService, never()).validateQuarter(quarterId);
        verify(overviewValidationService, times(1)).validateOnGet(quarterId, teamIds);
        verify(overviewPersistenceService, never()).getOverviewByQuarterIdAndTeamIds(anyLong(), anyList());
    }
}
