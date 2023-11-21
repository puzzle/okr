package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.ErrorMsg;
import ch.puzzle.okr.models.OkrResponseStatusException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

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

    public R getRepository() {
        return (R) repository;
    }

    public T findById(ID id) throws OkrResponseStatusException {
        checkIdNull(id);
        return repository.findById(id).orElseThrow(() -> createEntityNotFoundException(id));
    }

    public void checkIdNull(ID id) {
        if (id == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorMsg.NULL_ATTRIBUTE_ON_MODEL);
            // throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
            // format("Missing identifier for %s", getModelName()));
        }
    }

    public OkrResponseStatusException createEntityNotFoundException(ID id) {
        throw new OkrResponseStatusException(HttpStatus.NOT_FOUND, ErrorMsg.MODEL_WITH_ID_NOT_FOUND,
                List.of(getModelName(), id));
        // return new ResponseStatusException(NOT_FOUND, format("%s with id %s not found", getModelName(), id));
    }

    public T save(T model) throws OkrResponseStatusException {
        try {
            return repository.save(model);
        } catch (OptimisticLockingFailureException ex) {
            logger.info("optimistic locking exception while saving {}", model, ex);
            throw new OkrResponseStatusException(HttpStatus.UNPROCESSABLE_ENTITY, ErrorMsg.DATA_HAS_BEEN_UPDATED,
                    List.of(getModelName()));

            // throw new ResponseStatusException(UNPROCESSABLE_ENTITY,
            // String.format("The data of %s has been updated or deleted by another user." +
            // "Please reload the data and apply your changes again.", getModelName()));
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
