package ch.puzzle.okr.service.persistence.customCrud;

import org.springframework.data.repository.CrudRepository;

public class HardDelete<T, I, R extends CrudRepository<T, I>> implements DeleteMethod<T, I, R> {

    @Override
    public void deleteById(I id, R repo) {
        repo.deleteById(id);
    }

    @Override
    public Iterable<T> findAll(R repo) {
        return repo.findAll();
    }
}
