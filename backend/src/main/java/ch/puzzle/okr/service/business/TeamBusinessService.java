package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamBusinessService {

    private final TeamPersistenceService teamPersistenceService;

    private final ObjectiveBusinessService objectiveBusinessService;

    private final QuarterBusinessService quarterBusinessService;

    private final TeamValidationService validator;

    public TeamBusinessService(TeamPersistenceService teamPersistenceService,
            ObjectiveBusinessService objectiveBusinessService, QuarterBusinessService quarterBusinessService,
            TeamValidationService validator) {
        this.teamPersistenceService = teamPersistenceService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.quarterBusinessService = quarterBusinessService;
        this.validator = validator;
    }

    public Team getTeamById(Long teamId) {
        validator.validateOnGet(teamId);
        return teamPersistenceService.findById(teamId);
    }

    public Team createTeam(Team team) {
        validator.validateOnCreate(team);
        return teamPersistenceService.save(team);
    }

    public Team updateTeam(Team team, Long id) {
        validator.validateOnUpdate(id, team);
        return teamPersistenceService.save(team);
    }

    public void deleteTeam(Long id) {
        validator.validateOnDelete(id);
        objectiveBusinessService.getEntitiesByTeamId(id)
                .forEach(objective -> objectiveBusinessService.deleteEntityById(objective.getId()));
        teamPersistenceService.deleteById(id);
    }

    public List<Team> getAllTeams() {
        return teamPersistenceService.findAll();
    }

    public Integer activeObjectivesAmountOfTeam(Team team, Long quarterId) {
        validator.validateOnGetActiveObjectives(team);
        Quarter activeQuarter = quarterBusinessService.getQuarterById(quarterId);
        return objectiveBusinessService.activeObjectivesAmountOfTeam(team, activeQuarter);
    }
}
