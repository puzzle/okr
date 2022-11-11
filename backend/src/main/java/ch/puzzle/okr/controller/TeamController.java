package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @Operation(summary = "Get Teams",
            description = "Get all Teams from db.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Teams.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamDto.class))}),
    })
    @GetMapping
    public List<TeamDto> getAllTeams() {
        return teamService.getAllTeams().stream()
                .map(teamMapper::toDto)
                .toList();
    }

    @Operation(summary = "Get Team",
            description = "Get a Team by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a Team with a specified ID.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamDto.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find a Team with a specified ID.", content = @Content)
    })
    @GetMapping("/{id}")
    public TeamDto getTeamById(
            @Parameter(description = "The ID for getting a Team.", required = true)
            @PathVariable long id) {
        return teamMapper.toDto(teamService.getTeamById(id));
    }

    @Operation(summary = "Create Team",
            description = "Create a new Team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Created new Team.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamDto.class))}),
            @ApiResponse(responseCode = "400", description = "Can't create new Team, not allowed to give an ID or missing attributes.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createTeam(@RequestBody TeamDto teamDto) {
        Team team = teamMapper.toTeam(teamDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.saveTeam(team));
    }

    @Operation(summary = "Update Team",
            description = "Update Team by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Team in db.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamDto.class))}),
            @ApiResponse(responseCode = "404", description = "Given ID of Team wasn't found.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Team name was empty.", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(
            @Parameter(description = "The ID for updating a Team.", required = true)
            @PathVariable long id, @RequestBody TeamDto teamDto) {
        teamDto.setId(id);
        Team team = teamMapper.toTeam(teamDto);
        return ResponseEntity.status(HttpStatus.OK).body(teamService.updateTeam(id, team));
    }
}
