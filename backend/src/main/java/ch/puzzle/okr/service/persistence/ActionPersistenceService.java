package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.repository.ActionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.ACTION;

@Service
public class ActionPersistenceService extends PersistenceBase<Action, Long, ActionRepository> {

    protected ActionPersistenceService(ActionRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return ACTION;
    }

    public List<Action> getActionsByKeyResultIdOrderByPriorityAsc(Long keyResultId) {
        return getRepository().getActionsByKeyResultIdOrderByPriorityAsc(keyResultId);
    }

    public void duplicateActionList(KeyResult oldKeyResult, KeyResult newKeyResult) {
        List<Action> actionList = getActionsByKeyResultIdOrderByPriorityAsc(oldKeyResult.getId());
        if (actionList != null) {
            actionList.forEach(action -> {
                action.setKeyResult(newKeyResult);
                action.resetId();
                this.save(action);
            });
        }
    }
}
