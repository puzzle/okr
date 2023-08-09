package ch.puzzle.okr.service;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.TeamRepository;
import ch.puzzle.okr.service.validation.TeamValidationService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    private final ObjectiveRepository objectiveRepository;

    private final ObjectiveService objectiveService;

    private final TeamValidationService teamValidationService;

    public TeamService(TeamRepository teamRepository, ObjectiveRepository objectiveRepository,
            ObjectiveService objectiveService, TeamValidationService teamValidationService) {
        this.teamRepository = teamRepository;
        this.objectiveRepository = objectiveRepository;
        this.objectiveService = objectiveService;
        this.teamValidationService = teamValidationService;
    }

    public List<Team> getAllTeams() {
        return getAllTeams(Collections.emptyList());
    }

    public List<Team> getAllTeams(List<Long> teamIds) {
        List<Team> teamList = new ArrayList<>();
        Optional<Team> puzzleItcOptional = teamRepository.findByName(Constants.TEAM_PUZZLE);

        if (teamIds.isEmpty()) {
            puzzleItcOptional.ifPresent(teamList::add);
            teamList.addAll(teamRepository.findAllByNameNotOrderByNameAsc(Constants.TEAM_PUZZLE));
        } else {
            if (puzzleItcOptional.isPresent() && teamIds.contains(puzzleItcOptional.get().getId())) {
                puzzleItcOptional.ifPresent(teamList::add);
            }
            teamList.addAll(teamRepository.findAllByIdInAndNameNotOrderByNameAsc(teamIds, Constants.TEAM_PUZZLE));
        }
        return teamList;
    }

    public Team getTeamById(long id) {
        return teamRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                String.format("Team with id %d not found", id)));
    }

    public Team saveTeam(Team team) {
        teamValidationService.validateOnSave(team);
        return teamRepository.save(team);
    }

    public Team updateTeam(Long id, Team team) {
        teamValidationService.validateOnUpdate(team);
        getTeamById(id);
        return teamRepository.save(team);
    }

    public Team getTeamById(Long teamId) {
        if (teamId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute team id");
        }

        return teamRepository.findById(teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                (String.format("Team with id %d not found", teamId))));
    }

    @Transactional
    public void deleteTeamById(Long teamId) {
        objectiveRepository.findByTeamIdOrderByTitleAsc(teamId).forEach(objective -> {
            objectiveService.deleteObjectiveById(objective.getId());
        });

        teamRepository.deleteById(teamId);
    }
}
