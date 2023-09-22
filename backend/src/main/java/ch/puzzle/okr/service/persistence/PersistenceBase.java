package ch.puzzle.okr.service.persistence;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public abstract class PersistenceBase<T, E> {
    protected final CrudRepository<T, E> repository;

    protected PersistenceBase(CrudRepository<T, E> repository) {
        this.repository = repository;
    }

    public T findById(E id) throws ResponseStatusException {
        if (id == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    String.format("Missing identifier for %s", getModelName()));
        }
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", getModelName(), id)));
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
