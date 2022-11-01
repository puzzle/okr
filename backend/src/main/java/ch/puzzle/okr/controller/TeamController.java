package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.service.TeamService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/teams")
public class TeamController {

    TeamService teamService;
    TeamMapper teamMapper;

    public TeamController(TeamService teamService, TeamMapper teamMapper) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
    }

    @GetMapping
    public List<TeamDto> getAllTeams() {
        return teamService.getAllTeams().stream()
                .map(teamMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public TeamDto getTeams(@PathVariable long id) {
        return teamMapper.toDto(teamService.getTeamById(id));
    }
}
