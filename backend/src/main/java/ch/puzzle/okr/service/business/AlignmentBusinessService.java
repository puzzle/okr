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

import static ch.puzzle.okr.Constants.OBJECTIVE_LOWERCASE;

@Service
public class AlignmentBusinessService {

    private final AlignmentPersistenceService alignmentPersistenceService;
    private final AlignmentValidationService alignmentValidationService;
    private final ObjectivePersistenceService objectivePersistenceService;
    private final KeyResultPersistenceService keyResultPersistenceService;
    private final AlignmentViewPersistenceService alignmentViewPersistenceService;
    private final QuarterBusinessService quarterBusinessService;

    public AlignmentBusinessService(AlignmentPersistenceService alignmentPersistenceService,
            AlignmentValidationService alignmentValidationService,
            ObjectivePersistenceService objectivePersistenceService,
            KeyResultPersistenceService keyResultPersistenceService,
            AlignmentViewPersistenceService alignmentViewPersistenceService,
            QuarterBusinessService quarterBusinessService) {
        this.alignmentPersistenceService = alignmentPersistenceService;
        this.alignmentValidationService = alignmentValidationService;
        this.objectivePersistenceService = objectivePersistenceService;
        this.keyResultPersistenceService = keyResultPersistenceService;
        this.alignmentViewPersistenceService = alignmentViewPersistenceService;
        this.quarterBusinessService = quarterBusinessService;
    }

    protected record DividedAlignmentViewLists(List<AlignmentView> filterMatchingAlignments,
            List<AlignmentView> nonMatchingAlignments) {
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
        validateOnCreateAndSaveAlignment(alignedObjective);
    }

    private void validateOnCreateAndSaveAlignment(Objective alignedObjective) {
        Alignment alignment = buildAlignmentModel(alignedObjective, 0);
        alignmentValidationService.validateOnCreate(alignment);
        alignmentPersistenceService.save(alignment);
    }

    public void updateEntity(Long objectiveId, Objective objective) {
        Alignment savedAlignment = alignmentPersistenceService.findByAlignedObjectiveId(objectiveId);
        if (savedAlignment == null) {
            validateOnCreateAndSaveAlignment(objective);
        } else {
            if (objective.getAlignedEntity() == null) {
                validateOnDeleteAndDeleteById(savedAlignment.getId());
            } else {
                Alignment alignment = buildAlignmentModel(objective, savedAlignment.getVersion());
                validateOnUpdateAndRecreateOrSaveAlignment(alignment, savedAlignment);
            }
        }
    }

    private void validateOnUpdateAndRecreateOrSaveAlignment(Alignment alignment, Alignment savedAlignment) {
        if (isAlignmentTypeChange(alignment, savedAlignment)) {
            validateOnUpdateAndRecreateAlignment(savedAlignment.getId(), alignment);
        } else {
            validateOnUpdateAndSaveAlignment(savedAlignment.getId(), alignment);
        }
    }

    private void validateOnUpdateAndRecreateAlignment(Long id, Alignment alignment) {
        alignment.setId(id);
        alignmentValidationService.validateOnUpdate(id, alignment);
        alignmentPersistenceService.recreateEntity(id, alignment);
    }

    private void validateOnUpdateAndSaveAlignment(Long id, Alignment alignment) {
        alignment.setId(id);
        alignmentValidationService.validateOnUpdate(id, alignment);
        alignmentPersistenceService.save(alignment);
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
            return KeyResultAlignment.Builder.builder() //
                    .withAlignedObjective(alignedObjective) //
                    .withTargetKeyResult(targetKeyResult) //
                    .withVersion(version).build();
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
        alignmentPersistenceService.findByKeyResultAlignmentId(oldKeyResultId)
                .forEach(alignment -> validateOnUpdateAndSaveAlignment(keyResult, alignment));
    }

    private void validateOnUpdateAndSaveAlignment(KeyResult keyResult, KeyResultAlignment alignment) {
        alignment.setAlignmentTarget(keyResult);
        alignmentValidationService.validateOnUpdate(alignment.getId(), alignment);
        alignmentPersistenceService.save(alignment);
    }

    public void deleteAlignmentByObjectiveId(Long objectiveId) {
        ensureAlignmentIdIsNotNull(objectiveId);
        alignmentPersistenceService.findByObjectiveAlignmentId(objectiveId)
                .forEach(objectiveAlignment -> validateOnDeleteAndDeleteById(objectiveAlignment.getId()));
    }

    private void ensureAlignmentIdIsNotNull(Long objectiveId) {
        Alignment alignment = alignmentPersistenceService.findByAlignedObjectiveId(objectiveId);
        if (alignment != null) {
            validateOnDeleteAndDeleteById(alignment.getId());
        }
    }

