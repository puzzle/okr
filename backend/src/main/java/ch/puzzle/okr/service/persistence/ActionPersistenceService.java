package ch.puzzle.okr.service.persistence;

import static ch.puzzle.okr.Constants.ACTION;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.repository.ActionRepository;
import ch.puzzle.okr.service.persistence.customCrud.SoftDelete;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class ActionPersistenceService extends PersistenceBase<Action, Long, ActionRepository> {

    protected ActionPersistenceService(ActionRepository repository) {
        super(repository, new SoftDelete<>());
    }

    @Override
    public String getModelName() {
        return ACTION;
    }

    public List<Action> getActionsByKeyResultIdOrderByPriorityAsc(Long keyResultId) {
        return getRepository().getActionsByKeyResultIdAndIsDeletedFalseOrderByPriorityAsc(keyResultId);
    }
}
