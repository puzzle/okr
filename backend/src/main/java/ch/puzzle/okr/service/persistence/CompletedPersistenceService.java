package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.repository.CompletedRepository;
import org.springframework.stereotype.Service;

import static ch.puzzle.okr.Constants.COMPLETED;

@Service
public class CompletedPersistenceService extends PersistenceBase<Completed, Long, CompletedRepository> {

    protected CompletedPersistenceService(CompletedRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return COMPLETED;
    }

    public Completed getCompletedByObjectiveId(Long objectiveId) {
        return getRepository().findByObjectiveId(objectiveId);
    }
}
