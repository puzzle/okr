package ch.puzzle.okr.service.persistence.customCrud;

import org.springframework.data.repository.CrudRepository;

public interface DeleteMethod<T, I, R extends CrudRepository<T, I>> {
//    protected R repo;
    void deleteById(I id, R repo);
    Iterable<T> findAll(R repo);

//    public void setRepo(R repo) {
//        this.repo = repo;
//    }
}
