package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class TeamPersistenceService {

    private final TeamRepository teamRepository;

    public TeamPersistenceService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public Optional<Team> getTeamByName(String name) {
        return teamRepository.findByName(name);
    }

    public List<Team> getTeamsByExcludedName(String name) {
        return teamRepository.findAllByNameNotOrderByNameAsc(name);
    }

    public List<Team> getTeamsByIdsAndExcludedName(List<Long> teamIds, String name) {
        return teamRepository.findAllByIdInAndNameNotOrderByNameAsc(teamIds, name);
    }

    public Team getTeamById(Long teamId) {
        if (teamId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute team id");
        }
        return teamRepository.findById(teamId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                (String.format("Team with id %d not found", teamId))));

    }

    public Team saveTeam(Team team) {
        return teamRepository.save(team);
    }

    public Team updateTeam(Long id, Team team) {
        return teamRepository.save(team);
    }

    public void deleteTeamById(Long teamId) {
        teamRepository.deleteById(teamId);
    }
}
