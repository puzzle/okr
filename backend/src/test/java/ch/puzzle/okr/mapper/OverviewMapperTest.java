package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.overview.OverviewDto;
import ch.puzzle.okr.dto.overview.OverviewKeyResultDto;
import ch.puzzle.okr.dto.overview.OverviewKeyResultMetricDto;
import ch.puzzle.okr.dto.overview.OverviewKeyResultOrdinalDto;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class OverviewMapperTest {
    private final OverviewMapper overviewMapper = new OverviewMapper();

    @Test
    void toDto_ShouldReturnEmptyList_WhenNoTeamFound() {
        List<Overview> overviews = List.of();
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertTrue(overviewDtos.isEmpty());
    }

    @Test
    void toDto_ShouldReturnEmptyList_WhenTeamFound() {
        List<Overview> overviews = List
                .of(Overview.Builder.builder().withOverviewId(OverviewId.Builder.builder().withTeamId(2L).build())
                        .withTeamName("Puzzle ITC").build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(0, overviewDtos.get(0).objectives().size());
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
                .withKeyResultType(KEY_RESULT_TYPE_METRIC).build());
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
                .withKeyResultType(KEY_RESULT_TYPE_METRIC).withCheckInValue(27.5).withConfidence(5).build());
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
                        .withKeyResultType(KEY_RESULT_TYPE_ORDINAL).withCheckInZone("COMMIT").build(),
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L)
                                .withKeyResultId(5L).build())
                        .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").withKeyResultTitle("Key Result 5")
                        .withKeyResultType(KEY_RESULT_TYPE_METRIC).build());
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
                        .withKeyResultType(KEY_RESULT_TYPE_METRIC).withBaseline(20.0).withStretchGoal(37.0)
                        .withUnit("TCHF").withCheckInValue(27.5).build(),
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(5L).withTeamId(2L)
                                .withKeyResultId(6L).withCheckInId(7L).build())
                        .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 5").withKeyResultTitle("Key Result 6")
                        .withKeyResultType(KEY_RESULT_TYPE_ORDINAL).withCommitZone("commit").withTargetZone("target")
                        .withStretchZone("stretch").withCheckInZone("checkIn").build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(2, overviewDtos.get(0).objectives().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(0).keyResults().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(1).keyResults().size());

        OverviewKeyResultDto keyResultDto = overviewDtos.get(0).objectives().get(0).keyResults().get(0);
        assertTrue(keyResultDto instanceof OverviewKeyResultMetricDto);
        assertEquals(KEY_RESULT_TYPE_METRIC, keyResultDto.keyResultType());
        assertEquals(20.0, ((OverviewKeyResultMetricDto) keyResultDto).baseline(), 0.1);
        assertEquals(37.0, ((OverviewKeyResultMetricDto) keyResultDto).stretchGoal(), 0.1);
        assertEquals(27.5, ((OverviewKeyResultMetricDto) keyResultDto).lastCheckIn().value(), 0.1);

        keyResultDto = overviewDtos.get(0).objectives().get(1).keyResults().get(0);
        assertTrue(keyResultDto instanceof OverviewKeyResultOrdinalDto);
        assertEquals(KEY_RESULT_TYPE_ORDINAL, keyResultDto.keyResultType());
        assertEquals("commit", ((OverviewKeyResultOrdinalDto) keyResultDto).commitZone());
        assertEquals("target", ((OverviewKeyResultOrdinalDto) keyResultDto).targetZone());
        assertEquals("stretch", ((OverviewKeyResultOrdinalDto) keyResultDto).stretchZone());
        assertEquals("checkIn", ((OverviewKeyResultOrdinalDto) keyResultDto).lastCheckIn().value());
    }

    @Test
    void toDto_ShouldReturnOneElement_WhenTwoTeamsWithObjectivesAndKeyResultsFound() {
        List<Overview> overviews = List.of(
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L)
                                .withKeyResultId(3L).withCheckInId(4L).build())
                        .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").withKeyResultTitle("Key Result 1")
                        .withKeyResultType(KEY_RESULT_TYPE_ORDINAL).withCheckInZone("TARGET").build(),
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(5L).withTeamId(4L)
                                .withKeyResultId(6L).build())
                        .withTeamName("/BBT").withObjectiveTitle("Objective 5").withKeyResultTitle("Key Result 6")
                        .withKeyResultType(KEY_RESULT_TYPE_METRIC).build(),
                Overview.Builder.builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(5L).withTeamId(4L)
                                .withKeyResultId(8L).build())
                        .withTeamName("/BBT").withObjectiveTitle("Objective 5").withKeyResultTitle("Key Result 8")
                        .withKeyResultType(KEY_RESULT_TYPE_ORDINAL).build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(2, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(0).keyResults().size());
        assertEquals(1, overviewDtos.get(1).objectives().size());
        assertEquals(2, overviewDtos.get(1).objectives().get(0).keyResults().size());
    }

    @Test
    void toDto_ShouldThrowException_WhenKeyResultTypeNotSupported() {
        List<Overview> overviews = List.of(Overview.Builder.builder()
                .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L).withKeyResultId(3L)
                        .withCheckInId(4L).build())
                .withTeamName("Puzzle ITC").withObjectiveTitle("Objective 1").withKeyResultTitle("Key Result 1")
                .withKeyResultType("unknown").withCheckInZone("TARGET").build());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> overviewMapper.toDto(overviews));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertEquals("The key result type unknown can not be converted to a metric or ordinal DTO",
                exception.getReason());
    }
}
