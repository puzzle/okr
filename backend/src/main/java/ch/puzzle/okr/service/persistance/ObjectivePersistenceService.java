package ch.puzzle.okr.service.persistance;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.ObjectiveRepository;

public class ObjectivePersistenceService extends PersistenceBase<Objective, Long> {
    protected ObjectivePersistenceService(ObjectiveRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Objective";
    }
}
