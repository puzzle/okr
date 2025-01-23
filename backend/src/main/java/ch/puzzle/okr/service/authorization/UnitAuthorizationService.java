package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UnitBusinessService;
import ch.puzzle.okr.service.validation.UnitValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UnitAuthorizationService {
    private final UnitBusinessService unitBusinessService;
    private final AuthorizationService authorizationService;
    private final UnitValidationService unitValidationService;

    public UnitAuthorizationService(UnitBusinessService unitBusinessService,
                                    AuthorizationService authorizationService,
                                    UnitValidationService unitValidationService) {
        this.unitBusinessService = unitBusinessService;
        this.authorizationService = authorizationService;
        this.unitValidationService = unitValidationService;
    }

    public Unit createUnit(Unit unit) {
        return unitBusinessService.createEntity(unit);
    }

    public Unit editUnit(Long unitId, Unit unit) {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        unitValidationService.validateOnUpdate(unitId, unit);
        if (!isOwner(unit, authorizationUser)) {
            throw new OkrResponseStatusException(HttpStatus.FORBIDDEN,
                                                 ErrorKey.NOT_AUTHORIZED_TO_WRITE,
                                                 Constants.UNIT);
        }
        return unitBusinessService.updateEntity(unit.getId(), unit);
    }

    private boolean isOwner(Unit unit, AuthorizationUser authorizationUser) {
        return unitBusinessService
                .getEntityById(unit.getId())
                .getCreatedBy()
                .getId()
                .equals(authorizationUser.user().getId());
    }
}
