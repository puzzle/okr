package ch.puzzle.okr.mapper;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static ch.puzzle.okr.test.TestConstants.TEAM_PUZZLE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.dto.overview.OverviewDto;
import ch.puzzle.okr.dto.overview.OverviewKeyResultDto;
import ch.puzzle.okr.dto.overview.OverviewKeyResultMetricDto;
import ch.puzzle.okr.dto.overview.OverviewKeyResultOrdinalDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import ch.puzzle.okr.test.TestHelper;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OverviewMapperTest {

    @InjectMocks
    private OverviewMapper overviewMapper;

    @DisplayName("Should return an empty list when toDto() is called with no team found")
    @Test
    void shouldReturnEmptyListWhenToDtoIsCalledWithNoTeamFound() {
        List<Overview> overviews = List.of();
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertTrue(overviewDtos.isEmpty());
    }

    @DisplayName("Should return one element when toDto() is called with a team without objectives")
    @Test
    void shouldReturnOneElementWhenToDtoIsCalledWithTeamWithoutObjectives() {
        List<Overview> overviews = List
                .of(Overview.Builder
                        .builder()
                        .withOverviewId(OverviewId.Builder.builder().withTeamId(2L).build())
                        .withTeamName(TEAM_PUZZLE)
                        .build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(0, overviewDtos.get(0).objectives().size());
    }

    @DisplayName("Should return one element when toDto() is called with a single objective and no key-results")
    @Test
    void shouldReturnOneElementWhenToDtoIsCalledWithSingleObjectiveAndNoKeyResults() {
        List<Overview> overviews = List
                .of(Overview.Builder
                        .builder()
                        .withOverviewId(OverviewId.Builder.builder().withObjectiveId(1L).withTeamId(2L).build())
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(0, overviewDtos.get(0).objectives().get(0).keyResults().size());
    }

    @DisplayName("Should return one element when toDto() is called with a single objective and a key-result")
    @Test
    void shouldReturnOneElementWhenToDtoIsCalledWithSingleObjectiveAndKeyResult() {
        List<Overview> overviews = List
                .of(Overview.Builder
                        .builder()
                        .withOverviewId(OverviewId.Builder
                                .builder()
                                .withObjectiveId(1L)
                                .withTeamId(2L)
                                .withKeyResultId(3L)
                                .build())
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 1")
                        .withKeyResultType(KEY_RESULT_TYPE_METRIC)
                        .build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(0).keyResults().size());
    }

    @DisplayName("Should return one element when toDto() is called with an objective having multiple key results and check-ins")
    @Test
    void shouldReturnOneElementWhenToDtoIsCalledWithObjectiveHavingMultipleKeyResultsAndCheckIns() {
        List<Overview> overviews = List
                .of(Overview.Builder
                        .builder()
                        .withOverviewId(OverviewId.Builder
                                .builder()
                                .withObjectiveId(1L)
                                .withTeamId(2L)
                                .withKeyResultId(3L)
                                .withCheckInId(4L)
                                .build())
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 1")
                        .withKeyResultType(KEY_RESULT_TYPE_METRIC)
                        .withCheckInValue(27.5)
                        .withConfidence(5)
                        .build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(0).keyResults().size());
    }

    @DisplayName("Should return one element when toDto() is called with an objective having two key results and a check-in")
    @Test
    void shouldReturnOneElementWhenToDtoIsCalledWithObjectiveHavingTwoKeyResultsAndCheckIn() {
        List<Overview> overviews = List
                .of(Overview.Builder
                        .builder()
                        .withOverviewId(OverviewId.Builder
                                .builder()
                                .withObjectiveId(1L)
                                .withTeamId(2L)
                                .withKeyResultId(3L)
                                .withCheckInId(4L)
                                .build())
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 1")
                        .withKeyResultType(KEY_RESULT_TYPE_ORDINAL)
                        .withCheckInZone("COMMIT")
                        .build(),
                    Overview.Builder
                            .builder()
                            .withOverviewId(OverviewId.Builder
                                    .builder()
                                    .withObjectiveId(1L)
                                    .withTeamId(2L)
                                    .withKeyResultId(5L)
                                    .build())
                            .withTeamName(TEAM_PUZZLE)
                            .withObjectiveTitle("Objective 1")
                            .withKeyResultTitle("Key Result 5")
                            .withKeyResultType(KEY_RESULT_TYPE_METRIC)
                            .build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(1, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(2, overviewDtos.get(0).objectives().get(0).keyResults().size());
    }

    @DisplayName("Should return one element when toDto() is called with multiple objectives, key results, and check-ins")
    @Test
    void shouldReturnOneElementWhenToDtoIsCalledWithMultipleObjectivesKeyResultsAndCheckIns() {
        List<Overview> overviews = List
                .of(Overview.Builder
                        .builder()
                        .withOverviewId(OverviewId.Builder
                                .builder()
                                .withObjectiveId(1L)
                                .withTeamId(2L)
                                .withKeyResultId(3L)
                                .withCheckInId(4L)
                                .build())
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 1")
                        .withKeyResultType(KEY_RESULT_TYPE_METRIC)
                        .withBaseline(20.0)
                        .withStretchGoal(37.0)
                        .withUnit("TCHF")
                        .withCheckInValue(27.5)
                        .build(),
                    Overview.Builder
                            .builder()
                            .withOverviewId(OverviewId.Builder
                                    .builder()
                                    .withObjectiveId(5L)
                                    .withTeamId(2L)
                                    .withKeyResultId(6L)
                                    .withCheckInId(7L)
                                    .build())
                            .withTeamName(TEAM_PUZZLE)
                            .withObjectiveTitle("Objective 5")
                            .withKeyResultTitle("Key Result 6")
                            .withKeyResultType(KEY_RESULT_TYPE_ORDINAL)
                            .withCommitZone("commit")
                            .withTargetZone("target")
                            .withStretchZone("stretch")
                            .withCheckInZone("checkIn")
                            .build());
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
        assertEquals("checkIn", ((OverviewKeyResultOrdinalDto) keyResultDto).lastCheckIn().zone());
    }

    @DisplayName("Should return one element when toDto() is called with multiple teams having objectives and key-results")
    @Test
    void shouldReturnOneElementWhenToDtoIsCalledWithMultipleTeamsHavingObjectivesAndKeyResults() {
        List<Overview> overviews = List
                .of(Overview.Builder
                        .builder()
                        .withOverviewId(OverviewId.Builder
                                .builder()
                                .withObjectiveId(1L)
                                .withTeamId(2L)
                                .withKeyResultId(3L)
                                .withCheckInId(4L)
                                .build())
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 1")
                        .withKeyResultType(KEY_RESULT_TYPE_ORDINAL)
                        .withCheckInZone("TARGET")
                        .build(),
                    Overview.Builder
                            .builder()
                            .withOverviewId(OverviewId.Builder
                                    .builder()
                                    .withObjectiveId(5L)
                                    .withTeamId(4L)
                                    .withKeyResultId(6L)
                                    .build())
                            .withTeamName("/BBT")
                            .withObjectiveTitle("Objective 5")
                            .withKeyResultTitle("Key Result 6")
                            .withKeyResultType(KEY_RESULT_TYPE_METRIC)
                            .build(),
                    Overview.Builder
                            .builder()
                            .withOverviewId(OverviewId.Builder
                                    .builder()
                                    .withObjectiveId(5L)
                                    .withTeamId(4L)
                                    .withKeyResultId(8L)
                                    .build())
                            .withTeamName("/BBT")
                            .withObjectiveTitle("Objective 5")
                            .withKeyResultTitle("Key Result 8")
                            .withKeyResultType(KEY_RESULT_TYPE_ORDINAL)
                            .build());
        List<OverviewDto> overviewDtos = overviewMapper.toDto(overviews);

        assertEquals(2, overviewDtos.size());
        assertEquals(1, overviewDtos.get(0).objectives().size());
        assertEquals(1, overviewDtos.get(0).objectives().get(0).keyResults().size());
        assertEquals(1, overviewDtos.get(1).objectives().size());
        assertEquals(2, overviewDtos.get(1).objectives().get(0).keyResults().size());
    }

    @DisplayName("Should throw an exception when toDto() is called with an unsupported key result type")
    @Test
    void shouldThrowExceptionWhenToDtoIsCalledWithUnsupportedKeyResultType() {
        List<Overview> overviews = List
                .of(Overview.Builder
                        .builder()
                        .withOverviewId(OverviewId.Builder
                                .builder()
                                .withObjectiveId(1L)
                                .withTeamId(2L)
                                .withKeyResultId(3L)
                                .withCheckInId(4L)
                                .build())
                        .withTeamName(TEAM_PUZZLE)
                        .withObjectiveTitle("Objective 1")
                        .withKeyResultTitle("Key Result 1")
                        .withKeyResultType("unknown")
                        .withCheckInZone("TARGET")
                        .build());

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                                                            () -> overviewMapper.toDto(overviews));

        assertEquals(BAD_REQUEST, exception.getStatusCode());

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("KEY_RESULT_CONVERSION", List.of("unknown")));

        assertEquals(BAD_REQUEST, exception.getStatusCode());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
    }
}
