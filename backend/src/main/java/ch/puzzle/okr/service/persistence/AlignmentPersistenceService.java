package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.repository.AlignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlignmentPersistenceService extends PersistenceBase<Alignment, Long, AlignmentRepository> {

    protected AlignmentPersistenceService(AlignmentRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Alignment";
    }

    public List<Alignment> findByAlignedObjectiveId(Long alignedObjectiveId) {
        return getRepository().findByAlignedObjectiveId(alignedObjectiveId);
    }

    public List<KeyResultAlignment> findByKeyResultAlignmentId(Long keyResultId) {
        return getRepository().findByKeyResultAlignmentId(keyResultId);
    }

    public List<ObjectiveAlignment> findByObjectiveAlignmentId(Long objectiveId) {
        return getRepository().findByObjectiveAlignmentId(objectiveId);
    }
}
