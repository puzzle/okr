package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Service
public class ObjectiveAuthorizationService extends AuthorizationServiceBase<Long, Objective, ObjectiveBusinessService> {

    public ObjectiveAuthorizationService(ObjectiveBusinessService objectiveBusinessService,
            AuthorizationService authorizationService) {
        super(objectiveBusinessService, authorizationService);
    }

    public Objective duplicateEntity(Long id, Objective objective, List<KeyResult> keyResults) {
        AuthorizationUser authorizationUser = getAuthorizationService().updateOrAddAuthorizationUser();
        hasRoleCreateOrUpdate(objective, authorizationUser);
        return getBusinessService().duplicateObjective(id, objective, authorizationUser, keyResults);
    }

    public List<KeyResult> getAllKeyResultsByObjective(Long objectiveId) {
        AuthorizationUser authorizationUser = getAuthorizationService().updateOrAddAuthorizationUser();
        getAuthorizationService().hasRoleReadByObjectiveId(objectiveId, authorizationUser);
        List<KeyResult> keyResults = getBusinessService().getAllCheckInsByKeyResult(objectiveId);
        setRoleCreateOrUpdateKeyResult(keyResults, authorizationUser);
        return keyResults;
    }

    @Override
    protected void hasRoleReadById(Long id, AuthorizationUser authorizationUser) {
        getAuthorizationService().hasRoleReadByObjectiveId(id, authorizationUser);
    }

    @Override
    protected void hasRoleCreateOrUpdate(Objective entity, AuthorizationUser authorizationUser) {
        getAuthorizationService().hasRoleCreateOrUpdate(entity, authorizationUser);
    }

    @Override
    protected void hasRoleDeleteById(Long id, AuthorizationUser authorizationUser) {
        getAuthorizationService().hasRoleDeleteByObjectiveId(id, authorizationUser);
    }

    @Override
    protected boolean isWriteable(Objective entity, AuthorizationUser authorizationUser) {
        return getAuthorizationService().hasRoleWriteForTeam(entity, authorizationUser);
    }

    public boolean isImUsed(Objective objective) {
        return getBusinessService().isImUsed(objective);
    }

    private void setRoleCreateOrUpdateKeyResult(List<KeyResult> keyResults, AuthorizationUser authorizationUser) {
        if (!CollectionUtils.isEmpty(keyResults)) {
            boolean isWriteable = getAuthorizationService().hasRoleWriteForTeam(keyResults.get(0), authorizationUser);
            keyResults.forEach(c -> c.setWriteable(isWriteable));
        }
    }

}