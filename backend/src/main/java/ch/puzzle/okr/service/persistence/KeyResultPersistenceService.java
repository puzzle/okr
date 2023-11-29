package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

import static ch.puzzle.okr.Constants.KEY_RESULT;

@Service
public class KeyResultPersistenceService extends PersistenceBase<KeyResult, Long, KeyResultRepository> {

    protected KeyResultPersistenceService(KeyResultRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return KEY_RESULT;
    }

    public List<KeyResult> getKeyResultsByObjective(Long objectiveId) {
        return getRepository().findByObjectiveId(objectiveId);
    }

    @Transactional
    public KeyResult recreateEntity(Long id, KeyResult keyResult) {
        // delete entity in order to prevent duplicates in case of changed keyResultType
        deleteById(id);
        return save(keyResult);
    }

    public KeyResult updateEntity(KeyResult keyResult) {
        return save(keyResult);
    }
}
