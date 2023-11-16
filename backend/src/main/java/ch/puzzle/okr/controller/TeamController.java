package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.authorization.TeamAuthorizationService;
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

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v2/teams")
public class TeamController {
    private final TeamAuthorizationService teamAuthorizationService;
    private final TeamMapper teamMapper;

    public TeamController(TeamAuthorizationService teamAuthorizationService, TeamMapper teamMapper) {
        this.teamAuthorizationService = teamAuthorizationService;
        this.teamMapper = teamMapper;
    }

    @Operation(summary = "Get Teams", description = "Get all Teams from db as well as all active objectives from chosen quarter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Teams with active objective in quarter", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }), })
    @GetMapping
    public List<TeamDto> getAllTeams(@RequestParam(value = "quarterId", required = false) Long quarterId) {
        return teamAuthorizationService.getEntities().stream().map(team -> teamMapper.toDto(team, quarterId)).toList();
    }

    @Operation(summary = "Create Team", description = "Create a new Team")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Team", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Team, not allowed to give an ID", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized to create a Team", content = @Content) })
    @PostMapping
    public ResponseEntity<TeamDto> createTeam(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Team as json to create a new Team.", required = true) @RequestBody TeamDto teamDto) {
        Team createdTeam = teamAuthorizationService.createEntity(teamMapper.toTeam(teamDto));
        return ResponseEntity.status(HttpStatus.CREATED).body(teamMapper.toDto(createdTeam, null));
    }

    @Operation(summary = "Update Team", description = "Update a Team by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Team in db.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(allOf = { TeamDto.class })) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to update a Team", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find a Team with a specified ID to update.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Can't update Team since Team was updated or deleted by another user.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> updateTeam(
            @Parameter(description = "The ID for updating a Team.", required = true) @PathVariable long id,
            @RequestBody TeamDto teamDto) {
        Team updatedTeam = teamAuthorizationService.updateEntity(teamMapper.toTeam(teamDto), id);
        return ResponseEntity.status(OK).body(teamMapper.toDto(updatedTeam, null));
    }

    @Operation(summary = "Delete Team by ID", description = "Delete Team by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Team by ID"),
            @ApiResponse(responseCode = "401", description = "Not authorized to delete an Team", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the Team with requested ID") })
    @DeleteMapping("/{id}")
    public void deleteTeamById(
            @Parameter(description = "The ID of an Team to delete it.", required = true) @PathVariable long id) {
        teamAuthorizationService.deleteEntity(id);
    }
}
