package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * @param <T>
 *            the Type or entity of the repository
 * @param <ID>
 *            the Identifier or primary key of the entity
 * @param <R>
 *            the Repository of the entity
 */
public abstract class PersistenceBase<T, ID, R> {

    private static final Logger logger = LoggerFactory.getLogger(PersistenceBase.class);

    private final CrudRepository<T, ID> repository;

    protected PersistenceBase(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    @SuppressWarnings(value = "unchecked casts")
    public R getRepository() {
        return (R) repository;
    }

    public T findById(ID id) throws OkrResponseStatusException {
        checkIdNull(id);
        return repository.findById(id).orElseThrow(() -> createEntityNotFoundException(id));
    }

    public void checkIdNull(ID id) {
        if (id == null) {
            throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL, List.of("ID", getModelName()));
        }
    }

    public OkrResponseStatusException createEntityNotFoundException(ID id) {
        throw new OkrResponseStatusException(NOT_FOUND, ErrorKey.MODEL_WITH_ID_NOT_FOUND, List.of(getModelName(), id));
    }

    public T save(T model) throws OkrResponseStatusException {
        try {
            return repository.save(model);
        } catch (OptimisticLockingFailureException ex) {
            logger.info("optimistic locking exception while saving {}", model, ex);
            throw new OkrResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ErrorKey.DATA_HAS_BEEN_UPDATED,
                    getModelName());
        }
    }

    public List<T> findAll() {
        return iteratorToList(repository.findAll());
    }

    public void deleteById(ID id) {
        repository.deleteById(id);
    }

    public abstract String getModelName();

    private List<T> iteratorToList(Iterable<T> iterable) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), Spliterator.ORDERED), false).toList();
    }
}
