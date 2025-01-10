package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.authorization.AuthorizationUser;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <I>
 *            the Identifier or primary key of the entity
 */
public interface BusinessServiceInterface<I, T> {
    T getEntityById(I id);

    T createEntity(T entity, AuthorizationUser authorizationUser);

    T updateEntity(I id, T entity, AuthorizationUser authorizationUser);

    void deleteEntityById(I id);
}
