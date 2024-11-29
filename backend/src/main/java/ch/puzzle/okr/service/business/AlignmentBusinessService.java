package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import ch.puzzle.okr.service.validation.AlignmentValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlignmentBusinessService {
    private final AlignmentPersistenceService alignmentPersistenceService;
    private final AlignmentValidationService validation;

    public AlignmentBusinessService(AlignmentPersistenceService alignmentPersistenceService,
            AlignmentValidationService validation) {
        this.alignmentPersistenceService = alignmentPersistenceService;
        this.validation = validation;
    }

    public Alignment updateEntity(Long id, Alignment entity) {
        validation.validateOnUpdate(id, entity);
        return alignmentPersistenceService.save(entity);
    }

    public void updateKeyResultId(Long oldId, KeyResult newKeyResult) {
        List<KeyResultAlignment> alignments = alignmentPersistenceService.findByKeyResultAlignmentId(oldId);

        alignments.forEach(a -> {
            a.setAlignmentTarget(newKeyResult);
            this.updateEntity(a.getId(), a);
        });

    }
}
