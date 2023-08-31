package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.dto.overview.*;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.models.Team;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class OverviewMapper {

    private final ObjectiveMapper objectiveMapper;
    private final TeamMapper teamMapper;

    public OverviewMapper(ObjectiveMapper objectiveMapper, TeamMapper teamMapper) {
        this.objectiveMapper = objectiveMapper;
        this.teamMapper = teamMapper;
    }

    @Deprecated
    public OverviewDto toDto(Team team, List<Objective> objectives) {
        return new OverviewDto(teamMapper.toDto(team), objectives.stream().map(objectiveMapper::toDto).toList());
    }

    public List<ch.puzzle.okr.dto.overview.OverviewDto> toDto(List<Overview> overviews) {
        List<ch.puzzle.okr.dto.overview.OverviewDto> overviewDtos = new ArrayList<>();
        overviews.forEach(overview -> {
            processTeams(overviewDtos, overview);
        });
        return overviewDtos;
    }

    private Optional<ch.puzzle.okr.dto.overview.OverviewDto> getMatchingOverviewDto(Long teamId,
            List<ch.puzzle.okr.dto.overview.OverviewDto> result) {
        return result.stream().filter(overviewDto -> Objects.equals(teamId, overviewDto.team().id())).findFirst();
    }

    private Optional<OverviewObjectiveDto> getMatchingObjectiveDto(Long objectiveId,
            List<OverviewObjectiveDto> objectives) {
        return objectives.stream().filter(objectiveDto -> Objects.equals(objectiveId, objectiveDto.id())).findFirst();
    }

    private void processTeams(List<ch.puzzle.okr.dto.overview.OverviewDto> overviewDtos, Overview overview) {
        Optional<ch.puzzle.okr.dto.overview.OverviewDto> overviewDto = getMatchingOverviewDto(
                overview.getOverviewId().getTeamId(), overviewDtos);
        if (overviewDto.isPresent()) {
            processObjectives(overviewDto.get(), overview);
        } else {
            overviewDtos.add(createOverviewDto(overview));
        }
    }

    private void processObjectives(ch.puzzle.okr.dto.overview.OverviewDto overviewDto, Overview overview) {
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

    private ch.puzzle.okr.dto.overview.OverviewDto createOverviewDto(Overview overview) {
        List<OverviewObjectiveDto> objectives = new ArrayList<>();
        objectives.add(createObjectiveDto(overview));
        return new ch.puzzle.okr.dto.overview.OverviewDto(
                new OverviewTeamDto(overview.getOverviewId().getTeamId(), overview.getTeamName()), objectives);
    }

    private OverviewObjectiveDto createObjectiveDto(Overview overview) {
        List<OverviewKeyResultDto> keyResults = new ArrayList<>();
        if (overview.getOverviewId().getKeyResultId() != null && overview.getOverviewId().getKeyResultId() > -1) {
            keyResults.add(createKeyResultDto(overview));
        }
        return new OverviewObjectiveDto(overview.getOverviewId().getObjectiveId(), overview.getObjectiveTitle(),
                overview.getObjectiveState(),
                new OverviewQuarterDto(overview.getQuarterId(), overview.getQuarterLabel()), keyResults);
    }

    private OverviewKeyResultDto createKeyResultDto(Overview overview) {
        // TODO enhance for OverviewKeyResultOrdinalDto and OverviewLastCheckInOrdinalDto
        OverviewLastCheckInMetricDto lastCheckIn = null;
        if (overview.getOverviewId().getMeasureId() != null && overview.getOverviewId().getMeasureId() > -1) {
            lastCheckIn = new OverviewLastCheckInMetricDto(overview.getOverviewId().getMeasureId(),
                    overview.getMeasureValue(), 5, // TODO set confidence value properly
                    overview.getCreatedOn());
        }
        return new OverviewKeyResultMetricDto(overview.getOverviewId().getKeyResultId(), overview.getKeyResultTitle(),
                overview.getUnit(), overview.getBasisValue(), overview.getTargetValue(), lastCheckIn);
    }
}
