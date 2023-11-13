package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.overview.DashboardDto;
import ch.puzzle.okr.dto.overview.OverviewDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DashboardMapper {

    public DashboardDto toDto(List<OverviewDto> overviews, Boolean adminAccess) {
        return new DashboardDto(adminAccess, overviews);
    }
}
