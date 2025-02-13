package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Unit;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends DeleteRepository<Unit, Long> {

    Optional<Unit> findUnitByUnitName(String name);

    List<Unit> findAllByCreatedById(Long userId);

    boolean existsUnitByUnitName(String unitName);

    boolean existsUnitByUnitNameAndIdNot(String unitName, Long id);
}
