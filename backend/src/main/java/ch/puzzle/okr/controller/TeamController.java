package ch.puzzle.okr.controller;

import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/teams")
public class TeamController {

    @Autowired
    TeamRepository teamRepository;

    @GetMapping
    public List<Team> getAllTeams() {
        return (List<Team>) teamRepository.findAll();
    }
}
