package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.models.keyresult.KeyResultOrdinal;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
public class ObjectiveBusinessService implements BusinessServiceInterface<Long, Objective> {
    private final ObjectivePersistenceService objectivePersistenceService;
    private final ObjectiveValidationService validator;
    private final KeyResultBusinessService keyResultBusinessService;

    public ObjectiveBusinessService(@Lazy KeyResultBusinessService keyResultBusinessService,
            ObjectiveValidationService validator, ObjectivePersistenceService objectivePersistenceService) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.validator = validator;
        this.objectivePersistenceService = objectivePersistenceService;
    }

    public Objective getEntityById(Long id) {
        validator.validateOnGet(id);
        return objectivePersistenceService.findById(id);
    }

    @Transactional
    public Objective updateEntity(Long id, Objective objective, AuthorizationUser authorizationUser) {
        Objective savedObjective = objectivePersistenceService.findById(id);
        objective.setCreatedBy(savedObjective.getCreatedBy());
        objective.setCreatedOn(savedObjective.getCreatedOn());
        objective.setModifiedBy(authorizationUser.user());
        objective.setModifiedOn(LocalDateTime.now());
        throwExceptionWhenQuarterIsNotChangable(objective, savedObjective);
        validator.validateOnUpdate(id, objective);
        return objectivePersistenceService.save(objective);
    }

    private void throwExceptionWhenQuarterIsNotChangable(Objective objective, Objective savedObjective) {
        if (!Objects.equals(objective.getQuarter(), savedObjective.getQuarter()) && keyResultBusinessService
                .getAllKeyResultsByObjective(savedObjective.getId()).stream()
                .filter(kr -> keyResultBusinessService.hasKeyResultAnyCheckIns(kr.getId())).findAny().isPresent()) {
            throw new ResponseStatusException(BAD_REQUEST,
                    format("Not allowed to change the quarter of objective %s (id=%s)", savedObjective.getTitle(),
                            savedObjective.getId()));
        }
    }

    @Transactional
    public Objective createEntity(Objective objective, AuthorizationUser authorizationUser) {
        objective.setCreatedBy(authorizationUser.user());
        objective.setCreatedOn(LocalDateTime.now());
        validator.validateOnCreate(objective);
        return objectivePersistenceService.save(objective);
    }

    @Transactional
    public Objective duplicateObjective(Long id, Objective objective, AuthorizationUser authorizationUser) {
        Objective duplicatedObjective = createEntity(objective, authorizationUser);
        List<KeyResult> keyResultsOfDuplicatedObjective = keyResultBusinessService.getAllKeyResultsByObjective(id);
        for (KeyResult keyResult : keyResultsOfDuplicatedObjective) {
            if (keyResult.getKeyResultType().equals("metric")) {
                KeyResult keyResultMetric = KeyResultMetric.Builder.builder().withObjective(duplicatedObjective)
                        .withTitle(keyResult.getTitle()).withDescription(keyResult.getDescription())
                        .withOwner(authorizationUser.user()).withUnit(((KeyResultMetric) keyResult).getUnit())
                        .withBaseline(0D).withStretchGoal(1D).build();
                keyResultBusinessService.createEntity(keyResultMetric, authorizationUser);
            } else if (keyResult.getKeyResultType().equals("ordinal")) {
                KeyResult keyResultOrdinal = KeyResultOrdinal.Builder.builder().withObjective(duplicatedObjective)
                        .withTitle(keyResult.getTitle()).withDescription(keyResult.getDescription())
                        .withOwner(authorizationUser.user()).withCommitZone("-").withTargetZone("-")
                        .withStretchZone("-").build();
                keyResultBusinessService.createEntity(keyResultOrdinal, authorizationUser);
            }
        }
        return duplicatedObjective;
    }

    @Transactional
    public void deleteEntityById(Long id) {
        validator.validateOnDelete(id);
        keyResultBusinessService.getAllKeyResultsByObjective(id)
                .forEach(keyResult -> keyResultBusinessService.deleteEntityById(keyResult.getId()));
        objectivePersistenceService.deleteById(id);
    }

    public Integer activeObjectivesAmountOfTeam(Team team, Quarter quarter) {
        // validate quarter in objective validator by using the quarter validator
        return objectivePersistenceService.countByTeamAndQuarter(team, quarter);
    }
}
