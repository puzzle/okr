package ch.puzzle.okr.service.persistence;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.service.persistence.customCrud.DeleteMethod;
import ch.puzzle.okr.service.persistence.customCrud.HardDelete;
import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <I>
 *            the Identifier or primary key of the entity
 * @param <R>
 *            the Repository of the entity
 */
public abstract class PersistenceBase<T, I, R extends CrudRepository<T, I>> {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceBase.class);

    private final R repository;
    private final DeleteMethod<T, I, R> deleteMethod;

    protected PersistenceBase(R repository, DeleteMethod<T, I, R> deleteMethod) {
        this.repository = repository;
        this.deleteMethod = deleteMethod;
        deleteMethod.setRepo(repository);
    }

    protected PersistenceBase(R repository) {
        this.repository = repository;
        this.deleteMethod = new HardDelete<>();
    }

    public R getRepository() {
        return repository;
    }

    public T findById(I id) throws OkrResponseStatusException {
        checkIdNull(id);
        return repository.findById(id).orElseThrow(() -> createEntityNotFoundException(id));
    }

    public void checkIdNull(I id) {
        if (id == null) {
            throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL, List.of("ID", getModelName()));
        }
    }

    public OkrResponseStatusException createEntityNotFoundException(I id) {
        throw new OkrResponseStatusException(NOT_FOUND, ErrorKey.MODEL_WITH_ID_NOT_FOUND, List.of(getModelName(), id));
    }

    public T save(T model) throws OkrResponseStatusException {
        try {
            return repository.save(model);
        } catch (OptimisticLockingFailureException ex) {
            logger.info("optimistic locking exception while saving {}", model, ex);
            throw new OkrResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY,
                                                 ErrorKey.DATA_HAS_BEEN_UPDATED,
                                                 getModelName());
        }
    }

    public List<T> findAll() {
        return iteratorToList(this.deleteMethod.findAll());
    }

    public void deleteById(I id) {
        deleteMethod.setRepo(repository);
        deleteMethod.deleteById(id);
    }

    public void deleteById(I id, DeleteMethod<T, I, R> localDeleteMethod) {
        localDeleteMethod.setRepo(repository);
        localDeleteMethod.deleteById(id);
    }

    public abstract String getModelName();

    private List<T> iteratorToList(Iterable<T> iterable) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), Spliterator.ORDERED), false)
                .toList();
    }
}
