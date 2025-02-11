package ch.puzzle.okr.service.persistence.customCrud;

import ch.puzzle.okr.models.Deletable;
import ch.puzzle.okr.repository.DeleteRepository;
import org.springframework.data.repository.CrudRepository;


public class SoftDelete implements DeleteMethod {

    private <T extends Deletable, I, R extends DeleteRepository<T, I>> void deleteSoftById(I id, R repo) {
        repo.markAsDeleted(id);

    }

    @Override
    public <T, I, R extends CrudRepository<T, I>> void deleteById(I id, R repo) {
        @SuppressWarnings(value = "unchecked casts")
        DeleteRepository<? extends Deletable, I> deleteRepo = (DeleteRepository<?, I>) repo;
        deleteSoftById(id, deleteRepo);
    }

    @Override
    public DeleteMethod build() {
        return new SoftDelete();
    }
}
