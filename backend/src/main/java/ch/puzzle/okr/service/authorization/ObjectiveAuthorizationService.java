package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveAuthorizationService {

    private final ObjectiveBusinessService objectiveBusinessService;
    private final AuthorizationService authorizationService;

    public ObjectiveAuthorizationService(ObjectiveBusinessService objectiveBusinessService,
            AuthorizationService authorizationService) {
        this.objectiveBusinessService = objectiveBusinessService;
        this.authorizationService = authorizationService;
    }

    public Objective getObjectiveById(Long id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleReadByObjectiveId(id, authorizationUser);
        return objectiveBusinessService.getObjectiveById(id);
    }

    public Objective updateObjective(Long id, Objective objective, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleCreateOrUpdate(objective, authorizationUser);
        return objectiveBusinessService.updateObjective(id, objective, authorizationUser);
    }

    public Objective createObjective(Objective objective, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleCreateOrUpdate(objective, authorizationUser);
        return objectiveBusinessService.createObjective(objective, authorizationUser);
    }

    public void deleteObjectiveById(Long id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        authorizationService.hasRoleDeleteByObjectiveId(id, authorizationUser);
        objectiveBusinessService.deleteObjectiveById(id);
    }
}