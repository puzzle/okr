package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.repository.UnitRepository;
import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import org.springframework.stereotype.Service;

@Service
public class UnitValidationService extends ValidationBase<Unit, Long, UnitRepository, UnitPersistenceService> {

    private final KeyResultValidationService keyResultValidationService;

    public UnitValidationService(UnitPersistenceService unitPersistenceService,
                                 KeyResultValidationService keyResultValidationService) {
        super(unitPersistenceService);
        this.keyResultValidationService = keyResultValidationService;
    }

    public void validateOnGetByKeyResultId(Long keyResultId) {
        keyResultValidationService.validateOnGet(keyResultId);
    }

    @Override
    public void validateOnCreate(Unit model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());

        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Unit model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());
        validate(model);
    }
}
