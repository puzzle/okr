package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.CacheService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamBusinessService {

    private final TeamPersistenceService teamPersistenceService;

    private final ObjectiveBusinessService objectiveBusinessService;

    private final TeamValidationService validator;
    private final CacheService cacheService;

    public TeamBusinessService(TeamPersistenceService teamPersistenceService,
            ObjectiveBusinessService objectiveBusinessService, QuarterBusinessService quarterBusinessService,
            TeamValidationService validator, CacheService cacheService) {
        this.teamPersistenceService = teamPersistenceService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.validator = validator;
        this.cacheService = cacheService;
    }

    public Team getTeamById(Long teamId) {
        validator.validateOnGet(teamId);
        return teamPersistenceService.findById(teamId);
    }

    public Team createTeam(Team team) {
        validator.validateOnCreate(team);
        cacheService.emptyAuthorizationUsersCache();
        return teamPersistenceService.save(team);
    }

    public Team updateTeam(Team team, Long id) {
        validator.validateOnUpdate(id, team);
        cacheService.emptyAuthorizationUsersCache();
        return teamPersistenceService.save(team);
    }

    public void deleteTeam(Long id) {
        validator.validateOnDelete(id);
        objectiveBusinessService.getEntitiesByTeamId(id)
                .forEach(objective -> objectiveBusinessService.deleteEntityById(objective.getId()));
        cacheService.emptyAuthorizationUsersCache();
        teamPersistenceService.deleteById(id);
    }

    public List<Team> getAllTeams() {
        return teamPersistenceService.findAll();
    }
}
