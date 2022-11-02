package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class TeamService {

    TeamRepository teamRepository;

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        return (List<Team>) teamRepository.findAll();
    }
    public Team getTeamById(long id) {
        return teamRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Team with id %d not found", id))
        );
    }

    public Team saveTeam(Team givenTeam) {
        return teamRepository.save(Team.Builder.builder().withName(givenTeam.getName()).build());
    }
}
