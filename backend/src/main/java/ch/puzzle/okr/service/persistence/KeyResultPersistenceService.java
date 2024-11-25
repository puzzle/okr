package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.KEY_RESULT;

@Service
public class KeyResultPersistenceService extends PersistenceBase<KeyResult, Long, KeyResultRepository> {
    private static final Logger logger = LoggerFactory.getLogger(KeyResultPersistenceService.class);

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
        logger.debug(keyResult.toString());
        logger.debug("*".repeat(30));
        // delete entity in order to prevent duplicates in case of changed keyResultType
        deleteById(id);
        logger.debug("reached delete entity with {}", id);
        return save(keyResult);
    }

    public KeyResult updateEntity(KeyResult keyResult) {
        return save(keyResult);
    }

    public List<KeyResult> getKeyResultsOwnedByUser(long userId) {
        return findAll().stream() //
                .filter(keyResult -> keyResult.getOwner().getId().equals(userId)) //
                .toList();
    }
}
