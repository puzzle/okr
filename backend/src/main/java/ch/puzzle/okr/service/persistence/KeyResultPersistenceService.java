package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.keyResult.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class KeyResultPersistenceService extends PersistenceBase<KeyResult, Long> {

    protected KeyResultPersistenceService(KeyResultRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Objective";
    }

    public List<KeyResult> getKeyResultsByObjective(Objective objective) {
        return ((KeyResultRepository) this.repository).findByObjectiveId(objective.getId());
    }
}
