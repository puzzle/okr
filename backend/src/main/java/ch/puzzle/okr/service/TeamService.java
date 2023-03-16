package ch.puzzle.okr.service;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class TeamService {

    private final TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
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
        if (team.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id");
        }
        if (team.getName() == null || team.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute name when creating team");
        }
        return teamRepository.save(team);
    }

    public Team updateTeam(Long id, Team team) {
        if (team.getName() == null || team.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute name when creating team");
        }
        this.getTeamById(id);
        return teamRepository.save(team);
    }

    public Team getTeamById(Long teamId) {
        if (teamId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute team id");
        }

        return teamRepository.findById(teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                (String.format("Team with id %d not found", teamId))));
    }
}
