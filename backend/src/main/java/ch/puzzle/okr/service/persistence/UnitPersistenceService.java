package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.UNIT;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.repository.UnitRepository;
import java.util.List;

import ch.puzzle.okr.service.persistence.customCrud.SoftDelete;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UnitPersistenceService extends PersistenceBase<Unit, Long, UnitRepository> {

    protected UnitPersistenceService(UnitRepository repository) {
        super(repository, new SoftDelete<>());
    }

    @Override
    public String getModelName() {
        return UNIT;
    }

    public Unit findUnitByUnitName(String unitName) {
        return getRepository()
                .findUnitByUnitNameAndIsDeletedFalse(unitName)
                .orElseThrow(() -> new OkrResponseStatusException(HttpStatus.NOT_FOUND,
                                                                  ErrorKey.MODEL_NOT_FOUND_BY_PROPERTY,
                                                                  List.of(Constants.UNIT, "unit name", unitName)));
    }

    public boolean existsUnitByUnitName(String unitName) {
        return getRepository().existsUnitByUnitNameAndIsDeletedFalse(unitName);
    }

    public boolean existsUnitByUnitNameAndIdNot(String unitName, Long id) {
        return getRepository().existsUnitByUnitNameAndIdNotAndIsDeletedFalse(unitName, id);
    }

    public List<Unit> findUnitsByUser(Long userId) {
        return getRepository().findAllByCreatedByIdAndIsDeletedFalse(userId);
    }
}
