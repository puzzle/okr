package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.KEY_RESULT;

import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.service.persistence.customCrud.SoftDelete;
import jakarta.transaction.Transactional;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class KeyResultPersistenceService extends PersistenceBase<KeyResult, Long, KeyResultRepository> {

    protected KeyResultPersistenceService(KeyResultRepository repository) {
        super(repository, new SoftDelete<>());
    }

    @Override
    public String getModelName() {
        return KEY_RESULT;
    }

    public List<KeyResult> getKeyResultsByObjective(Long objectiveId) {
        return getRepository().findByObjectiveIdAndIsDeletedFalse(objectiveId);
    }

    @Transactional
    public KeyResult recreateEntity(Long id, KeyResult keyResult) {
        // delete entity in order to prevent duplicates in case of changed keyResultType
        deleteById(id);
        // deleteById(id, new HardDelete<>());

        // reset id of key result, so it gets saved as a new entity
        keyResult.resetId();
        return save(keyResult);
    }

    public KeyResult updateEntity(KeyResult keyResult) {
        return save(keyResult);
    }

    public List<KeyResult> getKeyResultsOwnedByUser(long userId) {
        return findAll()
                .stream() //
                .filter(keyResult -> keyResult.getOwner().getId().equals(userId)) //
                .toList();
    }
}
