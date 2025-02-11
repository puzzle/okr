package ch.puzzle.okr.service.persistence.customCrud;

import ch.puzzle.okr.models.Deletable;
import ch.puzzle.okr.repository.DeleteRepository;


public class SoftDelete <T extends Deletable, I, R extends DeleteRepository<T, I>> extends DeleteMethod<T, I, R> {
    @Override
    public void deleteById(I id) {
        repo.markAsDeleted(id);
    }
}
