package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import org.hibernate.Session;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class KeyResultPersistenceService extends PersistenceBase<KeyResult, Long> {

    @PersistenceContext
    private EntityManager entityManager;

    @PersistenceContext
    private Session session;

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

    public KeyResult updateKeyResult(KeyResult savedKeyResult, KeyResult keyResult) {
        session.evict(savedKeyResult);
        session.update(keyResult);
        return ((KeyResultRepository) this.repository).findById(keyResult.getId()).get();
    }
}
