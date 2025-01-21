package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.repository.ActionRepository;
import ch.puzzle.okr.repository.UnitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static ch.puzzle.okr.Constants.ACTION;
import static ch.puzzle.okr.Constants.UNIT;

@Service
public class UnitPersistenceService extends PersistenceBase<Unit, Long, UnitRepository> {

    protected UnitPersistenceService(UnitRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return UNIT;
    }

    public Optional<Unit> findUnitByUnitName(String unitName) {
        return getRepository().findUnitByUnitName(unitName);
    }
}
