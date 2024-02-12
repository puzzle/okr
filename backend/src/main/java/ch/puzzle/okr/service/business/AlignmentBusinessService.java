package ch.puzzle.okr.service.business;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.AlignmentValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlignmentBusinessService {

    private final AlignmentPersistenceService alignmentPersistenceService;
    private final AlignmentValidationService alignmentValidationService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final KeyResultPersistenceService keyResultPersistenceService;

    public AlignmentBusinessService(AlignmentPersistenceService alignmentPersistenceService,
            AlignmentValidationService alignmentValidationService,
            ObjectivePersistenceService objectivePersistenceService,
            KeyResultPersistenceService keyResultPersistenceService) {
        this.alignmentPersistenceService = alignmentPersistenceService;
        this.alignmentValidationService = alignmentValidationService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.keyResultPersistenceService = keyResultPersistenceService;
    }

    public String getTargetIdByAlignedObjectiveId(Long alignedObjectiveId) {
        alignmentValidationService.validateOnGet(alignedObjectiveId);
        Alignment alignment = alignmentPersistenceService.findByAlignedObjectiveId(alignedObjectiveId);
        if (alignment instanceof KeyResultAlignment keyResultAlignment) {
            return "K" + keyResultAlignment.getAlignmentTarget().getId();
        } else if (alignment instanceof ObjectiveAlignment objectiveAlignment) {
            return "O" + objectiveAlignment.getAlignmentTarget().getId();
        } else {
            return null;
        }
    }

    public void createEntity(Objective alignedObjective) {
        Alignment alignment = buildAlignmentModel(alignedObjective);
        alignmentValidationService.validateOnCreate(alignment);
        alignmentPersistenceService.save(alignment);
    }

    public void updateEntity(Long objectiveId, Objective objective) {
        Alignment savedAlignment = alignmentPersistenceService.findByAlignedObjectiveId(objectiveId);

        if (savedAlignment == null) {
            Alignment alignment = buildAlignmentModel(objective);
            alignmentValidationService.validateOnCreate(alignment);
            alignmentPersistenceService.save(alignment);
        } else {
            if (objective.getAlignedEntityId() == null) {
                alignmentValidationService.validateOnDelete(savedAlignment.getId());
                alignmentPersistenceService.deleteById(savedAlignment.getId());
            } else {
                Alignment alignment = buildAlignmentModel(objective);
                alignment.setId(savedAlignment.getId());
                alignmentValidationService.validateOnUpdate(savedAlignment.getId(), alignment);
                if (isAlignmentTypeChange(alignment, savedAlignment)) {
                    alignmentPersistenceService.recreateEntity(savedAlignment.getId(), alignment);
                } else {
                    alignmentPersistenceService.save(alignment);
                }
            }
        }
    }

    private Alignment buildAlignmentModel(Objective alignedObjective) {
        if (alignedObjective.getAlignedEntityId().startsWith("O")) {
            Objective targetObjective = objectivePersistenceService
                    .findById(Long.valueOf(alignedObjective.getAlignedEntityId().replace("O", "")));
            return ObjectiveAlignment.Builder.builder().withAlignedObjective(alignedObjective)
                    .withTargetObjective(targetObjective).build();
        } else if (alignedObjective.getAlignedEntityId().startsWith("K")) {
            KeyResult targetKeyResult = keyResultPersistenceService
                    .findById(Long.valueOf(alignedObjective.getAlignedEntityId().replace("K", "")));

            return KeyResultAlignment.Builder.builder().withAlignedObjective(alignedObjective)
                    .withTargetKeyResult(targetKeyResult).build();
        } else {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NOT_SET,
                    List.of("alignedEntityId", alignedObjective.getAlignedEntityId()));
        }
    }

    private boolean isAlignmentTypeChange(Alignment alignment, Alignment savedAlignment) {
        if (alignment instanceof ObjectiveAlignment && savedAlignment instanceof KeyResultAlignment) {
            return true;
        } else
            return alignment instanceof KeyResultAlignment && savedAlignment instanceof ObjectiveAlignment;
    }

    public void updateKeyResultIdOnIdChange(Long oldId, KeyResult keyResult) {
        List<KeyResultAlignment> alignments = alignmentPersistenceService.findByKeyResultAlignmentId(oldId);
        alignments.forEach(alignment -> {
            alignment.setAlignmentTarget(keyResult);
            alignmentValidationService.validateOnUpdate(alignment.getId(), alignment);
            alignmentPersistenceService.save(alignment);
        });
    }

    public void deleteAlignmentByObjectiveId(Long objectiveId) {
        Alignment alignment = alignmentPersistenceService.findByAlignedObjectiveId(objectiveId);
        alignmentValidationService.validateOnDelete(alignment.getId());
        alignmentPersistenceService.deleteById(alignment.getId());

        List<ObjectiveAlignment> alignmentList = alignmentPersistenceService.findByObjectiveAlignmentId(objectiveId);
        alignmentList.forEach(objectiveAlignment -> {
            alignmentValidationService.validateOnDelete(objectiveAlignment.getId());
            alignmentPersistenceService.deleteById(objectiveAlignment.getId());
        });
    }

    public void deleteAlignmentByKeyResultId(Long keyResultId) {
        List<KeyResultAlignment> alignmentList = alignmentPersistenceService.findByKeyResultAlignmentId(keyResultId);
        alignmentList.forEach(keyResultAlignment -> {
            alignmentValidationService.validateOnDelete(keyResultAlignment.getId());
            alignmentPersistenceService.deleteById(keyResultAlignment.getId());
        });
    }
}
