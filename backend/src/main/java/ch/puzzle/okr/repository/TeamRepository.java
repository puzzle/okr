package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TeamRepository extends CrudRepository<Team, Long> {
    @Query(value = "select t from Team as t where coalesce(:teamIds, null) is null or t.id in (:teamIds)")
    List<Team> findAllById(@Param("teamIds") List<Long> teamIds);
}