    private void validateOnDeleteAndDeleteById(Long id) {
        alignmentValidationService.validateOnDelete(id);
        alignmentPersistenceService.deleteById(id);
    }

    public void deleteAlignmentByKeyResultId(Long keyResultId) {
        alignmentPersistenceService.findByKeyResultAlignmentId(keyResultId)
                .forEach(keyResultAlignment -> validateOnDeleteAndDeleteById(keyResultAlignment.getId()));
    }

    public AlignmentLists getAlignmentListsByFilters(Long quarterFilter, List<Long> teamFilter,
            String objectiveFilter) {
        quarterFilter = quarterFilter(quarterFilter);
        teamFilter = Objects.requireNonNullElse(teamFilter, List.of());
        alignmentValidationService.validateOnAlignmentGet(quarterFilter, teamFilter);

        if (teamFilter.isEmpty()) {
            return new AlignmentLists(List.of(), List.of());
        }

        List<AlignmentView> correctAlignmentViewList = correctAlignmentViewList(quarterFilter, teamFilter,
                objectiveFilter);
        sourceAndTargetListsEqualSameSize(correctAlignmentViewList, quarterFilter, teamFilter, objectiveFilter);
        return generateAlignmentLists(correctAlignmentViewList);
    }

    private Long quarterFilter(Long quarterFilter) {
        if (Objects.isNull(quarterFilter)) {
            return quarterBusinessService.getCurrentQuarter().getId();
        }
        return quarterFilter;
    }

    private List<AlignmentView> correctAlignmentViewList(Long quarterFilter, List<Long> teamFilter,
            String objectiveFilter) {
        List<AlignmentView> alignmentViewListByQuarter = alignmentViewPersistenceService
                .getAlignmentViewListByQuarterId(quarterFilter);

        DividedAlignmentViewLists dividedAlignmentViewLists = filterAndDivideAlignmentViews(alignmentViewListByQuarter,
                teamFilter, objectiveFilter);
        return getAlignmentCounterpart(dividedAlignmentViewLists);
    }

    protected void sourceAndTargetListsEqualSameSize(List<AlignmentView> finalList, Long quarterFilter,
            List<Long> teamFilter, String objectiveFilter) {
        List<AlignmentView> sourceList = finalList.stream() //
                .filter(alignmentView -> Objects.equals(alignmentView.getConnectionRole(), "source")) //
                .toList();

        List<AlignmentView> targetList = finalList.stream() //
                .filter(alignmentView -> Objects.equals(alignmentView.getConnectionRole(), "target")) //
                .toList();

        if (sourceList.size() != targetList.size()) {
            throw new OkrResponseStatusException(HttpStatus.BAD_REQUEST, ErrorKey.ALIGNMENT_DATA_FAIL,
                    List.of("alignmentData", quarterFilter, teamFilter, objectiveFilter));
        }
    }

    protected AlignmentLists generateAlignmentLists(List<AlignmentView> alignmentViewList) {
        List<AlignmentObjectDto> distictObjectDtoList = createDistinctAlignmentObjectDtoList(alignmentViewList);
        List<AlignmentConnectionDto> alignmentConnectionDtoList = createAlignmentConnectionDtoListFromConnections(
                alignmentViewList);

        return new AlignmentLists(distictObjectDtoList, alignmentConnectionDtoList);
    }

    private List<AlignmentObjectDto> createDistinctAlignmentObjectDtoList(List<AlignmentView> alignmentViewList) {
        List<AlignmentObjectDto> alignmentObjectDtoList = new ArrayList<>();
        alignmentViewList.forEach(alignmentView -> alignmentObjectDtoList.add(new AlignmentObjectDto( //
                alignmentView.getId(), //
                alignmentView.getTitle(), //
                alignmentView.getTeamName(), //
                alignmentView.getState(), //
                alignmentView.getObjectType())));

        return alignmentObjectDtoList.stream() //
                .distinct() //
                .toList();
    }

    private List<AlignmentConnectionDto> createAlignmentConnectionDtoListFromConnections(
            List<AlignmentView> alignmentViewList) {
        List<AlignmentConnectionDto> alignmentConnectionDtoList = new ArrayList<>();
        alignmentViewList.forEach(alignmentView -> {
            if (Objects.equals(alignmentView.getConnectionRole(), "source")) {
                if (Objects.equals(alignmentView.getCounterpartType(), OBJECTIVE_LOWERCASE)) {
                    alignmentConnectionDtoList.add(new AlignmentConnectionDto( //
                            alignmentView.getId(), alignmentView.getCounterpartId(), null));
                } else {
                    alignmentConnectionDtoList.add(new AlignmentConnectionDto( //
                            alignmentView.getId(), null, alignmentView.getCounterpartId()));
                }
            }
        });
        return alignmentConnectionDtoList;
    }

