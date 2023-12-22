package ch.puzzle.okr.service.business;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.authorization.AuthorizationUser;
import ch.puzzle.okr.service.CacheService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
public class TeamBusinessService {

    private final TeamPersistenceService teamPersistenceService;

    private final ObjectiveBusinessService objectiveBusinessService;

    private final TeamValidationService validator;
    private final CacheService cacheService;

    public TeamBusinessService(TeamPersistenceService teamPersistenceService,
            ObjectiveBusinessService objectiveBusinessService, TeamValidationService validator,
            CacheService cacheService) {
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

    public List<Team> getAllTeams(AuthorizationUser authorizationUser) {
        List<Team> teams = teamPersistenceService.findAll();
        List<Team> mutableTeams = new ArrayList<>(teams);
        return sortTeams(mutableTeams, authorizationUser);
    }

    private List<Team> sortTeams(List<Team> teams, AuthorizationUser authorizationUser) {
        teams.sort(new TeamComparator(authorizationUser));
        return teams;
    }

    private record TeamComparator(AuthorizationUser authorizationUser) implements Comparator<Team> {
        @Override
        public int compare(Team t1, Team t2) {
            boolean isUserTeam1 = authorizationUser.extractTeamIds().contains(t1.getId());
            boolean isUserTeam2 = authorizationUser.extractTeamIds().contains(t2.getId());

            if (isUserTeam1 == isUserTeam2) {
                if (Objects.equals(t1.getName(), t2.getName())) {
                    return t1.getId().compareTo(t2.getId());
                } else {
                    return t1.getName().compareTo(t2.getName());
                }
            }
            return isUserTeam1 ? -1 : 1;
        }
    }
}
