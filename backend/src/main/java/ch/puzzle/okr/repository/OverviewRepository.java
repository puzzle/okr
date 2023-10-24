package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OverviewRepository extends ReadOnlyRepository<Overview, OverviewId> {

    @Query(value = "from Overview where quarterId = :quarter_id and overviewId.teamId in (:team_ids) and lower(objectiveTitle) like %:objective_query%")
    List<Overview> getFilteredOverview(@Param("quarter_id") Long quarterId, @Param("team_ids") List<Long> teamIds,
            @Param("objective_query") String objectiveQuery);
}
