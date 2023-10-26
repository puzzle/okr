package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.overview.Overview;
import ch.puzzle.okr.models.overview.OverviewId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OverviewRepository extends ReadOnlyRepository<Overview, OverviewId> {

    @Query(value = "from Overview where quarterId = :quarterId and overviewId.teamId in (:teamIds) and lower(coalesce(objectiveTitle, '')) like %:objectiveQuery%")
    List<Overview> getFilteredOverview(Long quarterId, List<Long> teamIds, String objectiveQuery);
}
