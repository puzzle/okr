package ch.puzzle.okr.service.business;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import ch.puzzle.okr.service.validation.UnitValidationService;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

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
        return unitPersistenceService
                .findUnitByUnitName(unitName);
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
    public Unit updateEntity(Long id, Unit unit) {
        validator.validateOnUpdate(id, unit);
        return unitPersistenceService.save(unit);
    }

    @Transactional
    public void deleteEntityById(Long id) {
        validator.validateOnDelete(id);
        unitPersistenceService.deleteById(id);
    }
}
