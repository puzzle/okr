package ch.puzzle.okr.service;

import ch.puzzle.okr.common.BusinessException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamService {

    @Autowired
    TeamRepository teamRepository;

    public List<Team> getAllTeams() {
        return (List<Team>) teamRepository.findAll();
    }
    public Team getTeamById(long id) throws BusinessException {
        return teamRepository.findById(id).orElseThrow(() -> new BusinessException(404, String.format("Team with id %d not found", id)));
    }
}
