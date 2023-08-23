package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TeamBusinessService {

    private final TeamPersistenceService teamPersistenceService;

    private final ObjectiveBusinessService objectiveBusinessService;

    private final QuarterBusinessService quarterBusinessService;

    private final TeamValidationService validator;;

    public TeamBusinessService(TeamPersistenceService teamPersistenceService,
            ObjectiveBusinessService objectiveBusinessService, QuarterBusinessService quarterBusinessService,
            TeamValidationService validator) {
        this.teamPersistenceService = teamPersistenceService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.quarterBusinessService = quarterBusinessService;
        this.validator = validator;
    }

    public List<Team> getAllTeams() {
        return teamPersistenceService.findAll();
    }

    @Transactional
    @Deprecated
    public Team createTeam(Team team) {
        validator.validateOnCreate(team);
        return teamPersistenceService.save(team);
    }

    @Transactional
    @Deprecated
    public Team updateTeam(Long id, Team team) {
        validator.validateOnUpdate(id, team);
        getTeamById(id);
        return teamPersistenceService.save(team);
    }

    public Team getTeamById(Long id) {
        validator.validateOnGet(id);
        return teamPersistenceService.findById(id);
    }

    @Transactional
    @Deprecated
    public void deleteTeamById(Long teamId) {
        validator.validateOnDelete(teamId);
        objectiveBusinessService.getObjectivesByTeamIdOrderByTitleAsc(teamId)
                .forEach(objective -> objectiveBusinessService.deleteObjectiveById(objective.getId()));

        teamPersistenceService.deleteById(teamId);
    }

    public Integer activeObjectivesAmountOfTeam(Team team, Long quarterId) {
        validator.validateOnGetActiveObjectives(team);
        Quarter activeQuarter = quarterBusinessService.getQuarterById(quarterId);
        return objectiveBusinessService.activeObjectivesAmountOfTeam(team, activeQuarter);
    }
}
