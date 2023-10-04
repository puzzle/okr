package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.overview.*;
import ch.puzzle.okr.models.overview.Overview;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class OverviewMapper {

    public List<OverviewDto> toDto(List<Overview> overviews) {
        List<OverviewDto> overviewDtos = new ArrayList<>();
        overviews.forEach(overview -> processTeams(overviewDtos, overview));
        return overviewDtos;
    }

    private Optional<OverviewDto> getMatchingOverviewDto(Long teamId, List<OverviewDto> result) {
        return result.stream().filter(overviewDto -> Objects.equals(teamId, overviewDto.team().id())).findFirst();
    }

    private Optional<OverviewObjectiveDto> getMatchingObjectiveDto(Long objectiveId,
            List<OverviewObjectiveDto> objectives) {
        return objectives.stream().filter(objectiveDto -> Objects.equals(objectiveId, objectiveDto.id())).findFirst();
    }

    private void processTeams(List<OverviewDto> overviewDtos, Overview overview) {
        Optional<OverviewDto> overviewDto = getMatchingOverviewDto(overview.getOverviewId().getTeamId(), overviewDtos);
        if (overviewDto.isPresent()) {
            processObjectives(overviewDto.get(), overview);
        } else {
            overviewDtos.add(createOverviewDto(overview));
        }
    }

    private void processObjectives(OverviewDto overviewDto, Overview overview) {
        Optional<OverviewObjectiveDto> overviewObjectiveDto = getMatchingObjectiveDto(
                overview.getOverviewId().getObjectiveId(), overviewDto.objectives());
        if (overviewObjectiveDto.isPresent()) {
            processKeyResults(overviewObjectiveDto.get(), overview);
        } else {
            overviewDto.objectives().add(createObjectiveDto(overview));
        }
    }

    private void processKeyResults(OverviewObjectiveDto overviewObjectiveDto, Overview overview) {
        overviewObjectiveDto.keyResults().add(createKeyResultDto(overview));
    }

    private OverviewDto createOverviewDto(Overview overview) {
        List<OverviewObjectiveDto> objectives = new ArrayList<>();
        if (isValidId(overview.getOverviewId().getObjectiveId())) {
            objectives.add(createObjectiveDto(overview));
        }
        return new OverviewDto(new OverviewTeamDto(overview.getOverviewId().getTeamId(), overview.getTeamName()),
                objectives);
    }

    private OverviewObjectiveDto createObjectiveDto(Overview overview) {
        List<OverviewKeyResultDto> keyResults = new ArrayList<>();
        if (isValidId(overview.getOverviewId().getKeyResultId())) {
            keyResults.add(createKeyResultDto(overview));
        }
        return new OverviewObjectiveDto(overview.getOverviewId().getObjectiveId(), overview.getObjectiveTitle(),
                overview.getObjectiveState(),
                new OverviewQuarterDto(overview.getQuarterId(), overview.getQuarterLabel()), keyResults);
    }

    private OverviewKeyResultDto createKeyResultDto(Overview overview) {
        if (Objects.equals(overview.getKeyResultType(), KEY_RESULT_TYPE_METRIC)) {
            return createKeyResultMetricDto(overview);
        } else if (Objects.equals(overview.getKeyResultType(), KEY_RESULT_TYPE_ORDINAL)) {
            return createKeyResultOrdinalDto(overview);
        } else {
            throw new ResponseStatusException(BAD_REQUEST,
                    String.format("The key result type %s can not be converted to a metric or ordinal DTO",
                            overview.getKeyResultType()));
        }
    }

    private OverviewKeyResultMetricDto createKeyResultMetricDto(Overview overview) {
        OverviewLastCheckInMetricDto lastCheckIn = null;
        if (isValidId(overview.getOverviewId().getCheckInId())) {
            lastCheckIn = new OverviewLastCheckInMetricDto(overview.getOverviewId().getCheckInId(),
                    overview.getCheckInValue(), overview.getConfidence(), overview.getCreatedOn());
        }
        return new OverviewKeyResultMetricDto(overview.getOverviewId().getKeyResultId(), overview.getKeyResultTitle(),
                overview.getKeyResultType(), overview.getUnit(), overview.getBaseline(), overview.getStretchGoal(),
                lastCheckIn);
    }

    private OverviewKeyResultOrdinalDto createKeyResultOrdinalDto(Overview overview) {
        OverviewLastCheckInOrdinalDto lastCheckIn = null;
        if (isValidId(overview.getOverviewId().getCheckInId())) {
            lastCheckIn = new OverviewLastCheckInOrdinalDto(overview.getOverviewId().getCheckInId(),
                    overview.getCheckInZone(), overview.getConfidence(), overview.getCreatedOn());
        }
        return new OverviewKeyResultOrdinalDto(overview.getOverviewId().getKeyResultId(), overview.getKeyResultTitle(),
                overview.getKeyResultType(), overview.getCommitZone(), overview.getTargetZone(),
                overview.getStretchZone(), lastCheckIn);
    }

    private boolean isValidId(Long id) {
        return id != null && id > -1;
    }
}
