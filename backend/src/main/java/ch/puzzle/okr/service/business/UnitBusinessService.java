package ch.puzzle.okr.service.business;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import ch.puzzle.okr.service.validation.UnitValidationService;
import jakarta.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UnitBusinessService {
    private final UnitPersistenceService unitPersistenceService;
    private final UnitValidationService validator;

    public UnitBusinessService(UnitPersistenceService unitPersistenceService, UnitValidationService validator) {
        this.unitPersistenceService = unitPersistenceService;
        this.validator = validator;
    }

    public Unit findUnitByName(String unitName) {
        return unitPersistenceService
                .findUnitByUnitName(unitName)
                .orElseThrow( ()-> new OkrResponseStatusException(HttpStatus.NOT_FOUND, ErrorKey.MODEL_NOT_FOUND_BY_PROPERTY, List.of(
                        Constants.UNIT, "unit name", unitName)));
    }

    public Unit getEntityById(Long id) {
        validator.validateOnGet(id);
        return unitPersistenceService.findById(id);
    }

    @Transactional
    public Unit createEntity(Unit action) {
        validator.validateOnCreate(action);
        return unitPersistenceService.save(action);
    }

    @Transactional
    public List<Unit> updateEntities(List<Unit> actionList) {
        List<Unit> savedActions = new ArrayList<>();

        return savedActions;
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
