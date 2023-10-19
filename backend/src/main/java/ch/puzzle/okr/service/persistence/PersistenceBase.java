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
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public abstract class PersistenceBase<T, E, R> {

    protected final CrudRepository<T, E> repository;

    protected PersistenceBase(CrudRepository<T, E> repository) {
        this.repository = repository;
    }

    public R getRepository() {
        return (R) repository;
    }

    public T findById(E id) throws ResponseStatusException {
        checkIdNull(id);
        return repository.findById(id).orElseThrow(() -> createEntityNotFoundException(id));
    }

    public void checkIdNull(E id) {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    format("Missing identifier for %s", getModelName()));
        }
    }

    public ResponseStatusException createEntityNotFoundException(E id) {
        return new ResponseStatusException(NOT_FOUND, format("%s with id %s not found", getModelName(), id));
    }

    public ResponseStatusException createUnauthorizedException(Long id) {
        return new ResponseStatusException(UNAUTHORIZED,
                String.format("not authorized to select id=%s from %s", id, getModelName()));
    }

    public T save(T model) {
        return repository.save(model);
    }

    public List<T> findAll() {
        return iteratorToList(repository.findAll());
    }

    public void deleteById(E id) {
        repository.deleteById(id);
    }

    public abstract String getModelName();

    private List<T> iteratorToList(Iterable<T> iterable) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), Spliterator.ORDERED), false).toList();
    }
}
