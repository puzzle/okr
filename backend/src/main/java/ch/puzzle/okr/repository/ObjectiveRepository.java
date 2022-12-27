package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Objective;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends CrudRepository<Objective, Long> {

    @Query(value = "select coalesce(AVG((CAST(m.value as decimal) / CAST((k.target_value - k.basis_value) as decimal))*100), 0)"
            + "from key_result as k, measure as m, (select max(created_on) as created_on, key_result_id from measure group by key_result_id) as t where t.key_result_id = m.key_result_id and t.created_on = m.created_on and k.id = m.key_result_id and k.objective_id = :objective_id", nativeQuery = true)
    Double getProgressOfObjective(@Param("objective_id") Long objectiveId);

    List<Objective> findByTeamId(long id);

    @Query(value = "select o from Objective as o where (:quarterId is null or o.quarter.id = :quarterId) and "
            + "(:teamId is null or o.team.id = :teamId)")
    List<Objective> findByQuarterIdAndTeamId(@Param("quarterId") Long quarterId, @Param("teamId") Long teamId);
}
