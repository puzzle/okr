package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.WriteableInterface;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.BusinessServiceInterface;
import org.springframework.security.oauth2.jwt.Jwt;

/**
 * @param <ID>
 *            the Identifier or primary key of the entity
 * @param <T>
 *            the Type or entity of the repository
 * @param <BS>
 *            the Business Service of this entity
 */
public abstract class AuthorizationServiceBase<ID, T extends WriteableInterface, BS> {
    private final BusinessServiceInterface<ID, T> businessService;
    private final AuthorizationService authorizationService;

    public AuthorizationServiceBase(BusinessServiceInterface<ID, T> businessService,
            AuthorizationService authorizationService) {
        this.businessService = businessService;
        this.authorizationService = authorizationService;
    }

    protected abstract void hasRoleReadById(ID id, AuthorizationUser authorizationUser);

    protected abstract void hasRoleCreateOrUpdate(T entity, AuthorizationUser authorizationUser);

    protected abstract void hasRoleDeleteById(ID id, AuthorizationUser authorizationUser);

    protected abstract boolean isWriteable(T entity, AuthorizationUser authorizationUser);

    public T getEntityById(ID id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        hasRoleReadById(id, authorizationUser);
        T entity = businessService.getEntityById(id);
        setRoleCreateOrUpdate(entity, authorizationUser);
        return entity;
    }

    public T createEntity(T entity, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        hasRoleCreateOrUpdate(entity, authorizationUser);
        return businessService.createEntity(entity, authorizationUser);
    }

    public T updateEntity(ID id, T entity, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        hasRoleCreateOrUpdate(entity, authorizationUser);
        return businessService.updateEntity(id, entity, authorizationUser);
    }

    public void deleteEntityById(ID id, Jwt token) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser(token);
        hasRoleDeleteById(id, authorizationUser);
        businessService.deleteEntityById(id);
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public BS getBusinessService() {
        return (BS) businessService;
    }

    private void setRoleCreateOrUpdate(T entity, AuthorizationUser authorizationUser) {
        entity.setWriteable(isWriteable(entity, authorizationUser));
    }
}
