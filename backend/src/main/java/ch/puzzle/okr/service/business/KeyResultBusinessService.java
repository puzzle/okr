package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultWithActionList;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class KeyResultBusinessService implements BusinessServiceInterface<Long, KeyResult> {

    private static final Logger logger = LoggerFactory.getLogger(KeyResultBusinessService.class);
    private final KeyResultPersistenceService keyResultPersistenceService;
    private final CheckInBusinessService checkInBusinessService;
    private final ActionBusinessService actionBusinessService;
    private final KeyResultValidationService validator;

    public KeyResultBusinessService(KeyResultPersistenceService keyResultPersistenceService,
            KeyResultValidationService validator, CheckInBusinessService checkInBusinessService,
            ActionBusinessService actionBusinessService) {
        this.keyResultPersistenceService = keyResultPersistenceService;
        this.checkInBusinessService = checkInBusinessService;
        this.actionBusinessService = actionBusinessService;
        this.validator = validator;
    }

    @Override
    @Transactional
    public KeyResult createEntity(KeyResult keyResult, AuthorizationUser authorizationUser) {
        keyResult.setCreatedOn(LocalDateTime.now());
        keyResult.setCreatedBy(authorizationUser.user());
        validator.validateOnCreate(keyResult);
        return keyResultPersistenceService.save(keyResult);
    }

    @Override
    public KeyResult getEntityById(Long id) {
        validator.validateOnGet(id);
        return keyResultPersistenceService.findById(id);
    }

    @Override
    public KeyResult updateEntity(Long id, KeyResult keyResult, AuthorizationUser authorizationUser) {
        throw new IllegalCallerException("unsupported method 'updateEntity' use updateEntities() instead");
    }

    @Transactional
    public KeyResultWithActionList updateEntities(Long id, KeyResult keyResult, List<Action> actionList) {
        KeyResult savedKeyResult = keyResultPersistenceService.findById(id);
        keyResult.setCreatedBy(savedKeyResult.getCreatedBy());
        keyResult.setCreatedOn(savedKeyResult.getCreatedOn());
        keyResult.setObjective(savedKeyResult.getObjective());
        keyResult.setModifiedOn(LocalDateTime.now());
        if (Objects.equals(keyResult.getKeyResultType(), savedKeyResult.getKeyResultType())) {
            logger.debug("keyResultType is identically, {}", keyResult);
            validator.validateOnUpdate(id, keyResult);
            return new KeyResultWithActionList(keyResultPersistenceService.updateEntity(keyResult),
                    actionBusinessService.updateEntities(actionList));
        } else {
            if (isKeyResultTypeChangeable(id)) {
                logger.debug("keyResultType has changed and is changeable, {}", keyResult);
                validator.validateOnUpdate(id, keyResult);
                return new KeyResultWithActionList(recreateEntity(id, keyResult, actionList),
                        actionBusinessService.createEntities(actionList));
            } else {
                savedKeyResult.setTitle(keyResult.getTitle());
                savedKeyResult.setDescription(keyResult.getDescription());
                savedKeyResult.setOwner(keyResult.getOwner());
                savedKeyResult.setModifiedOn(keyResult.getModifiedOn());
                logger.debug("keyResultType has changed and is NOT changeable, {}", savedKeyResult);
                validator.validateOnUpdate(id, keyResult);
                return new KeyResultWithActionList(keyResultPersistenceService.updateEntity(savedKeyResult),
                        actionBusinessService.updateEntities(actionList));
            }
        }
    }

    private KeyResult recreateEntity(Long id, KeyResult keyResult, List<Action> actionList) {
        actionBusinessService.deleteEntitiesByKeyResultId(id);
        KeyResult recreatedEntity = keyResultPersistenceService.recreateEntity(id, keyResult);
        actionList.forEach(action -> {
            action.resetId();
            action.setKeyResult(recreatedEntity);
        });
        return recreatedEntity;
    }

    @Transactional
    public void deleteEntityById(Long id) {
        validator.validateOnDelete(id);
        checkInBusinessService.getCheckInsByKeyResultId(id)
                .forEach(checkIn -> checkInBusinessService.deleteEntityById(checkIn.getId()));
        actionBusinessService.getActionsByKeyResultId(id)
                .forEach(action -> actionBusinessService.deleteEntityById(action.getId()));
        keyResultPersistenceService.deleteById(id);
    }

    public List<CheckIn> getAllCheckInsByKeyResult(Long keyResultId) {
        KeyResult keyResult = keyResultPersistenceService.findById(keyResultId);
        return checkInBusinessService.getCheckInsByKeyResultId(keyResult.getId());
    }

    public List<KeyResult> getAllKeyResultsByObjective(Long objectiveId) {
        return keyResultPersistenceService.getKeyResultsByObjective(objectiveId);
    }

    public boolean hasKeyResultAnyCheckIns(Long id) {
        return !checkInBusinessService.getCheckInsByKeyResultId(id).isEmpty();
    }

    public boolean isImUsed(Long id, KeyResult keyResult) {
        return hasKeyResultAnyCheckIns(id)
                && !keyResultPersistenceService.findById(id).getKeyResultType().equals(keyResult.getKeyResultType());
    }

    private boolean isKeyResultTypeChangeable(Long id) {
        return !hasKeyResultAnyCheckIns(id);
    }
}
