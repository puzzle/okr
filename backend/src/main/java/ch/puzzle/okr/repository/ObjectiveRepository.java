package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Objective;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ObjectiveRepository extends CrudRepository<Objective, Long> {
    List<Objective> findByTeamIdOrderByTitleAsc(Long id);

    List<Objective> findByQuarterIdAndTeamIdOrderByModifiedOnDesc(Long quarterId, Long teamId);
}
