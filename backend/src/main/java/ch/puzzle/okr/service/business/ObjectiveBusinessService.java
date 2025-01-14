package ch.puzzle.okr.service.business;

import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_METRIC;
import static ch.puzzle.okr.Constants.KEY_RESULT_TYPE_ORDINAL;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveBusinessService implements BusinessServiceInterface<Long, Objective> {
    private static final Logger logger = LoggerFactory.getLogger(ObjectiveBusinessService.class);
    private final ObjectivePersistenceService objectivePersistenceService;
    private final ObjectiveValidationService validator;
    private final KeyResultBusinessService keyResultBusinessService;
    private final CompletedBusinessService completedBusinessService;

    public ObjectiveBusinessService(@Lazy KeyResultBusinessService keyResultBusinessService,
                                    ObjectiveValidationService validator,
                                    ObjectivePersistenceService objectivePersistenceService,
                                    CompletedBusinessService completedBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.validator = validator;
        this.objectivePersistenceService = objectivePersistenceService;
        this.completedBusinessService = completedBusinessService;
    }

    private static boolean hasQuarterChanged(Objective objective, Objective savedObjective) {
        return !Objects.equals(objective.getQuarter(), savedObjective.getQuarter());
    }

    public Objective getEntityById(Long id) {
        validator.validateOnGet(id);
        return objectivePersistenceService.findById(id);
    }

    public List<Objective> getEntitiesByTeamId(Long id) {
        validator.validateOnGet(id);
        return objectivePersistenceService.findObjectiveByTeamId(id);
    }

    public List<KeyResult> getAllKeyResultsByObjective(Long objectiveId) {
        Objective objective = objectivePersistenceService.findById(objectiveId);
        return keyResultBusinessService.getAllKeyResultsByObjective(objective.getId());
    }

    @Transactional
    public Objective updateEntity(Long id, Objective objective, AuthorizationUser authorizationUser) {
        Objective savedObjective = objectivePersistenceService.findById(id);
        objective.setCreatedBy(savedObjective.getCreatedBy());
        objective.setCreatedOn(savedObjective.getCreatedOn());
        objective.setModifiedBy(authorizationUser.user());
        objective.setModifiedOn(LocalDateTime.now());
        setQuarterIfIsImUsed(objective, savedObjective);
        validator.validateOnUpdate(id, objective);
        return objectivePersistenceService.save(objective);
    }

    private void setQuarterIfIsImUsed(Objective objective, Objective savedObjective) {
        boolean isImUsed = isImUsed(objective, savedObjective);
        if (isImUsed) {
            objective.setQuarter(savedObjective.getQuarter());
        }
        logger.atDebug().log("quarter has changed and is{}changeable, {}", spaceOrNot(isImUsed), objective);
    }

    private String spaceOrNot(boolean isImUsed) {
        if (isImUsed)
            return " NOT ";
        return " ";
    }

    public boolean isImUsed(Objective objective) {
        Objective savedObjective = objectivePersistenceService.findById(objective.getId());
        return isImUsed(objective, savedObjective);
    }

    private boolean isImUsed(Objective objective, Objective savedObjective) {
        return (hasQuarterChanged(objective, savedObjective) && hasAlreadyCheckIns(savedObjective));
    }

    private boolean hasAlreadyCheckIns(Objective savedObjective) {
        return keyResultBusinessService
                .getAllKeyResultsByObjective(savedObjective.getId())
                .stream()
                .anyMatch(kr -> keyResultBusinessService.hasKeyResultAnyCheckIns(kr.getId()));
    }

    @Transactional
    public Objective createEntity(Objective objective, AuthorizationUser authorizationUser) {
        objective.setCreatedBy(authorizationUser.user());
        objective.setCreatedOn(LocalDateTime.now());
        validator.validateOnCreate(objective);
        return objectivePersistenceService.save(objective);
    }

    /**
     * Create a new Objective (with a new ID) and copy from a source Objective
     * (identified by id) the KeyResults. The CheckIns are not copied.
     *
     * @param id
     *            ID of the source Objective
     * @param objective
     *            New Objective with no KeyResults
     * @param authorizationUser
     *            AuthorizationUser
     * @param keyResultIds
     *            KeyResultIds to copy
     * @return New Objective with copied KeyResults form the source Objective
     */
    @Transactional
    public Objective duplicateObjective(Long id, Objective objective, AuthorizationUser authorizationUser,
                                        List<Long> keyResultIds) {
        Objective duplicatedObjective = createEntity(objective, authorizationUser);
        for (Long keyResult : keyResultIds) {
            duplicateKeyResult(authorizationUser,
                               keyResultBusinessService.getEntityById(keyResult),
                               duplicatedObjective);
        }
        return duplicatedObjective;
    }

    public void duplicateKeyResult(AuthorizationUser authorizationUser, KeyResult keyResult,
                                   Objective duplicatedObjective) {
        if (keyResult.getKeyResultType().equals(KEY_RESULT_TYPE_METRIC)) {
            KeyResult keyResultMetric = makeCopyOfKeyResultMetric(keyResult, duplicatedObjective);
            keyResultBusinessService.createEntity(keyResultMetric, authorizationUser);
        } else if (keyResult.getKeyResultType().equals(KEY_RESULT_TYPE_ORDINAL)) {
            KeyResult keyResultOrdinal = makeCopyOfKeyResultOrdinal(keyResult, duplicatedObjective);
            keyResultBusinessService.createEntity(keyResultOrdinal, authorizationUser);
        }
    }

    public KeyResult makeCopyOfKeyResultMetric(KeyResult keyResult, Objective duplicatedObjective) {
        KeyResult copyOfMetricKeyResult = KeyResultMetric.Builder
                .builder() //
                .withObjective(duplicatedObjective) //
                .withTitle(keyResult.getTitle()) //
                .withDescription(keyResult.getDescription()) //
                .withOwner(keyResult.getOwner()) //
                .withUnit(((KeyResultMetric) keyResult).getUnit()) //
                .withBaseline(((KeyResultMetric) keyResult).getBaseline()) //
                .withStretchGoal(((KeyResultMetric) keyResult).getStretchGoal()) //
                .build();
        copyOfMetricKeyResult.setActionList(duplicateActionList(keyResult, copyOfMetricKeyResult));
        return copyOfMetricKeyResult;
    }

    public KeyResult makeCopyOfKeyResultOrdinal(KeyResult keyResult, Objective duplicatedObjective) {
        KeyResult copyOfOrdinalKeyResult = KeyResultOrdinal.Builder
                .builder() //
                .withObjective(duplicatedObjective) //
                .withTitle(keyResult.getTitle()) //
                .withDescription(keyResult.getDescription()) //
                .withOwner(keyResult.getOwner()) //
                .withCommitZone(((KeyResultOrdinal) keyResult).getCommitZone()) //
                .withTargetZone(((KeyResultOrdinal) keyResult).getTargetZone()) //
                .withStretchZone(((KeyResultOrdinal) keyResult).getStretchZone()) //
                .build();

        copyOfOrdinalKeyResult.setActionList(duplicateActionList(keyResult, copyOfOrdinalKeyResult));
        return copyOfOrdinalKeyResult;
    }

    public List<Action> duplicateActionList(KeyResult oldKeyResult, KeyResult newKeyResult) {
        List<Action> actionList = oldKeyResult.getActionList();
        if (!actionList.isEmpty()) {
            actionList = actionList
                    .stream()
                    .map(e -> Action.Builder
                            .builder()
                            .withKeyResult(newKeyResult)
                            .isChecked(e.isChecked())
                            .withAction(e.getActionPoint())
                            .withPriority(e.getPriority())
                            .build())
                    .toList();
        }
        return actionList;
    }

    @Transactional
    public void deleteEntityById(Long id) {
        validator.validateOnDelete(id);
        completedBusinessService.deleteCompletedByObjectiveId(id);
        keyResultBusinessService //
                .getAllKeyResultsByObjective(id) //
                .forEach(keyResult -> keyResultBusinessService.deleteEntityById(keyResult.getId()));
        objectivePersistenceService.deleteById(id);
    }
}
