package ch.puzzle.okr.service.persistence.customCrud;

import ch.puzzle.okr.models.Deletable;
import ch.puzzle.okr.repository.DeleteRepository;
import ch.puzzle.okr.specifications.DeleteSpecifications;

public class SoftDelete<T extends Deletable, I, R extends DeleteRepository<T, I>> implements DeleteMethod<T, I, R> {
    @Override
    public void deleteById(I id, R repo) {
        repo.markAsDeleted(id);
    }

    @Override
    public Iterable<T> findAll(R repo) {
        return repo.findAll(DeleteSpecifications.isNotMarkedAsDeleted());
    }
}
