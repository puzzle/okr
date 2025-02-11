package ch.puzzle.okr.service.persistence.customCrud;

import org.springframework.data.repository.CrudRepository;

public class HardDelete<T, I, R extends CrudRepository<T, I>> extends DeleteMethod<T, I, R> {

    @Override
    public void deleteById(I id) {
        this.repo.deleteById(id);
    }

    @Override
    public Iterable<T> findAll() {
        return this.repo.findAll();
    }
}
