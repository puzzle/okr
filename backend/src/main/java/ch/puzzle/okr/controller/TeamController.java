package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.dto.UserDto;
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

    @Operation(summary = "Get Teams", description = "Get all Teams from db")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned all Teams", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }), })
    @GetMapping
    public List<TeamDto> getAllTeams() {
        return teamAuthorizationService.getAllTeams().stream().map(teamMapper::toDto).toList();
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
        return ResponseEntity.status(HttpStatus.CREATED).body(teamMapper.toDto(createdTeam));
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
        return ResponseEntity.status(OK).body(teamMapper.toDto(updatedTeam));
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

    @Operation(summary = "Add users to a team", description = "Add users to a team")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Added users to team"),
            @ApiResponse(responseCode = "401", description = "Not authorized to add users to the team", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the Team with requested ID") })
    @PutMapping("/{id}/addusers")
    public void addUsersToTeam(
            @Parameter(description = "The ID of an Team to delete it.", required = true) @PathVariable long id,
            @RequestBody List<UserDto> userDtoList) {
        var userIds = userDtoList.stream().map(UserDto::id).toList();
        teamAuthorizationService.addUsersToTeam(id, userIds);
    }

    @Operation(summary = "Remove User from Team", description = "Remove User with given UserID from Team")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Removed User from Team"),
            @ApiResponse(responseCode = "401", description = "Not authorized to remove user from team", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the Team with requested ID") })
    @PutMapping("/{id}/user/{userId}/removeuser")
    public void removeUserFromTeam(
            @Parameter(description = "The ID of an team to remove the user from it.", required = true) @PathVariable long id,
            @Parameter(description = "The User ID to remove from the team.", required = true) @PathVariable long userId) {
        teamAuthorizationService.removeUserFromTeam(id, userId);
    }

    @Operation(summary = "Update or add team membership", description = "If user is already member of this team, isAdmin is set. otherwise new team membership "
            + "is added with isAdmin true or false")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Update or add team membership"),
            @ApiResponse(responseCode = "401", description = "Not authorized to update or add team membership", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the Team with requested ID") })
    @PutMapping("/{id}/user/{userId}/updateaddteammembership/{isAdmin}")
    public void updateOrAddTeamMembership(
            @Parameter(description = "The ID of an team to update or add membership", required = true) @PathVariable long id,
            @Parameter(description = "The User ID to update or add membership", required = true) @PathVariable long userId,
            @Parameter(description = "The parameter if user should be admin or not", required = true) @PathVariable boolean isAdmin) {
        teamAuthorizationService.updateOrAddTeamMembership(id, userId, isAdmin);
    }
}
