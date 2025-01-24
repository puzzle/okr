package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import ch.puzzle.okr.service.validation.UnitValidationService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class UnitBusinessService {
    private final UnitPersistenceService unitPersistenceService;
    private final UnitValidationService validator;

    public UnitBusinessService(UnitPersistenceService unitPersistenceService, UnitValidationService validator) {
        this.unitPersistenceService = unitPersistenceService;
        this.validator = validator;
    }

    public List<Unit> getAllUnits() {
        return unitPersistenceService.findAll();
    }

    public List<Unit> findUnitsByUser(Long userId) {
        validator.validateOnGet(userId);
        return unitPersistenceService.findUnitsByUser(userId);
    }

    public Unit findUnitByName(String unitName) {
        return unitPersistenceService.findUnitByUnitName(unitName);
    }

    public Unit getEntityById(Long id) {
        validator.validateOnGet(id);
        return unitPersistenceService.findById(id);
    }

    @Transactional
    public Unit createEntity(Unit unit) {
        validator.validateOnCreate(unit);
        return unitPersistenceService.save(unit);
    }

    @Transactional
    public Unit updateEntity(Long id, Unit newUnit) {
        validator.validateOnUpdate(id, newUnit);
        Unit oldUnit = unitPersistenceService.findById(id);
        oldUnit.setUnitName(newUnit.getUnitName());
        return unitPersistenceService.save(oldUnit);
    }

    @Transactional
    public void deleteEntityById(Long id) {
        validator.validateOnDelete(id);
        unitPersistenceService.deleteById(id);
    }
}
