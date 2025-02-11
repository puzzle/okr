package ch.puzzle.okr.service.persistence.customCrud;

import org.springframework.data.repository.CrudRepository;

public abstract class DeleteMethod<T, I, R extends CrudRepository<T, I>> {
    protected R repo;
    public abstract void deleteById(I id);

    public void setRepo(R repo){
        this.repo  = repo;
    }
}
