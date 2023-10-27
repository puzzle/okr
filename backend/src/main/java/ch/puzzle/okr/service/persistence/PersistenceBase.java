package ch.puzzle.okr.service.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

import static java.lang.String.format;
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

    protected final CrudRepository<T, ID> repository;

    protected PersistenceBase(CrudRepository<T, ID> repository) {
        this.repository = repository;
    }

    public R getRepository() {
        return (R) repository;
    }

    public T findById(ID id) throws ResponseStatusException {
        checkIdNull(id);
        return repository.findById(id).orElseThrow(() -> createEntityNotFoundException(id));
    }

    public void checkIdNull(ID id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    format("Missing identifier for %s", getModelName()));
        }
    }

    public ResponseStatusException createEntityNotFoundException(ID id) {
        return new ResponseStatusException(NOT_FOUND, format("%s with id %s not found", getModelName(), id));
    }

    public T save(T model) {
        return repository.save(model);
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
