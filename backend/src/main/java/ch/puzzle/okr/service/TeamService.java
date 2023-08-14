package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    private final ObjectiveService objectiveService;

    private final TeamValidationService validator;

    private final QuarterService quarterService;

    public TeamService(TeamRepository teamRepository, ObjectiveService objectiveService,
            TeamValidationService validator, QuarterService quarterService) {
        this.teamRepository = teamRepository;
        this.objectiveService = objectiveService;
        this.validator = validator;
        this.quarterService = quarterService;
    }

    public List<Team> getAllTeams() {
        return (List<Team>) teamRepository.findAll();
    }

    @Transactional
    public Team getTeamById(Long id) {
        validator.validateOnGet(id);
        // It's impossible for the next line to return null since it gets validated before
        return teamRepository.findById(id).orElse(null);
    }

    @Transactional
    public Team updateTeam(Long id, Team team) {
        validator.validateOnUpdate(id, team);
        return teamRepository.save(team);
    }

    @Transactional
    public Team createTeam(Team team) {
        validator.validateOnCreate(team);
        return teamRepository.save(team);
    }

    @Transactional
    public void deleteTeamById(Long teamId) {
        validator.validateOnDelete(teamId);
        teamRepository.deleteById(teamId);
    }

    public Integer activeObjectivesAmountOfTeam(Team team) {
        String currentQuarter = quarterService.getQuarter(quarterService.now);
        Quarter quarter = quarterService.getOrCreateQuarter(currentQuarter);
        return objectiveService.activeObjectivesAmountOfTeam(team, quarter);
    }
}
