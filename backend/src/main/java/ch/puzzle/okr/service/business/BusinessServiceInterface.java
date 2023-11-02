package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.authorization.AuthorizationUser;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <ID>
 *            the Identifier or primary key of the entity
 */
public interface BusinessServiceInterface<ID, T> {
    T getEntityById(ID id);

    T createEntity(T entity, AuthorizationUser authorizationUser);

    T updateEntity(ID id, T entity, AuthorizationUser authorizationUser);

    void deleteEntityById(ID id);
}
