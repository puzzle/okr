package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.checkin.CheckIn;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.authorization.ActionAuthorizationService;
import ch.puzzle.okr.service.persistence.KeyResultPersistenceService;
import ch.puzzle.okr.service.validation.KeyResultValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class KeyResultBusinessService implements BusinessServiceInterface<Long, KeyResult> {

    private final KeyResultPersistenceService keyResultPersistenceService;
    private final CheckInBusinessService checkInBusinessService;
    private final ActionAuthorizationService actionAuthorizationService;
    private final KeyResultValidationService validator;
    private static final Logger logger = LoggerFactory.getLogger(KeyResultBusinessService.class);

    public KeyResultBusinessService(KeyResultPersistenceService keyResultPersistenceService,
            KeyResultValidationService validator, CheckInBusinessService checkInBusinessService,
            ActionAuthorizationService actionAuthorizationService) {
        this.keyResultPersistenceService = keyResultPersistenceService;
        this.checkInBusinessService = checkInBusinessService;
        this.actionAuthorizationService = actionAuthorizationService;
        this.validator = validator;
    }

    @Transactional
    public KeyResult createEntity(KeyResult keyResult, AuthorizationUser authorizationUser) {
        keyResult.setCreatedOn(LocalDateTime.now());
        keyResult.setCreatedBy(authorizationUser.user());
        validator.validateOnCreate(keyResult);
        return keyResultPersistenceService.save(keyResult);
    }

    public KeyResult getEntityById(Long id) {
        validator.validateOnGet(id);
        return keyResultPersistenceService.findById(id);
    }

    @Transactional
    public KeyResult updateEntity(Long id, KeyResult keyResult, AuthorizationUser authorizationUser) {
        KeyResult savedKeyResult = keyResultPersistenceService.findById(id);
        keyResult.setCreatedBy(savedKeyResult.getCreatedBy());
        keyResult.setCreatedOn(savedKeyResult.getCreatedOn());
        keyResult.setObjective(savedKeyResult.getObjective());
        keyResult.setModifiedOn(LocalDateTime.now());
        if (Objects.equals(keyResult.getKeyResultType(), savedKeyResult.getKeyResultType())) {
            logger.debug("keyResultType is identically, {}", keyResult);
            validator.validateOnUpdate(id, keyResult);
            return keyResultPersistenceService.updateEntity(keyResult);
        } else {
            if (isKeyResultTypeChangeable(id)) {
                logger.debug("keyResultType has changed and is changeable, {}", keyResult);
                validator.validateOnUpdate(id, keyResult);
                List<Action> actionList = actionAuthorizationService.getEntitiesByKeyResultId(id);
                KeyResult createdKeyResult = keyResultPersistenceService.recreateEntity(id, keyResult);
                actionAuthorizationService.updateEntities(actionList);
                return createdKeyResult;
            } else {
                savedKeyResult.setTitle(keyResult.getTitle());
                savedKeyResult.setDescription(keyResult.getDescription());
                savedKeyResult.setOwner(keyResult.getOwner());
                savedKeyResult.setModifiedOn(keyResult.getModifiedOn());
                logger.debug("keyResultType has changed and is NOT changeable, {}", savedKeyResult);
                validator.validateOnUpdate(id, keyResult);
                return keyResultPersistenceService.updateEntity(savedKeyResult);
            }
        }
    }

    @Transactional
    public void deleteEntityById(Long id) {
        validator.validateOnDelete(id);
        checkInBusinessService.getCheckInsByKeyResultId(id)
                .forEach(checkIn -> checkInBusinessService.deleteEntityById(checkIn.getId()));
        actionAuthorizationService.getEntitiesByKeyResultId(id)
                .forEach(action -> actionAuthorizationService.deleteActionByActionId(action.getId()));
        keyResultPersistenceService.deleteById(id);
    }

    public List<CheckIn> getAllCheckInsByKeyResult(Long keyResultId) {
        KeyResult keyResult = keyResultPersistenceService.findById(keyResultId);
        return checkInBusinessService.getCheckInsByKeyResultId(keyResult.getId());
    }

    public List<KeyResult> getAllKeyResultsByObjective(Long objectiveId) {
        return keyResultPersistenceService.getKeyResultsByObjective(objectiveId);
    }

    private boolean isKeyResultTypeChangeable(Long id) {
        return !hasKeyResultAnyCheckIns(id);
    }

    public boolean hasKeyResultAnyCheckIns(Long id) {
        return !checkInBusinessService.getCheckInsByKeyResultId(id).isEmpty();
    }

    public boolean isImUsed(Long id, KeyResult keyResult) {
        return hasKeyResultAnyCheckIns(id)
                && !keyResultPersistenceService.findById(id).getKeyResultType().equals(keyResult.getKeyResultType());
    }
}
