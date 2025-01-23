package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.UNIT;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.repository.UnitRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

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

    public List<Unit> findUnitsByUser(Long userId) {
        return getRepository().findAllByCreatedById(userId);
    }
}
