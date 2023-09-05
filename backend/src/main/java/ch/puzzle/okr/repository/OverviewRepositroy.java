package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Overview;
import ch.puzzle.okr.models.OverviewId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OverviewRepositroy extends ReadOnlyRepository<Overview, OverviewId> {

    @Query(value = "from Overview where quarterId = :quarter_id and overviewId.teamId in (:team_ids)")
    List<Overview> getOverviewByQuarterIdAndTeamIds(@Param("quarter_id") Long quarterId,
            @Param("team_ids") List<Long> teamIds);

    @Query(value = "from Overview where quarterId = :quarter_id")
    List<Overview> getOverviewByQuarterId(@Param("quarter_id") Long quarterId);
}
