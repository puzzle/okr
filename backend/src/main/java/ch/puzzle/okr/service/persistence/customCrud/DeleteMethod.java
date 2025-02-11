package ch.puzzle.okr.service.persistence.customCrud;

import ch.puzzle.okr.models.Deletable;
import ch.puzzle.okr.repository.DeleteRepository;
import org.springframework.data.repository.CrudRepository;

public interface DeleteMethod {
    <T , I, R extends CrudRepository<T, I>> void deleteById(I id, R repo);
    DeleteMethod build();
}
