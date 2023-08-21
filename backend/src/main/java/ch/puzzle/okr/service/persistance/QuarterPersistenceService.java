package ch.puzzle.okr.service.persistance;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.repository.QuarterRepository;

public class QuarterPersistenceService extends PersistenceBase<Quarter, Long> {
    protected QuarterPersistenceService(QuarterRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Quarter";
    }
}
