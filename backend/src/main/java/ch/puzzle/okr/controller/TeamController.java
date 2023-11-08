package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.authorization.TeamAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
        return ResponseEntity.status(HttpStatus.OK).body(teamMapper.toDto(createdTeam, null));
    }
}
