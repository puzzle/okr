package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.overview.OverviewDto;
import ch.puzzle.okr.dto.overview.OverviewObjectiveDto;
import ch.puzzle.okr.dto.overview.OverviewQuarterDto;
import ch.puzzle.okr.dto.overview.OverviewTeamDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.models.OverviewId;
import ch.puzzle.okr.models.State;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OverviewServiceTest {

    @InjectMocks
    OverviewService objectiveService;
    @Mock
    OverviewBusinessService overviewBusinessService;
    @Mock
    OverviewMapper overviewMapper;

    private static Overview createOverview() {
        return Overview.Builder.builder().withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).build())
                .withObjectiveTitle("Objective 1").build();
    }

    private static OverviewDto createOverviewDto() {
        return new OverviewDto(new OverviewTeamDto(4L, "Findus"), List.of(new OverviewObjectiveDto(1L, "Objective 1",
                State.ONGOING, new OverviewQuarterDto(2L, "GJ 22/23-Q3"), List.of())));
    }

    @Test
    void getOverviewByQuarterIdAndTeamIds_ShouldReturnListOfOverviews() {
        List<Overview> overviews = List.of(createOverview());
        Mockito.when(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(2L, List.of(4L))).thenReturn(overviews);
        Mockito.when(overviewMapper.toDto(overviews)).thenReturn(List.of(createOverviewDto()));

        List<OverviewDto> overviewDtos = objectiveService.getOverviewByQuarterIdAndTeamIds(2L, List.of(4L));

        assertEquals(1, overviewDtos.size());
        assertEquals(4L, overviewDtos.get(0).team().id());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(1L, overviewDtos.get(0).objectives().get(0).id());
        assertEquals(2L, overviewDtos.get(0).objectives().get(0).quarter().id());
    }
}
