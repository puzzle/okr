package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.service.TeamService;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/teams")
public class TeamController {
    @Autowired
    TeamRepository teamRepository;

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

    @Operation(summary = "Get specific teams")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found a team",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamController.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid id supplied",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Team not found",
                    content = @Content) })
    @GetMapping("/{id}")
    public Optional<Team> getTeam(@PathVariable long id) {
        return teamRepository.findById(id);
    }
}
