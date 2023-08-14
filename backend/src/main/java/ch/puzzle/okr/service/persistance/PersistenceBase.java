package ch.puzzle.okr.service.persistance;

import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.stream.StreamSupport;

public abstract class PersistenceBase<Model, Id> {
    protected final CrudRepository<Model, Id> repository;

    protected PersistenceBase(CrudRepository<Model, Id> repository) {
        this.repository = repository;
    }

    public Model findById(Id id) throws ResponseStatusException {
        return repository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("%s with id %s not found", getModelName(), id)));
    }

    public Model save(Model model) {
        return repository.save(model);
    }

    public List<Model> findAll() {
        return iteratorToList(repository.findAll());
    }

    public void deleteById(Id id) {
        repository.deleteById(id);
    }

    public abstract String getModelName();

    private List<Model> iteratorToList(Iterable<Model> iterable) {
        return StreamSupport
                .stream(Spliterators.spliteratorUnknownSize(iterable.iterator(), Spliterator.ORDERED), false).toList();
    }
}
