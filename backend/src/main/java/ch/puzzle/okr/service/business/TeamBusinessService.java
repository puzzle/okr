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

    public Team getTeamById(Long id) {
        validator.validateOnGet(id);
        return teamPersistenceService.findById(id);
    }

    public Integer activeObjectivesAmountOfTeam(Team team, Long quarterId) {
        validator.validateOnGetActiveObjectives(team);
        Quarter activeQuarter = quarterBusinessService.getQuarterById(quarterId);
        return objectiveBusinessService.activeObjectivesAmountOfTeam(team, activeQuarter);
    }
}
