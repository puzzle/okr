package ch.puzzle.okr.service.authorization;

import ch.puzzle.okr.models.WriteableInterface;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.business.BusinessServiceInterface;

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

    public T getEntityById(ID id) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        hasRoleReadById(id, authorizationUser);
        T entity = businessService.getEntityById(id);
        setRoleCreateOrUpdate(entity, authorizationUser);
        return entity;
    }

    public T createEntity(T entity) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        hasRoleCreateOrUpdate(entity, authorizationUser);
        T savedEntity = businessService.createEntity(entity, authorizationUser);
        setRoleCreateOrUpdate(savedEntity, authorizationUser);
        return savedEntity;
    }

    public T updateEntity(ID id, T entity) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
        hasRoleCreateOrUpdate(entity, authorizationUser);
        T updatedEntity = businessService.updateEntity(id, entity, authorizationUser);
        setRoleCreateOrUpdate(updatedEntity, authorizationUser);
        return updatedEntity;
    }

    public void deleteEntityById(ID id) {
        AuthorizationUser authorizationUser = authorizationService.getAuthorizationUser();
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
