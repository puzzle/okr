package ch.puzzle.okr.service.business;

import ch.puzzle.okr.dto.AlignmentDto;
import ch.puzzle.okr.dto.AlignmentObjectDto;
import ch.puzzle.okr.dto.alignment.AlignedEntityDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;

@Service
public class ObjectiveBusinessService implements BusinessServiceInterface<Long, Objective> {
    private final ObjectivePersistenceService objectivePersistenceService;
    private final ObjectiveValidationService validator;
    private final KeyResultBusinessService keyResultBusinessService;
    private final CompletedBusinessService completedBusinessService;
    private final AlignmentBusinessService alignmentBusinessService;

    private static final Logger logger = LoggerFactory.getLogger(ObjectiveBusinessService.class);

    public ObjectiveBusinessService(@Lazy KeyResultBusinessService keyResultBusinessService,
            ObjectiveValidationService validator, ObjectivePersistenceService objectivePersistenceService,
            CompletedBusinessService completedBusinessService, AlignmentBusinessService alignmentBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.validator = validator;
        this.objectivePersistenceService = objectivePersistenceService;
        this.completedBusinessService = completedBusinessService;
        this.alignmentBusinessService = alignmentBusinessService;
    }

    public Objective getEntityById(Long id) {
        validator.validateOnGet(id);
        Objective objective = objectivePersistenceService.findById(id);

        AlignedEntityDto alignedEntity = alignmentBusinessService.getTargetIdByAlignedObjectiveId(objective.getId());
        objective.setAlignedEntity(alignedEntity);
        return objective;
    }

    public List<AlignmentDto> getAlignmentPossibilities(Long quarterId) {
        validator.validateOnGet(quarterId);

        List<Objective> objectivesByQuarter = objectivePersistenceService.findObjectiveByQuarterId(quarterId);
        List<Team> teamList = getTeamsFromObjectives(objectivesByQuarter);

        return createAlignmentDtoForEveryTeam(teamList, objectivesByQuarter);
    }

    private List<Team> getTeamsFromObjectives(List<Objective> objectiveList) {
        return objectiveList.stream() //
                .map(Objective::getTeam) //
                .distinct() //
                .sorted(Comparator.comparing(Team::getName)) //
                .toList();
    }

    private List<AlignmentDto> createAlignmentDtoForEveryTeam(List<Team> teamList,
            List<Objective> objectivesByQuarter) {
        List<AlignmentDto> alignmentDtoList = new ArrayList<>();

        teamList.forEach(team -> {
            List<Objective> filteredObjectiveList = objectivesByQuarter.stream()
                    .filter(objective -> objective.getTeam().equals(team))
                    .sorted(Comparator.comparing(Objective::getTitle)).toList();

            List<AlignmentObjectDto> alignmentObjectDtoList = generateAlignmentObjects(filteredObjectiveList);
            AlignmentDto alignmentDto = new AlignmentDto(team.getId(), team.getName(), alignmentObjectDtoList);
            alignmentDtoList.add(alignmentDto);
        });

        return alignmentDtoList;
    }

    private List<AlignmentObjectDto> generateAlignmentObjects(List<Objective> filteredObjectiveList) {
        List<AlignmentObjectDto> alignmentObjectDtoList = new ArrayList<>();
        filteredObjectiveList.forEach(objective -> {
            AlignmentObjectDto objectiveDto = new AlignmentObjectDto(objective.getId(), "O - " + objective.getTitle(),
                    "objective");
            alignmentObjectDtoList.add(objectiveDto);

            List<KeyResult> keyResultList = keyResultBusinessService.getAllKeyResultsByObjective(objective.getId())
                    .stream().sorted(Comparator.comparing(KeyResult::getTitle)).toList();

            keyResultList.forEach(keyResult -> {
                AlignmentObjectDto keyResultDto = new AlignmentObjectDto(keyResult.getId(),
                        "KR - " + keyResult.getTitle(), "keyResult");
                alignmentObjectDtoList.add(keyResultDto);
            });
        });
        return alignmentObjectDtoList;
    }

    public List<Objective> getEntitiesByTeamId(Long id) {
        validator.validateOnGet(id);

        List<Objective> objectiveList = objectivePersistenceService.findObjectiveByTeamId(id);
        objectiveList.forEach(objective -> {
            AlignedEntityDto alignedEntity = alignmentBusinessService
                    .getTargetIdByAlignedObjectiveId(objective.getId());
            objective.setAlignedEntity(alignedEntity);
        });

        return objectiveList;
    }

    @Transactional
    public Objective updateEntity(Long id, Objective objective, AuthorizationUser authorizationUser) {
        Objective savedObjective = objectivePersistenceService.findById(id);
        Objective updatedObjective = updateObjectiveWithSavedAttrs(objective, savedObjective, authorizationUser);

        validator.validateOnUpdate(id, updatedObjective);
        savedObjective = objectivePersistenceService.save(updatedObjective);
        handleAlignedEntity(id, savedObjective, updatedObjective);
        return savedObjective;
    }

