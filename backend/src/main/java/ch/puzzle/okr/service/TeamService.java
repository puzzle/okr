package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.TeamDto;
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

    public Team saveTeam(Team team) {
        if (team.getId() != null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Not allowed to give an id");
        }
        if (team.getName() == null || team.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute name when creating team");
        }
        return teamRepository.save(team);
    }

    public Team updateTeam(long id, TeamDto teamDto) {
        if (teamDto.getName() == null || teamDto.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing attribute name when creating team");
        }
        this.getTeamById(id);
        return this.teamRepository.save(
                Team.Builder.builder()
                        .withId(id)
                        .withName(teamDto.getName()).build());
    }
}
