package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.repository.AlignmentRepository;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AlignmentValidationService
        extends ValidationBase<Alignment, Long, AlignmentRepository, AlignmentPersistenceService> {

    private final AlignmentPersistenceService alignmentPersistenceService;

    public AlignmentValidationService(AlignmentPersistenceService alignmentPersistenceService) {
        super(alignmentPersistenceService);
        this.alignmentPersistenceService = alignmentPersistenceService;
    }

    @Override
    public void validateOnCreate(Alignment model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        throwExceptionWhenAlignedObjectIsNull(model);
        throwExceptionWhenAlignedIdIsSameAsTargetId(model);
        throwExceptionWhenAlignedObjectiveAlreadyExists(model);
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Alignment model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenAlignedObjectIsNull(model);
        throwExceptionWhenAlignedIdIsSameAsTargetId(model);
        validate(model);
    }

    private static void throwExceptionWhenAlignedObjectIsNull(Alignment model) {
        if (model.getAlignedObjective() == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                    List.of("alignedObjectiveId", model.getAlignedObjective().getId()));
        } else if (model instanceof ObjectiveAlignment objectiveAlignment) {
            if (objectiveAlignment.getAlignmentTarget() == null) {
                throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                        List.of("targetObjectiveId", objectiveAlignment.getAlignmentTarget().getId()));
            }
        } else if (model instanceof KeyResultAlignment keyResultAlignment) {
            if (keyResultAlignment.getAlignmentTarget() == null) {
                throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                        List.of("targetKeyResultId", keyResultAlignment.getAlignmentTarget().getId()));
            }
        }
    }

    private static void throwExceptionWhenAlignedIdIsSameAsTargetId(Alignment model) {
        if (model instanceof ObjectiveAlignment objectiveAlignment) {
            if (Objects.equals(objectiveAlignment.getAlignedObjective().getId(),
                    objectiveAlignment.getAlignmentTarget().getId())) {
                throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.NOT_LINK_YOURSELF,
                        List.of("targetObjectiveId", objectiveAlignment.getAlignmentTarget().getId()));
            }
        }
    }

    private void throwExceptionWhenAlignedObjectiveAlreadyExists(Alignment model) {
        if (this.alignmentPersistenceService.findByAlignedObjectiveId(model.getAlignedObjective().getId()) != null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ALIGNMENT_ALREADY_EXISTS,
                    List.of("alignedObjectiveId", model.getAlignedObjective().getId()));
        }
    }
}
