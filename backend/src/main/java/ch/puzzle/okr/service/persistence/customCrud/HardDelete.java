package ch.puzzle.okr.service.persistence.customCrud;

import ch.puzzle.okr.models.Deletable;
import org.springframework.data.repository.CrudRepository;


public class HardDelete<T, I, R extends CrudRepository<T, I>> extends DeleteMethod<T, I, R>{

    @Override
    public void deleteById(I id) {
        this.repo.deleteById(id);
    }
}
