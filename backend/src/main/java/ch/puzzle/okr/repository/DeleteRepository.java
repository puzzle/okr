package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Deletable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface DeleteRepository<E extends Deletable, T> extends CrudRepository<E, T> {

    @Modifying
    @Query("update #{#entityName} e set e.isDeleted = true where e.id = :id")
    void markAsDeleted(@Param("id") T id);
}
