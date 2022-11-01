package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.TeamService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/teams")
public class TeamController {
    private final TeamService teamService;
    private final TeamMapper teamMapper;

    public TeamController(TeamService teamService, TeamMapper teamMapper) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all teams",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Team.class))}),
    })
    @GetMapping
    public List<TeamDto> getAllTeams() {
        return teamService.getAllTeams().stream()
                .map(teamMapper::toDto)
                .toList();
    }


    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a team with a specified ID.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Team.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find a team with a specified ID.", content = @Content)
    })
    @GetMapping("/{id}")
    public TeamDto getTeams(@PathVariable long id) {
        return teamMapper.toDto(teamService.getTeamById(id));
    }

}
