package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.BusinessServiceInterface;
import org.springframework.security.oauth2.jwt.Jwt;

public abstract class AuthorizationServiceBase<K, E, B> {
    private final BusinessServiceInterface<K, E> businessService;
    private final AuthorizationService authorizationService;

    public AuthorizationServiceBase(BusinessServiceInterface<K, E> businessService,
            AuthorizationService authorizationService) {
        this.businessService = businessService;
        this.authorizationService = authorizationService;
    }

    protected abstract void hasRoleReadById(K id, AuthorizationUser authorizationUser);

    protected abstract void hasRoleCreateOrUpdate(E entity, AuthorizationUser authorizationUser);

    protected abstract void hasRoleDeleteById(K id, AuthorizationUser authorizationUser);

    public E getEntityById(K id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        hasRoleReadById(id, authorizationUser);
        return businessService.getEntityById(id);
    }

    public E createEntity(E entity, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        hasRoleCreateOrUpdate(entity, authorizationUser);
        return businessService.createEntity(entity, authorizationUser);
    }

    public E updateEntity(K id, E entity, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        hasRoleCreateOrUpdate(entity, authorizationUser);
        return businessService.updateEntity(id, entity, authorizationUser);
    }

    public void deleteEntityById(K id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        hasRoleDeleteById(id, authorizationUser);
        businessService.deleteEntityById(id);
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public B getBusinessService() {
        return (B) businessService;
    }
}
