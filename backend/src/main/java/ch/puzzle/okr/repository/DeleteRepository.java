package ch.puzzle.okr.repository;

import ch.puzzle.okr.models.Deletable;
import java.util.List;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.query.Param;

@NoRepositoryBean
public interface DeleteRepository<E extends Deletable, I> extends CrudRepository<E, I>, JpaSpecificationExecutor<E> {

    @Query("select e from #{#entityName} e where e.isDeleted = false ")
    List<E> findAllVisible();

    @Modifying
    @Query("update #{#entityName} e set e.isDeleted = true where e.id = :id")
    void markAsDeleted(@Param("id") I id);
}
