package ch.puzzle.okr.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

/**
 * @param <I>
 *            the Identifier or primary key of the entity
 * @param <T>
 *            the Type or entity of the repository
 */
@NoRepositoryBean
public interface ReadOnlyRepository<T, I> extends Repository<T, I> {
}
