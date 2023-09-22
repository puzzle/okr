package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.overview.OverviewDto;
import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.models.OverviewId;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OverviewMapperTest {
    private final OverviewMapper overviewMapper = new OverviewMapper();

    @Test
    void toDto_ShouldReturnEmptyList_WhenNoTeamFound() {
        List<Overview> overviews = List.of();
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertTrue(overviewDtos.isEmpty());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenObjectiveFound() {
        List<Overview> overviews = List.of(Overview.Builder.builder()
                .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L).build())
                .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(0, overviewDtos.get(0).objectives().get(0).keyResults().size());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenObjectiveWithKeyResultFound() {
        List<Overview> overviews = List.of(Overview.Builder.builder()
                .withOverviewId(
                        OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L).withKeyResultId(3L).build())
                .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").withKeyResultTitle("Key Result 1")
                .build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(0).keyResults().size());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenObjectiveWithKeyResultAndCheckInsFound() {
        List<Overview> overviews = List.of(Overview.Builder.builder()
                .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L).withKeyResultId(3L)
                        .withCheckInId(4L).build())
                .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").withKeyResultTitle("Key Result 1")
                .withCheckInValue(27.5).withConfidence(5).build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(0).keyResults().size());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenObjectiveWithTwoKeyResultAndCheckInFound() {
        List<Overview> overviews = List.of(
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L)
                                .withKeyResultId(3L).withCheckInId(4L).build())
                        .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").withKeyResultTitle("Key Result 1")
                        .withCheckInZone("COMMIT").build(),
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L)
                                .withKeyResultId(5L).build())
                        .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").withKeyResultTitle("Key Result 5")
                        .build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(2, overviewDtos.get(0).objectives().get(0).keyResults().size());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenTwoObjectivesWithKeyResultAndCheckInFound() {
        List<Overview> overviews = List.of(
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L)
                                .withKeyResultId(3L).withCheckInId(4L).build())
                        .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").withKeyResultTitle("Key Result 1")
                        .withCheckInValue(27.5).build(),
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(5L).withTeamId(2L)
                                .withKeyResultId(6L).withCheckInId(7L).build())
                        .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 5").withKeyResultTitle("Key Result 6")
                        .withCheckInValue(33.5).build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(2, overviewDtos.get(0).objectives().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(0).keyResults().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(1).keyResults().size());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenTwoTeamsWithObjectivesAndKeyResultsFound() {
        List<Overview> overviews = List.of(
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L)
                                .withKeyResultId(3L).withCheckInId(4L).build())
                        .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").withKeyResultTitle("Key Result 1")
                        .withCheckInZone("TARGET").build(),
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(5L).withTeamId(4L)
                                .withKeyResultId(6L).build())
                        .withTeamName("/BBT").withObjectiveTitle("Objective 5").withKeyResultTitle("Key Result 6")
                        .build(),
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(5L).withTeamId(4L)
                                .withKeyResultId(8L).build())
                        .withTeamName("/BBT").withObjectiveTitle("Objective 5").withKeyResultTitle("Key Result 8")
                        .build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(2, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(0).keyResults().size());
        assertEquals(1, overviewDtos.get(1).objectives().size());
        assertEquals(2, overviewDtos.get(1).objectives().get(0).keyResults().size());
    }
}
