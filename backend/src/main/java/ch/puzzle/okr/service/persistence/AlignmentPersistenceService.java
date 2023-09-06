package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Alignment;
import ch.puzzle.okr.models.KeyResultAlignment;
import ch.puzzle.okr.models.ObjectiveAlignment;
import ch.puzzle.okr.repository.AlignmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlignmentPersistenceService extends PersistenceBase<Alignment, Long> {

    protected AlignmentPersistenceService(AlignmentRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return "Alignment";
    }

    public List<Alignment> findByAlignedObjectiveId(Long alignedObjectiveId) {
        return getAlignmentRepository().findByAlignedObjectiveId(alignedObjectiveId);
    }

    public List<KeyResultAlignment> findByKeyResultAlignmentId(Long keyResultId) {
        return getAlignmentRepository().findByKeyResultAlignmentId(keyResultId);
    }

    public List<ObjectiveAlignment> findByObjectiveAlignmentId(Long objectiveId) {
        return getAlignmentRepository().findByObjectiveAlignmentId(objectiveId);
    }

    private AlignmentRepository getAlignmentRepository() {
        return (AlignmentRepository) repository;
    }
}
