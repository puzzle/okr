package ch.puzzle.okr.controller;

import ch.puzzle.okr.common.BusinessException;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/teams")
public class TeamController {

    @Autowired
    TeamService teamService;

    @Autowired
    TeamMapper teamMapper;

    @GetMapping
    public List<TeamDto> getAllTeams() {
        return teamService.getAllTeams().stream()
                .map(teamMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getTeams(@PathVariable long id) {
        try {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(teamMapper.toDto(teamService.getTeamById(id)));
        } catch (BusinessException e) {
            return ResponseEntity.status(e.getCode()).body(e.getClientMessage());
        }
    }
}
