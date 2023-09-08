package ch.puzzle.okr.service;

import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.models.OverviewId;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;

import java.util.List;

import static org.mockito.Mockito.*;

@SpringIntegrationTest
class OverviewServiceIT {
    @InjectMocks
    @Spy
    OverviewService overviewService;
    @Mock
    OverviewBusinessService overviewBusinessService;
    @Mock
    OverviewMapper overviewMapper;

    @Test
    void shouldCallCorrectMethodsWhenGetOverviewByQuarterAndTeam() {
        Overview overview = Overview.Builder.builder()
                .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).build())
                .withObjectiveTitle("Objective 1").build();

        when(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(1L, List.of(4L))).thenReturn(List.of(overview));

        overviewService.getOverviewByQuarterIdAndTeamIds(1L, List.of(4L));

        verify(overviewMapper, times(1)).toDto(List.of(overview));
        verify(overviewBusinessService, times(1)).getOverviewByQuarterIdAndTeamIds(1L, List.of(4L));
    }
}
