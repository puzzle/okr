package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.repository.CompletedRepository;
import org.springframework.stereotype.Service;

@Service
public class CompletedPersistenceService extends PersistenceBase<Completed, Long, CompletedRepository> {

    protected CompletedPersistenceService(CompletedRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Completed";
    }

    public Completed getCompletedByObjectiveId(Long objectiveId) {
        return getRepository().findByObjectiveId(objectiveId);
    }
}
