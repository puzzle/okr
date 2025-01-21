package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Unit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UnitRepository extends CrudRepository<Unit, Long> {

        Optional<Unit> findUnitByUnitName(String name);

}
