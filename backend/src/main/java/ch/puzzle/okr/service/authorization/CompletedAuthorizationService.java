package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.CompletedBusinessService;
import org.springframework.stereotype.Service;

@Service
public class CompletedAuthorizationService {
    private final CompletedBusinessService completedBusinessService;
    private final AuthorizationService authorizationService;

    public CompletedAuthorizationService(CompletedBusinessService completedBusinessService,
            AuthorizationService authorizationService) {
        this.completedBusinessService = completedBusinessService;
        this.authorizationService = authorizationService;
    }

    public Completed createCompleted(Completed completed) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        authorizationService.hasRoleCreateOrUpdateByObjectiveId(completed.getObjective().getId(), authorizationUser);
        return completedBusinessService.createCompleted(completed);
    }

    public void deleteCompletedByObjectiveId(Long objectiveId) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        authorizationService.hasRoleDeleteByObjectiveId(objectiveId, authorizationUser);
        completedBusinessService.deleteCompletedByObjectiveId(objectiveId);
    }
}
