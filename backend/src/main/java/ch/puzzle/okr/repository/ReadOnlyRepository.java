package ch.puzzle.okr.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * @param <ID>
 *            the Identifier or primary key of the entity
 * @param <T>
 *            the Type or entity of the repository
 */
@NoRepositoryBean
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {
}
