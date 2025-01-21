package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Unit;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {

    Optional<Unit> findUnitByUnitName(String name);

}
