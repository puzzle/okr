package ch.puzzle.okr.service.persistence;

import java.util.List;

import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;

import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;

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
        System.out.println(keyResult.toString());
        System.out.println("*".repeat(30));
        // delete entity in order to prevent duplicates in case of changed keyResultType
        deleteById(id);
        System.out.printf("reached delete entity with %d", id);
        return save(keyResult);
    }

    public KeyResult updateEntity(KeyResult keyResult) {
        return save(keyResult);
    }

    public List<KeyResult> getKeyResultsOwnedByUser(long userId) {
        return findAll().stream() //
                        .filter(keyResult -> keyResult.getOwner()
                                                      .getId()
                                                      .equals(userId)) //
                        .toList();
    }
}
