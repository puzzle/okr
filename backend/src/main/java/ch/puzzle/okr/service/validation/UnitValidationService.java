package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.repository.UnitRepository;
import ch.puzzle.okr.service.persistence.UnitPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class UnitValidationService extends ValidationBase<Unit, Long, UnitRepository, UnitPersistenceService> {

    public UnitValidationService(UnitPersistenceService unitPersistenceService) {
        super(unitPersistenceService);
    }

    @Override
    public void validateOnCreate(Unit model) {
        throwExceptionWhenModelIsNull(model);
        throwErrorWhenUnitIsNotUnique(model);
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Unit model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenIdHasChanged(id, model.getId());
        if (!Objects.equals(this.getPersistenceService().findById(id).getUnitName(), model.getUnitName())) {
            throwErrorWhenUnitIsNotUnique(model);
        }
        validate(model);
    }

    private void throwErrorWhenUnitIsNotUnique(Unit model) {
        if (this.getPersistenceService().existsUnitByUnitName(model.getUnitName())) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST,
                                                 ErrorKey.ATTRIBUTE_MUST_BE_UNIQUE,
                                                 List.of("unitname",model.getUnitName(), this.getPersistenceService().getModelName()));
        }
    }
}
