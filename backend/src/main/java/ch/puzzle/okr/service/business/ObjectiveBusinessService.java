package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.State;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;

@Service
public class ObjectiveBusinessService {
    private final ObjectivePersistenceService objectivePersistenceService;
    private final ObjectiveValidationService validator;
    private final KeyResultBusinessService keyResultBusinessService;
    private final UserBusinessService userBusinessService;

    public ObjectiveBusinessService(@Lazy KeyResultBusinessService keyResultBusinessService,
            ObjectiveValidationService validator, ObjectivePersistenceService objectivePersistenceService,
            UserBusinessService userBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.validator = validator;
        this.objectivePersistenceService = objectivePersistenceService;
        this.userBusinessService = userBusinessService;
    }

    public Objective getObjectiveById(Long id) {
        validator.validateOnGet(id);
        return objectivePersistenceService.findById(id);
    }

    @Transactional
    public Objective updateObjective(Long id, Objective objective, Jwt token) {
        objective.setCreatedBy(userBusinessService.getUserByAuthorisationToken(token));
        validator.validateOnUpdate(id, objective);
        return objectivePersistenceService.save(objective);
    }

    @Transactional
    public Objective createObjective(Objective objective, Jwt token) {
        objective.setCreatedBy(userBusinessService.getUserByAuthorisationToken(token));
        objective.setState(State.DRAFT);
        objective.setCreatedOn(LocalDateTime.now());
        validator.validateOnCreate(objective);
        return objectivePersistenceService.save(objective);
    }

    @Transactional
    public void deleteObjectiveById(Long id) {
        validator.validateOnDelete(id);
        keyResultBusinessService.getAllKeyResultsByObjective(id)
                .forEach(keyResult -> keyResultBusinessService.deleteKeyResultById(keyResult.getId()));
        objectivePersistenceService.deleteById(id);
    }

    public Integer activeObjectivesAmountOfTeam(Team team, Quarter quarter) {
        // validate quarter in objective validator by using the quarter validator
        return objectivePersistenceService.countByTeamAndQuarter(team, quarter);
    }
}