    private void handleAlignedEntity(Long id, Objective savedObjective, Objective updatedObjective) {
        AlignedEntityDto alignedEntity = alignmentBusinessService.getTargetIdByAlignedObjectiveId(savedObjective.getId());
        if ((updatedObjective.getAlignedEntity() != null)
                || updatedObjective.getAlignedEntity() == null && alignedEntity != null) {
            savedObjective.setAlignedEntity(updatedObjective.getAlignedEntity());
            alignmentBusinessService.updateEntity(id, savedObjective);
        }
    }

    private Objective updateObjectiveWithSavedAttrs(Objective objective, Objective savedObjective,
            AuthorizationUser authorizationUser) {
        objective.setCreatedBy(savedObjective.getCreatedBy());
        objective.setCreatedOn(savedObjective.getCreatedOn());
        objective.setModifiedBy(authorizationUser.user());
        objective.setModifiedOn(LocalDateTime.now());
        String not = " ";
        if (isImUsed(objective, savedObjective)) {
            objective.setQuarter(savedObjective.getQuarter());
            not = " NOT ";
        }
        logger.debug("quarter has changed and is{}changeable, {}", not, objective);
        return objective;
    }

    public boolean isImUsed(Objective objective) {
        Objective savedObjective = objectivePersistenceService.findById(objective.getId());
        return isImUsed(objective, savedObjective);
    }

    private boolean isImUsed(Objective objective, Objective savedObjective) {
        return (hasQuarterChanged(objective, savedObjective) && hasAlreadyCheckIns(savedObjective));
    }

    private boolean hasAlreadyCheckIns(Objective savedObjective) {
        return keyResultBusinessService.getAllKeyResultsByObjective(savedObjective.getId()).stream()
                .anyMatch(kr -> keyResultBusinessService.hasKeyResultAnyCheckIns(kr.getId()));
    }

    private static boolean hasQuarterChanged(Objective objective, Objective savedObjective) {
        return !Objects.equals(objective.getQuarter(), savedObjective.getQuarter());
    }

    @Transactional
    public Objective createEntity(Objective objective, AuthorizationUser authorizationUser) {
        objective.setCreatedBy(authorizationUser.user());
        objective.setCreatedOn(LocalDateTime.now());
        validator.validateOnCreate(objective);
        Objective savedObjective = objectivePersistenceService.save(objective);
        if (objective.getAlignedEntity() != null) {
            alignmentBusinessService.createEntity(savedObjective);
        }
        return savedObjective;
    }

    @Transactional
    public Objective duplicateObjective(Long id, Objective objective, AuthorizationUser authorizationUser) {
        Objective duplicatedObjective = createEntity(objective, authorizationUser);
        List<KeyResult> keyResultsOfDuplicatedObjective = keyResultBusinessService.getAllKeyResultsByObjective(id);
        for (KeyResult keyResult : keyResultsOfDuplicatedObjective) {
            createKeyResult(keyResult, duplicatedObjective, authorizationUser);
        }
        return duplicatedObjective;
    }

    private void createKeyResult(KeyResult keyResult, Objective objective, AuthorizationUser authorizationUser) {
        if (keyResult.getKeyResultType().equals(KEY_RESULT_TYPE_METRIC)) {
            createMetricKeyResult(keyResult, objective, authorizationUser);
        } else if (keyResult.getKeyResultType().equals(KEY_RESULT_TYPE_ORDINAL)) {
            createOrdinalKeyResult(keyResult, objective, authorizationUser);
        }
    }

    private void createMetricKeyResult(KeyResult keyResult, Objective objective, AuthorizationUser authorizationUser) {
        KeyResult keyResultMetric = KeyResultMetric.Builder.builder().withObjective(objective)
                .withTitle(keyResult.getTitle()).withDescription(keyResult.getDescription())
                .withOwner(keyResult.getOwner()).withUnit(((KeyResultMetric) keyResult).getUnit()).withBaseline(0D)
                .withStretchGoal(1D).build();
        keyResultBusinessService.createEntity(keyResultMetric, authorizationUser);
    }

    private void createOrdinalKeyResult(KeyResult keyResult, Objective objective, AuthorizationUser authorizationUser) {
        KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withObjective(objective)
                .withTitle(keyResult.getTitle()).withDescription(keyResult.getDescription())
                .withOwner(keyResult.getOwner()).withCommitZone("-").withTargetZone("-").withStretchZone("-").build();
        keyResultBusinessService.createEntity(keyResultOrdinal, authorizationUser);
    }

    @Transactional
    public void deleteEntityById(Long id) {
        validator.validateOnDelete(id);
        completedBusinessService.deleteCompletedByObjectiveId(id);
        keyResultBusinessService.getAllKeyResultsByObjective(id)
                .forEach(keyResult -> keyResultBusinessService.deleteEntityById(keyResult.getId()));
        alignmentBusinessService.deleteAlignmentByObjectiveId(id);
        objectivePersistenceService.deleteById(id);
    }
}
