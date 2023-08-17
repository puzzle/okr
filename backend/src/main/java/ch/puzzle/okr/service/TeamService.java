package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.persistance.TeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TeamService {

    private final TeamPersistenceService teamPersistenceService;

    private final ObjectiveService objectiveService;

    private final TeamValidationService validator;

    private final QuarterService quarterService;

    public TeamService(TeamPersistenceService teamPersistenceService, ObjectiveService objectiveService,
            TeamValidationService validator, QuarterService quarterService) {
        this.teamPersistenceService = teamPersistenceService;
        this.objectiveService = objectiveService;
        this.validator = validator;
        this.quarterService = quarterService;
    }

    public List<Team> getAllTeams() {
        return teamPersistenceService.findAll();
    }

    @Transactional
    public Team getTeamById(Long id) {
        validator.validateOnGet(id);
        return teamPersistenceService.findById(id);
    }

    @Transactional
    @Deprecated
    public Team updateTeam(Long id, Team team) {
        validator.validateOnUpdate(id, team);
        return teamPersistenceService.save(team);
    }

    @Transactional
    @Deprecated
    public Team createTeam(Team team) {
        validator.validateOnCreate(team);
        return teamPersistenceService.save(team);
    }

    @Transactional
    @Deprecated
    public void deleteTeamById(Long teamId) {
        validator.validateOnDelete(teamId);
        objectiveService.getObjectivesByTeam(teamId).forEach(objective -> objectiveService.deleteObjectiveById(teamId));
        teamPersistenceService.deleteById(teamId);
    }

    public Integer activeObjectivesAmountOfTeam(Team team, Long quarterId) {
        validator.validateOnGetActiveObjectives(team);
        Quarter activeQuarter = quarterService.getQuarterById(quarterId);
        return objectiveService.activeObjectivesAmountOfTeam(team, activeQuarter);
    }
}
