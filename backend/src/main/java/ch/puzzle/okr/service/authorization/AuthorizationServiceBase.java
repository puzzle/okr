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

    protected AuthorizationServiceBase(BusinessServiceInterface<ID, T> businessService,
                                       AuthorizationService authorizationService) {
        this.businessService = businessService;
        this.authorizationService = authorizationService;
    }

    protected abstract void hasRoleReadById(ID id, AuthorizationUser authorizationUser);

    protected abstract void hasRoleCreateOrUpdate(T entity, AuthorizationUser authorizationUser);

    protected abstract void hasRoleDeleteById(ID id, AuthorizationUser authorizationUser);

    protected abstract boolean isWriteable(T entity, AuthorizationUser authorizationUser);

    public T getEntityById(ID id) {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        hasRoleReadById(id, authorizationUser);
        T entity = businessService.getEntityById(id);
        entity.setWriteable(isWriteable(entity, authorizationUser));
        return entity;
    }

    public T createEntity(T entity) {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        hasRoleCreateOrUpdate(entity, authorizationUser);
        T savedEntity = businessService.createEntity(entity, authorizationUser);
        savedEntity.setWriteable(true);
        return savedEntity;
    }

    public T updateEntity(ID id, T entity) {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        hasRoleCreateOrUpdate(entity, authorizationUser);
        T updatedEntity = businessService.updateEntity(id, entity, authorizationUser);
        updatedEntity.setWriteable(true);
        return updatedEntity;
    }

    public void deleteEntityById(ID id) {
        AuthorizationUser authorizationUser = authorizationService.updateOrAddAuthorizationUser();
        hasRoleDeleteById(id, authorizationUser);
        businessService.deleteEntityById(id);
    }

    public AuthorizationService getAuthorizationService() {
        return authorizationService;
    }

    public BS getBusinessService() {
        return (BS) businessService;
    }

}
