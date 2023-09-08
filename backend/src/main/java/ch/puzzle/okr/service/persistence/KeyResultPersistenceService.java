package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class KeyResultPersistenceService extends PersistenceBase<KeyResult, Long> {

    protected KeyResultPersistenceService(KeyResultRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "KeyResult";
    }

    public List<KeyResult> getKeyResultsByObjective(Objective objective) {
        return ((KeyResultRepository) this.repository).findByObjectiveId(objective.getId());
    }

    @Transactional
    public KeyResult updateEntity(Long id, KeyResult keyResult) {
        KeyResult savedKeyResult = findById(id);
        keyResult.setCreatedBy(savedKeyResult.getCreatedBy());
        keyResult.setCreatedOn(savedKeyResult.getCreatedOn());
        keyResult.setObjective(savedKeyResult.getObjective());

        // Delete Entity in order to prevent duplicates
        deleteById(id);
        return save(keyResult);
    }

    public KeyResult updateAbstractEntity(Long id, KeyResult keyResult) {
        KeyResult savedKeyResult = findById(id);
        savedKeyResult.setTitle(keyResult.getTitle());
        savedKeyResult.setDescription(keyResult.getDescription());
        savedKeyResult.setOwner(keyResult.getOwner());
        savedKeyResult.setModifiedOn(keyResult.getModifiedOn());

        return save(savedKeyResult);
    }
}