    protected List<AlignmentView> getAlignmentCounterpart(DividedAlignmentViewLists alignmentViewLists) {
        List<AlignmentView> nonMatchingAlignments = alignmentViewLists.nonMatchingAlignments();
        List<AlignmentView> filterMatchingAlignments = alignmentViewLists.filterMatchingAlignments();
        List<AlignmentView> correctAlignmentViewList = correctAlignmentViewList(filterMatchingAlignments,
                nonMatchingAlignments);
        return createFinalAlignmentViewList(filterMatchingAlignments, correctAlignmentViewList);
    }

    private List<AlignmentView> correctAlignmentViewList(List<AlignmentView> filterMatchingAlignments,
            List<AlignmentView> nonMatchingAlignments) {
        List<AlignmentView> correctAlignmentViewList = new ArrayList<>();
        filterMatchingAlignments.forEach(alignment -> {
            Optional<AlignmentView> matchingObject = findMatchingAlignmentInList(nonMatchingAlignments, alignment);
            matchingObject.map(correctAlignmentViewList::add);
        });
        return correctAlignmentViewList;
    }

    private Optional<AlignmentView> findMatchingAlignmentInList(List<AlignmentView> alignmentList,
            AlignmentView alignment) {
        return alignmentList.stream().filter(view -> isMatching(alignment, view)).findFirst();
    }

    private boolean isMatching(AlignmentView firstAlignment, AlignmentView secondAlignment) {
        return Objects.equals(secondAlignment.getId(), firstAlignment.getCounterpartId())
                && Objects.equals(secondAlignment.getObjectType(), firstAlignment.getCounterpartType())
                && Objects.equals(secondAlignment.getCounterpartId(), firstAlignment.getId())
                && Objects.equals(secondAlignment.getCounterpartType(), firstAlignment.getObjectType());
    }

    private List<AlignmentView> createFinalAlignmentViewList(List<AlignmentView> filterMatchingAlignments,
            List<AlignmentView> correctAlignmentViewList) {
        List<AlignmentView> finalAlignmentViewList = new ArrayList<>(filterMatchingAlignments);
        if (!correctAlignmentViewList.isEmpty()) {
            finalAlignmentViewList.addAll(correctAlignmentViewList);
        }
        return finalAlignmentViewList;
    }

    protected DividedAlignmentViewLists filterAndDivideAlignmentViews(List<AlignmentView> alignmentViewList,
            List<Long> teamFilter, String objectiveFilter) {
        List<AlignmentView> filterMatchingAlignments = filterAlignmentListByTeamAndObjective(alignmentViewList,
                teamFilter, objectiveFilter);
        List<AlignmentView> nonMatchingAlignments = filterNonMatchingAlignments(alignmentViewList,
                filterMatchingAlignments);

        return new DividedAlignmentViewLists(filterMatchingAlignments, nonMatchingAlignments);
    }

    private List<AlignmentView> filterAlignmentListByTeamAndObjective(List<AlignmentView> alignmentViewList,
            List<Long> teamFilter, String objectiveFilter) {
        List<AlignmentView> filteredList = filterByTeam(alignmentViewList, teamFilter);
        if (StringUtils.isNotBlank(objectiveFilter)) {
            filteredList = filterByObjective(filteredList, objectiveFilter);
        }
        return filteredList;
    }

    private List<AlignmentView> filterByTeam(List<AlignmentView> alignmentViewList, List<Long> teamFilter) {
        return alignmentViewList.stream() //
                .filter(alignmentView -> teamFilter.contains(alignmentView.getTeamId())) //
                .toList();
    }

    private List<AlignmentView> filterByObjective(List<AlignmentView> filteredList, String objectiveFilter) {
        return filteredList.stream() //
                .filter(alignmentView -> isObjectiveAndMatchesFilter(alignmentView, objectiveFilter)) //
                .toList();
    }

    private static boolean isObjectiveAndMatchesFilter(AlignmentView alignmentView, String objectiveFilter) {
        return Objects.equals(alignmentView.getObjectType(), OBJECTIVE_LOWERCASE)
                && alignmentView.getTitle().toLowerCase().contains(objectiveFilter.toLowerCase());
    }

    private List<AlignmentView> filterNonMatchingAlignments(List<AlignmentView> alignmentViewList,
            List<AlignmentView> nonMatchingAlignments) {
        return alignmentViewList.stream() //
                .filter(alignmentView -> !nonMatchingAlignments.contains(alignmentView)) //
                .toList();
    }
}
