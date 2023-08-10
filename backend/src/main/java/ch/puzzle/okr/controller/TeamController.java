package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.ObjectiveService;
import ch.puzzle.okr.service.RegisterNewUserService;
import ch.puzzle.okr.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/teams")
public class TeamController {

    private final TeamService teamService;
    private final TeamMapper teamMapper;
    private final ObjectiveService objectiveService;
    private final RegisterNewUserService registerNewUserService;
    private final ObjectiveMapper objectiveMapper;

    public TeamController(TeamService teamService, TeamMapper teamMapper, ObjectiveService objectiveService,
            RegisterNewUserService registerNewUserService, ObjectiveMapper objectiveMapper) {
        this.teamService = teamService;
        this.teamMapper = teamMapper;
        this.objectiveService = objectiveService;
        this.registerNewUserService = registerNewUserService;
        this.objectiveMapper = objectiveMapper;
    }

    @Operation(summary = "Get Teams", description = "Get all Teams from db.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned all Teams.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }), })
    @GetMapping
    public List<TeamDto> getAllTeams() {
        this.registerNewUserService.registerNewUser(SecurityContextHolder.getContext());
        return teamService.getAllTeams().stream().map(teamMapper::toDto).toList();
    }

    @Deprecated(forRemoval = true)
    @Operation(summary = "Get Objectives by Team", description = "Get a List of Objectives by Team Id", deprecated = true)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a List of Objectives associated to a Team with a specified ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find a Team with a specified ID.", content = @Content) })
    @GetMapping("/{id}/objectives")
    public ResponseEntity<List<ObjectiveDto>> getObjectivesByTeamId(
            @Parameter(description = "The ID of a Team to get a list of its Objectives.", required = true) @PathVariable long id) {
        this.registerNewUserService.registerNewUser(SecurityContextHolder.getContext());
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.objectiveService.getObjectivesByTeam(id).stream().map(objectiveMapper::toDto).toList());
    }

    @Deprecated(forRemoval = true)
    @Operation(summary = "Get Team", description = "Get a Team by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a Team with a specified ID.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find a Team with a specified ID.", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<TeamDto> getTeamById(
            @Parameter(description = "The ID for getting a Team.", required = true) @PathVariable Long id) {
        this.registerNewUserService.registerNewUser(SecurityContextHolder.getContext());
        return ResponseEntity.status(HttpStatus.OK).body(this.teamMapper.toDto(this.teamService.getTeamById(id)));
    }

    @Deprecated(forRemoval = true)
    @Operation(summary = "Create Team", description = "Create a new Team.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Team.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Team, not allowed to give an ID or missing attributes.", content = @Content) })
    @PostMapping
    public ResponseEntity<TeamDto> createTeam(@RequestBody TeamDto teamDto) {
        this.registerNewUserService.registerNewUser(SecurityContextHolder.getContext());
        Team team = teamMapper.toTeam(teamDto);
        TeamDto createdTeam = this.teamMapper.toDto(this.teamService.saveTeam(team));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdTeam);
    }

    @Deprecated(forRemoval = true)
    @Operation(summary = "Update Team", description = "Update Team by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Team in db.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Given ID of Team wasn't found.", content = @Content),
            @ApiResponse(responseCode = "400", description = "Team name was empty.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<TeamDto> updateTeam(
            @Parameter(description = "The ID for updating a Team.", required = true) @PathVariable long id,
            @RequestBody TeamDto teamDto) {
        this.registerNewUserService.registerNewUser(SecurityContextHolder.getContext());
        Team team = teamMapper.toTeam(teamDto);
        TeamDto createdTeam = this.teamMapper.toDto(this.teamService.updateTeam(team));
        return ResponseEntity.status(HttpStatus.OK).body(createdTeam);
    }

    @Deprecated(forRemoval = true)
    @Operation(summary = "Delete Team by Id", description = "Delete Team by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted team by Id"),
            @ApiResponse(responseCode = "404", description = "Did not find the team with requested id") })
    @DeleteMapping("/{id}")
    public void deleteTeamById(@PathVariable long id) {
        this.teamService.deleteTeamById(id);
    }
}
