package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.repository.AlignmentRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

import static ch.puzzle.okr.Constants.ALIGNMENT;

@Service
public class AlignmentPersistenceService extends PersistenceBase<Alignment, Long, AlignmentRepository> {
    private static final Logger logger = LoggerFactory.getLogger(AlignmentPersistenceService.class);

    protected AlignmentPersistenceService(AlignmentRepository repository) {
        super(repository);
    }

    @Override
    public String getModelName() {
        return ALIGNMENT;
    }

    @Transactional
    public Alignment recreateEntity(Long id, Alignment alignment) {
        logger.debug("Delete and create new Alignment in order to prevent duplicates in case of changed Type");
        logger.debug("{}", alignment);
        // delete entity in order to prevent duplicates in case of changed keyResultType
        deleteById(id);
        logger.debug("Reached delete entity with id {}", id);
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
