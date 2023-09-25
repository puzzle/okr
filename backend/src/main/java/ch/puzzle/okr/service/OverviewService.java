package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.overview.OverviewDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OverviewService {
    private final OverviewMapper overviewMapper;
    private final OverviewBusinessService overviewBusinessService;

    public OverviewService(OverviewMapper overviewMapper, OverviewBusinessService overviewBusinessService) {
        this.overviewMapper = overviewMapper;
        this.overviewBusinessService = overviewBusinessService;
    }

    public List<OverviewDto> getOverviewByQuarterIdAndTeamIds(Long quarterFilter, List<Long> teamFilter) {
        return overviewMapper
                .toDto(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterFilter, teamFilter));
    }
}
