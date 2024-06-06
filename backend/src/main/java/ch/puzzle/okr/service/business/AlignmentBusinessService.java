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
        Alignment alignment = buildAlignmentModel(alignedObjective, 0);
        alignmentValidationService.validateOnCreate(alignment);
        alignmentPersistenceService.save(alignment);
    }

    public void updateEntity(Long objectiveId, Objective objective) {
        Alignment savedAlignment = alignmentPersistenceService.findByAlignedObjectiveId(objectiveId);

        if (savedAlignment == null) {
            createEntity(objective);
        } else {
            handleExistingAlignment(objective, savedAlignment);
        }
    }

    private void handleExistingAlignment(Objective objective, Alignment savedAlignment) {
        if (objective.getAlignedEntityId() == null) {
            validateAndDeleteAlignmentById(savedAlignment.getId());
        } else {
            validateAndUpdateAlignment(objective, savedAlignment);
        }
    }

    private void validateAndUpdateAlignment(Objective objective, Alignment savedAlignment) {
        Alignment alignment = buildAlignmentModel(objective, savedAlignment.getVersion());

        alignment.setId(savedAlignment.getId());
        alignmentValidationService.validateOnUpdate(savedAlignment.getId(), alignment);
        updateAlignment(savedAlignment, alignment);
    }

    private void updateAlignment(Alignment savedAlignment, Alignment alignment) {
        if (isAlignmentTypeChange(alignment, savedAlignment)) {
            alignmentPersistenceService.recreateEntity(savedAlignment.getId(), alignment);
        } else {
            alignmentPersistenceService.save(alignment);
        }
    }

    public Alignment buildAlignmentModel(Objective alignedObjective, int version) {
        if (alignedObjective.getAlignedEntityId().startsWith("O")) {
            Long entityId = Long.valueOf(alignedObjective.getAlignedEntityId().replace("O", ""));

            Objective targetObjective = objectivePersistenceService.findById(entityId);
            return ObjectiveAlignment.Builder.builder() //
                    .withAlignedObjective(alignedObjective) //
                    .withTargetObjective(targetObjective) //
                    .withVersion(version).build();
        } else if (alignedObjective.getAlignedEntityId().startsWith("K")) {
            Long entityId = Long.valueOf(alignedObjective.getAlignedEntityId().replace("K", ""));

            KeyResult targetKeyResult = keyResultPersistenceService.findById(entityId);
            return KeyResultAlignment.Builder.builder().withAlignedObjective(alignedObjective)
                    .withTargetKeyResult(targetKeyResult).withVersion(version).build();
        } else {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NOT_SET,
                    List.of("alignedEntityId", alignedObjective.getAlignedEntityId()));
        }
    }

    public boolean isAlignmentTypeChange(Alignment alignment, Alignment savedAlignment) {
        return (alignment instanceof ObjectiveAlignment && savedAlignment instanceof KeyResultAlignment)
                || (alignment instanceof KeyResultAlignment && savedAlignment instanceof ObjectiveAlignment);
    }

    public void updateKeyResultIdOnIdChange(Long oldKeyResultId, KeyResult keyResult) {
        List<KeyResultAlignment> keyResultAlignmentList = alignmentPersistenceService
                .findByKeyResultAlignmentId(oldKeyResultId);
        keyResultAlignmentList.forEach(alignment -> {
            alignment.setAlignmentTarget(keyResult);
            alignmentValidationService.validateOnUpdate(alignment.getId(), alignment);
            alignmentPersistenceService.save(alignment);
        });
    }

    public void deleteAlignmentByObjectiveId(Long objectiveId) {
        Alignment alignment = alignmentPersistenceService.findByAlignedObjectiveId(objectiveId);
        if (alignment != null) {
            validateAndDeleteAlignmentById(alignment.getId());
        }
        List<ObjectiveAlignment> objectiveAlignmentList = alignmentPersistenceService
                .findByObjectiveAlignmentId(objectiveId);
        objectiveAlignmentList
                .forEach(objectiveAlignment -> validateAndDeleteAlignmentById(objectiveAlignment.getId()));
    }

    public void deleteAlignmentByKeyResultId(Long keyResultId) {
        List<KeyResultAlignment> keyResultAlignmentList = alignmentPersistenceService
                .findByKeyResultAlignmentId(keyResultId);
        keyResultAlignmentList
                .forEach(keyResultAlignment -> validateAndDeleteAlignmentById(keyResultAlignment.getId()));
    }

    private void validateAndDeleteAlignmentById(Long alignmentId) {
        alignmentValidationService.validateOnDelete(alignmentId);
        alignmentPersistenceService.deleteById(alignmentId);
    }
}
