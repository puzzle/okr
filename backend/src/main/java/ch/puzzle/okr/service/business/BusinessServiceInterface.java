package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.authorization.AuthorizationUser;

public interface BusinessServiceInterface<K, E> {
    E getEntityById(K id);

    E createEntity(E entity, AuthorizationUser authorizationUser);

    E updateEntity(K id, E entity, AuthorizationUser authorizationUser);

    void deleteEntityById(K id);
}
