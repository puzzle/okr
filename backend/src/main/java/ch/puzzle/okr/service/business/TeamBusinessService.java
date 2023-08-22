package ch.puzzle.okr.service.business;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.ValidationService;
import ch.puzzle.okr.service.persistence.TeamPersistenceService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TeamBusinessService {

    private final TeamPersistenceService teamPersistenceService;

    private final ObjectiveBusinessService objectiveBusinessService;

    private final ValidationService validationService;

    public TeamBusinessService(TeamPersistenceService teamPersistenceService,
            ObjectiveBusinessService objectiveBusinessService, ValidationService validationService) {
        this.teamPersistenceService = teamPersistenceService;
        this.objectiveBusinessService = objectiveBusinessService;
        this.validationService = validationService;
    }

    public List<Team> getAllTeams() {
        return getAllTeams(Collections.emptyList());
    }

    public List<Team> getAllTeams(List<Long> teamIds) {
        List<Team> teamList = new ArrayList<>();
        Optional<Team> puzzleItcOptional = teamPersistenceService.getTeamByName(Constants.TEAM_PUZZLE);

        if (teamIds.isEmpty()) {
            puzzleItcOptional.ifPresent(teamList::add);
            teamList.addAll(teamPersistenceService.getTeamsByExcludedName(Constants.TEAM_PUZZLE));
        } else {
            if (puzzleItcOptional.isPresent() && teamIds.contains(puzzleItcOptional.get().getId())) {
                puzzleItcOptional.ifPresent(teamList::add);
            }
            teamList.addAll(teamPersistenceService.getTeamsByIdsAndExcludedName(teamIds, Constants.TEAM_PUZZLE));
        }
        return teamList;
    }

    public Team saveTeam(Team team) {
        validationService.validateOnSave(team);
        return teamPersistenceService.saveTeam(team);
    }

    public Team updateTeam(Long id, Team team) {
        validationService.validateOnUpdate(team);
        getTeamById(id);
        return teamPersistenceService.updateTeam(id, team);
    }

    public Team getTeamById(Long teamId) {
        return teamPersistenceService.getTeamById(teamId);
    }

    @Transactional
    public void deleteTeamById(Long teamId) {
        objectiveBusinessService.getObjectivesByTeamIdOrderByTitleAsc(teamId)
                .forEach(objective -> objectiveBusinessService.deleteObjectiveById(objective.getId()));

        teamPersistenceService.deleteTeamById(teamId);
    }
}
