package ch.puzzle.okr.repository;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

@NoRepositoryBean
public interface ReadOnlyRepository<Type, Id> extends Repository<Type, Id> {
}
