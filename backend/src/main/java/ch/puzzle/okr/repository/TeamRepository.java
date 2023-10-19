package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Team;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TeamRepository extends CrudRepository<Team, Long> {

    Optional<Team> findByRoleName(String roleName);

    @Query(value = "select t.id from Team t where t.roleName IN (:roleNames)")
    List<Long> findByRoleNames(@Param("roleNames") List<String> roleNames);
}
