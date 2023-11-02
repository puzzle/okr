package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.repository.ActionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ActionPersistenceService extends PersistenceBase<Action, Long, ActionRepository> {

    protected ActionPersistenceService(ActionRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Action";
    }

    public List<Action> getActionsByKeyResultIdOrderByPriorityAsc(Long keyResultId) {
        return getRepository().getActionsByKeyResultIdOrderByPriorityAsc(keyResultId);
    }
}
