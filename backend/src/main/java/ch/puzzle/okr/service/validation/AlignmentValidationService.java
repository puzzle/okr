package ch.puzzle.okr.service.validation;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.repository.AlignmentRepository;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class AlignmentValidationService
        extends ValidationBase<Alignment, Long, AlignmentRepository, AlignmentPersistenceService> {

    private final AlignmentPersistenceService alignmentPersistenceService;
    private final TeamPersistenceService teamPersistenceService;

    public AlignmentValidationService(AlignmentPersistenceService alignmentPersistenceService,
            TeamPersistenceService teamPersistenceService) {
        super(alignmentPersistenceService);
        this.alignmentPersistenceService = alignmentPersistenceService;
        this.teamPersistenceService = teamPersistenceService;
    }

    @Override
    public void validateOnCreate(Alignment model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNotNull(model.getId());
        throwExceptionWhenAlignedObjectIsNull(model);
        throwExceptionWhenAlignedIdIsSameAsTargetId(model);
        throwExceptionWhenAlignmentIsInSameTeam(model);
        throwExceptionWhenAlignedObjectiveAlreadyExists(model);
        validate(model);
    }

    @Override
    public void validateOnUpdate(Long id, Alignment model) {
        throwExceptionWhenModelIsNull(model);
        throwExceptionWhenIdIsNull(model.getId());
        throwExceptionWhenAlignedObjectIsNull(model);
        throwExceptionWhenAlignedIdIsSameAsTargetId(model);
        throwExceptionWhenAlignmentIsInSameTeam(model);
        validate(model);
    }

    private void throwExceptionWhenAlignedObjectIsNull(Alignment model) {
        if (model.getAlignedObjective() == null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                    List.of("alignedObjectiveId"));
        } else if (model instanceof ObjectiveAlignment objectiveAlignment) {
            if (objectiveAlignment.getAlignmentTarget() == null) {
                throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                        List.of("targetObjectiveId", objectiveAlignment.getAlignedObjective().getId()));
            }
        } else if (model instanceof KeyResultAlignment keyResultAlignment
                && (keyResultAlignment.getAlignmentTarget() == null)) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NULL,
                    List.of("targetKeyResultId", keyResultAlignment.getAlignedObjective().getId()));

        }
    }

    private void throwExceptionWhenAlignmentIsInSameTeam(Alignment model) {
        Team alignedTeam = teamPersistenceService.findById(model.getAlignedObjective().getTeam().getId());
        Team targetTeam = null;

        if (model instanceof ObjectiveAlignment objectiveAlignment) {
            targetTeam = teamPersistenceService.findById(objectiveAlignment.getAlignmentTarget().getTeam().getId());
        } else if (model instanceof KeyResultAlignment keyResultAlignment) {
            targetTeam = teamPersistenceService
                    .findById(keyResultAlignment.getAlignmentTarget().getObjective().getTeam().getId());
        }

        if (alignedTeam.equals(targetTeam)) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.NOT_LINK_IN_SAME_TEAM,
                    List.of("teamId", targetTeam.getId()));
        }
    }

    private void throwExceptionWhenAlignedIdIsSameAsTargetId(Alignment model) {
        if (model instanceof ObjectiveAlignment objectiveAlignment
                && (Objects.equals(objectiveAlignment.getAlignedObjective().getId(),
                        objectiveAlignment.getAlignmentTarget().getId()))) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.NOT_LINK_YOURSELF,
                    List.of("targetObjectiveId", objectiveAlignment.getAlignmentTarget().getId()));

        }
    }

    private void throwExceptionWhenAlignedObjectiveAlreadyExists(Alignment model) {
        if (this.alignmentPersistenceService.findByAlignedObjectiveId(model.getAlignedObjective().getId()) != null) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ALIGNMENT_ALREADY_EXISTS,
                    List.of("alignedObjectiveId", model.getAlignedObjective().getId()));
        }
    }
}
