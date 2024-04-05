package ch.puzzle.okr.service.business;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.dto.alignment.AlignmentConnectionDto;
import ch.puzzle.okr.dto.alignment.AlignmentLists;
import ch.puzzle.okr.dto.alignment.AlignmentObjectDto;
import ch.puzzle.okr.dto.alignment.AlignedEntityDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.AlignmentView;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.alignment.ObjectiveAlignment;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import ch.puzzle.okr.service.persistence.AlignmentViewPersistenceService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.AlignmentValidationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class AlignmentBusinessService {

    private final AlignmentPersistenceService alignmentPersistenceService;
    private final AlignmentValidationService alignmentValidationService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final KeyResultPersistenceService keyResultPersistenceService;
    private final AlignmentViewPersistenceService alignmentViewPersistenceService;

    public AlignmentBusinessService(AlignmentPersistenceService alignmentPersistenceService,
            AlignmentValidationService alignmentValidationService,
            ObjectivePersistenceService objectivePersistenceService,
            KeyResultPersistenceService keyResultPersistenceService,
            AlignmentViewPersistenceService alignmentViewPersistenceService) {
        this.alignmentPersistenceService = alignmentPersistenceService;
        this.alignmentValidationService = alignmentValidationService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.keyResultPersistenceService = keyResultPersistenceService;
        this.alignmentViewPersistenceService = alignmentViewPersistenceService;
    }

    protected record DividedAlignmentViewLists(List<AlignmentView> correctAlignments,
            List<AlignmentView> wrongAlignments) {
    }

    public AlignedEntityDto getTargetIdByAlignedObjectiveId(Long alignedObjectiveId) {
        alignmentValidationService.validateOnGet(alignedObjectiveId);
        Alignment alignment = alignmentPersistenceService.findByAlignedObjectiveId(alignedObjectiveId);
        if (alignment instanceof KeyResultAlignment keyResultAlignment) {
            return new AlignedEntityDto(keyResultAlignment.getAlignmentTarget().getId(), "keyResult");
        } else if (alignment instanceof ObjectiveAlignment objectiveAlignment) {
            return new AlignedEntityDto(objectiveAlignment.getAlignmentTarget().getId(), "objective");
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
        if (objective.getAlignedEntity() == null) {
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
        if (alignedObjective.getAlignedEntity().type().equals("objective")) {
            Long entityId = alignedObjective.getAlignedEntity().id();

            Objective targetObjective = objectivePersistenceService.findById(entityId);
            return ObjectiveAlignment.Builder.builder() //
                    .withAlignedObjective(alignedObjective) //
                    .withTargetObjective(targetObjective) //
                    .withVersion(version).build();
        } else if (alignedObjective.getAlignedEntity().type().equals("keyResult")) {
            Long entityId = alignedObjective.getAlignedEntity().id();

            KeyResult targetKeyResult = keyResultPersistenceService.findById(entityId);
            return KeyResultAlignment.Builder.builder().withAlignedObjective(alignedObjective)
                    .withTargetKeyResult(targetKeyResult).withVersion(version).build();
        } else {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ATTRIBUTE_NOT_SET,
                    List.of("alignedEntity", alignedObjective.getAlignedEntity()));
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

    public AlignmentLists getAlignmentsByFilters(Long quarterFilter, List<Long> teamFilter, String objectiveFilter) {
        alignmentValidationService.validateOnAlignmentGet(quarterFilter, teamFilter);

        List<AlignmentView> alignmentViewList = alignmentViewPersistenceService
                .getAlignmentViewListByQuarterId(quarterFilter);
        DividedAlignmentViewLists dividedAlignmentViewLists = filterAlignmentViews(alignmentViewList, teamFilter,
                objectiveFilter);

        List<AlignmentView> finalList = getAlignmentCounterpart(dividedAlignmentViewLists);

        return generateAlignmentLists(finalList);
    }

    protected AlignmentLists generateAlignmentLists(List<AlignmentView> alignmentViewList) {
        List<AlignmentConnectionDto> alignmentConnectionDtoList = new ArrayList<>();
        List<AlignmentObjectDto> alignmentObjectDtoList = new ArrayList<>();

        // Create ConnectionDtoList for every connection
        alignmentViewList.forEach(alignmentView -> {
            if (Objects.equals(alignmentView.getConnectionItem(), "source")) {
                if (Objects.equals(alignmentView.getRefType(), "objective")) {
                    alignmentConnectionDtoList
                            .add(new AlignmentConnectionDto(alignmentView.getId(), alignmentView.getRefId(), null));
                } else {
                    alignmentConnectionDtoList
                            .add(new AlignmentConnectionDto(alignmentView.getId(), null, alignmentView.getRefId()));
                }
            }
        });

        alignmentViewList.forEach(alignmentView -> alignmentObjectDtoList
                .add(new AlignmentObjectDto(alignmentView.getId(), alignmentView.getTitle(),
                        alignmentView.getTeamName(), alignmentView.getState(), alignmentView.getObjectType())));

        // Remove duplicated items
        List<AlignmentObjectDto> alignmentObjectDtos = alignmentObjectDtoList.stream().distinct().toList();

        return new AlignmentLists(alignmentObjectDtos, alignmentConnectionDtoList);
    }

    protected List<AlignmentView> getAlignmentCounterpart(DividedAlignmentViewLists alignmentViewLists) {
        List<AlignmentView> correctAlignments = alignmentViewLists.correctAlignments();
        List<AlignmentView> wrongAlignments = alignmentViewLists.wrongAlignments();
        List<AlignmentView> targetAlignmentList = new ArrayList<>();

        // If counterpart of the correct Alignment is in wrongAlignmentList, take it back
        correctAlignments.forEach(alignment -> {
            Optional<AlignmentView> matchingObject = wrongAlignments.stream()
                    .filter(view -> Objects.equals(view.getId(), alignment.getRefId())
                            && Objects.equals(view.getObjectType(), alignment.getRefType())
                            && Objects.equals(view.getRefId(), alignment.getId())
                            && !Objects.equals(view.getConnectionItem(), alignment.getConnectionItem()))
                    .findFirst();

            if (matchingObject.isPresent()) {
                AlignmentView alignmentView = matchingObject.get();
                targetAlignmentList.add(alignmentView);
            }
        });

        // Create a new list because correctAlignments has a fixed length and targetAlignmentList can't be added
        List<AlignmentView> finalList = new ArrayList<>(correctAlignments);
        if (!targetAlignmentList.isEmpty()) {
            finalList.addAll(targetAlignmentList);
        }
        return finalList;
    }

    protected DividedAlignmentViewLists filterAlignmentViews(List<AlignmentView> alignmentViewList,
            List<Long> teamFilter, String objectiveFilter) {
        List<AlignmentView> filteredList = alignmentViewList.stream()
                .filter(alignmentView -> teamFilter.contains(alignmentView.getTeamId())).toList();

        boolean isObjectiveFilterDefined = StringUtils.isNotBlank(objectiveFilter);
        if (isObjectiveFilterDefined) {
            filteredList = filteredList.stream()
                    .filter(alignmentView -> Objects.equals(alignmentView.getObjectType(), "objective")
                            && alignmentView.getTitle().toLowerCase().contains(objectiveFilter.toLowerCase()))
                    .toList();
        }
        List<AlignmentView> correctAlignments = filteredList;
        List<AlignmentView> wrongAlignments = alignmentViewList.stream()
                .filter(alignmentView -> !correctAlignments.contains(alignmentView)).toList();
        return new DividedAlignmentViewLists(correctAlignments, wrongAlignments);
    }
}
