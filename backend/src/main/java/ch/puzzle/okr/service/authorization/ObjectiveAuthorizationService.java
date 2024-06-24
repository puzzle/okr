package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.dto.AlignmentDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectiveAuthorizationService extends AuthorizationServiceBase<Long, Objective, ObjectiveBusinessService> {

    public ObjectiveAuthorizationService(ObjectiveBusinessService objectiveBusinessService,
            AuthorizationService authorizationService) {
        super(objectiveBusinessService, authorizationService);
    }

    public Objective duplicateEntity(Long id, Objective objective) {
        AuthorizationUser authorizationUser = getAuthorizationService().getAuthorizationUser();
        hasRoleCreateOrUpdate(objective, authorizationUser);
        return getBusinessService().duplicateObjective(id, objective, authorizationUser);
    }

    public List<AlignmentDto> getAlignmentPossibilities(Long quarterId) {
        return getBusinessService().getAlignmentPossibilities(quarterId);
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
        return getAuthorizationService().isWriteable(entity, authorizationUser);
    }

    public boolean isImUsed(Objective objective) {
        return getBusinessService().isImUsed(objective);
    }
}