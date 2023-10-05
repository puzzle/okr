package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OverviewRepository extends ReadOnlyRepository<Overview, OverviewId> {

    @Query(value = "from Overview where quarterId = :quarter_id and overviewId.teamId in (:team_ids)")
    List<Overview> getOverviewByQuarterIdAndTeamIds(@Param("quarter_id") Long quarterId,
            @Param("team_ids") List<Long> teamIds);

    // TODO remove function below as soon as teamids are able to be read from jwt token
    @Query(value = "from Overview where quarterId = :quarter_id")
    List<Overview> getOverviewByQuarterId(@Param("quarter_id") Long quarterId);
}
