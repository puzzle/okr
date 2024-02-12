package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.repository.AlignmentRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.ALIGNMENT;

@Service
public class AlignmentPersistenceService extends PersistenceBase<Alignment, Long, AlignmentRepository> {

    protected AlignmentPersistenceService(AlignmentRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return ALIGNMENT;
    }

    @Transactional
    public Alignment recreateEntity(Long id, Alignment alignment) {
        System.out.println(alignment.toString());
        System.out.println("*".repeat(30));
        // delete entity in order to prevent duplicates in case of changed keyResultType
        deleteById(id);
        System.out.printf("reached delete entity with %d", id);
        return save(alignment);
    }

    public Alignment findByAlignedObjectiveId(Long alignedObjectiveId) {
        return getRepository().findByAlignedObjectiveId(alignedObjectiveId);
    }

    public List<KeyResultAlignment> findByKeyResultAlignmentId(Long keyResultId) {
        return getRepository().findByKeyResultAlignmentId(keyResultId);
    }

    public List<ObjectiveAlignment> findByObjectiveAlignmentId(Long objectiveId) {
        return getRepository().findByObjectiveAlignmentId(objectiveId);
    }
}
