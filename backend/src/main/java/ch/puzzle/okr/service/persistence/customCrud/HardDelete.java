package ch.puzzle.okr.service.persistence.customCrud;

import org.springframework.data.repository.CrudRepository;


public class HardDelete implements DeleteMethod{

    @Override
    public <T, I, R extends CrudRepository<T, I>> void deleteById(I id, R repo) {
                repo.deleteById(id);

    }

    @Override
    public DeleteMethod build() {
        return new HardDelete();
    }
}
