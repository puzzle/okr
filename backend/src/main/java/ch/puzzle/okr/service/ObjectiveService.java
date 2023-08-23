package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.service.persistance.ObjectivePersistenceService;
import ch.puzzle.okr.service.validation.ObjectiveValidationService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final KeyResultService keyResultService;
    private final ObjectiveValidationService validator;
    private final ObjectivePersistenceService objectivePersistenceService;

    public ObjectiveService(ObjectiveRepository objectiveRepository, @Lazy KeyResultService keyResultService,
            ObjectiveValidationService validator, ObjectivePersistenceService objectivePersistenceService) {
        this.objectiveRepository = objectiveRepository;
        this.keyResultService = keyResultService;
        this.validator = validator;
        this.objectivePersistenceService = objectivePersistenceService;
    }

    public List<Objective> getAllObjectives() {
        return objectivePersistenceService.findAll();
    }

    public Objective getObjectiveById(Long id) {
        validator.validateOnGet(id);
        return objectivePersistenceService.findById(id);
    }

    public List<Objective> getObjectivesByTeam(Long teamId) {
        return objectivePersistenceService.getObjectivesByTeamId(teamId);
    }

    public Objective updateObjective(Long id, Objective objective) {
        validator.validateOnUpdate(id, objective);
        return objectivePersistenceService.save(objective);
    }

    public Objective createObjective(Objective objective) {
        validator.validateOnCreate(objective);
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
