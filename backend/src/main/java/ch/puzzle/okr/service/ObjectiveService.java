package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistance.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ObjectiveService {
    private final ObjectivePersistenceService objectivePersistenceService;
    private final ObjectiveValidationService validator;
    private final KeyResultService keyResultService;
    private final UserService userService;

    public ObjectiveService(@Lazy KeyResultService keyResultService, ObjectiveValidationService validator,
            ObjectivePersistenceService objectivePersistenceService, UserService userService) {
        this.keyResultService = keyResultService;
        this.validator = validator;
        this.objectivePersistenceService = objectivePersistenceService;
        this.userService = userService;
    }

    public Objective getObjectiveById(Long id) {
        validator.validateOnGet(id);
        return objectivePersistenceService.findById(id);
    }

    @Transactional
    public Objective updateObjective(Long id, Objective objective, String username) {
        validator.validateOnUpdate(id, objective);
        objective.setCreatedBy(userService.getUserByUsername(username));
        return objectivePersistenceService.save(objective);
    }

    @Transactional
    public Objective createObjective(Objective objective, String username) {
        validator.validateOnCreate(objective);
        objective.setCreatedBy(userService.getUserByUsername(username));
        return objectivePersistenceService.save(objective);
    }

    @Transactional
    public void deleteObjectiveById(Long id) {
        validator.validateOnDelete(id);
        keyResultService.getAllKeyResultsByObjective(id)
                .forEach(keyResult -> keyResultService.deleteKeyResultById(keyResult.getId()));
        objectivePersistenceService.deleteById(id);
    }

    public Integer activeObjectivesAmountOfTeam(Team team, Quarter quarter) {
        // validate quarter in objective validator by using the quarter validator
        return objectivePersistenceService.countByTeamAndQuarter(team, quarter);
    }
}
