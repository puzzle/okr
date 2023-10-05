package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.service.persistence.OverviewPersistenceService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OverviewBusinessServiceTest {

    @InjectMocks
    OverviewBusinessService overviewBusinessService;
    @Mock
    OverviewPersistenceService overviewPersistenceService;
    @Mock
    QuarterBusinessService quarterBusinessService;

    private static Overview createOverview() {
        return Overview.Builder.builder().withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).build())
                .withObjectiveTitle("Objective 1").build();
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnListOfOverviews() {
        when(overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(1L, List.of(4L)))
                .thenReturn(List.of(createOverview()));

        List<Overview> overviews = overviewBusinessService.getOverviewByQuarterIdAndTeamIds(1L, List.of(4L));

        assertEquals(1, overviews.size());
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterIdAndTeamIds(1L, List.of(4L));
        verify(quarterBusinessService, times(0)).getCurrentQuarter();
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnListOfOverviewsWhenQuarterIsNull() {
        when(overviewPersistenceService.getOverviewByQuarterIdAndTeamIds(1L, List.of(4L)))
                .thenReturn(List.of(createOverview()));
        when(quarterBusinessService.getCurrentQuarter())
                .thenReturn(Quarter.Builder.builder().withId(1L).withLabel("GJ 22/23-Q2").build());

        List<Overview> overviews = overviewBusinessService.getOverviewByQuarterIdAndTeamIds(null, List.of(4L));

        assertEquals(1, overviews.size());
        verify(overviewPersistenceService, times(1)).getOverviewByQuarterIdAndTeamIds(1L, List.of(4L));
        verify(quarterBusinessService, times(1)).getCurrentQuarter();
    }
}
