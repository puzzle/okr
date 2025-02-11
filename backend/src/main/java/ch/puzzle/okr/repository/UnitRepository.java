package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Unit;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnitRepository extends DeleteRepository<Unit, Long> {

    Optional<Unit> findUnitByUnitNameAndIsDeletedFalse(String name);

    List<Unit> findAllByCreatedByIdAndIsDeletedFalse(Long userId);

    boolean existsUnitByUnitNameAndIsDeletedFalse(String unitName);

    boolean existsUnitByUnitNameAndIdNotAndIsDeletedFalse(String unitName, Long id);
}
