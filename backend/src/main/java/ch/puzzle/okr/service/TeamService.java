package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.text.Collator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;

@Service
public class TeamService {

    private final TeamRepository teamRepository;
    Collator collator = Collator.getInstance(Locale.GERMAN);

    public TeamService(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    public List<Team> getAllTeams() {
        Team puzzleTeam = teamRepository.findByName("Puzzle ITC");
        List<Team> teamList = (List<Team>) teamRepository.findAll();
        return Stream
                .concat(Stream.of(puzzleTeam),
                        teamList.stream()
                                .filter(team -> !"Puzzle ITC".equals(team.getName()))
                                .sorted((team1, team2) -> collator.compare(team1.getName(), team2.getName())))
                .toList();
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
