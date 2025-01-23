package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.UnitBusinessService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class UnitAuthorizationService {
    private final UnitBusinessService unitBusinessService;
    private final AuthorizationService authorizationService;

    public UnitAuthorizationService(UnitBusinessService unitBusinessService,
                                    AuthorizationService authorizationService) {
        this.unitBusinessService = unitBusinessService;
        this.authorizationService = authorizationService;
    }

    public List<Unit> getAllUnits() {
        return unitBusinessService.getAllUnits();
    }

    public List<Unit> getUnitsOfUser() {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        return unitBusinessService.findUnitsByUser(authorizationUser.user().getId());
    }

    public Unit createUnit(Unit unit) {
        return unitBusinessService.createEntity(unit);
    }

    public Unit updateUnit(Long unitId, Unit unit) {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        validateOwner(unit, authorizationUser);
        return unitBusinessService.updateEntity(unitId, unit);
    }

    public void deleteUnitById(Long unitId) {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        Unit unit = unitBusinessService.getEntityById(unitId);
        validateOwner(unit, authorizationUser);
        unitBusinessService.deleteEntityById(unitId);
    }

    private void validateOwner(Unit unit, AuthorizationUser authorizationUser) {
        boolean isOwner = unitBusinessService
                .getEntityById(unit.getId())
                .getCreatedBy()
                .getId()
                .equals(authorizationUser.user().getId());
        if (isOwner) {
            return;
        }
        throw new OkrResponseStatusException(HttpStatus.FORBIDDEN, ErrorKey.NOT_AUTHORIZED_TO_DELETE, Constants.UNIT);
    }
}
