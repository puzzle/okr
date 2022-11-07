package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.TeamService;
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

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all teams",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamDto.class))}),
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
                            schema = @Schema(implementation = TeamDto.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find a team with a specified ID.", content = @Content)
    })
    @GetMapping("/{id}")
    public TeamDto getTeamById(@PathVariable long id) {
        return teamMapper.toDto(teamService.getTeamById(id));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated team in db",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamDto.class))}),
            @ApiResponse(responseCode = "404", description = "Given id of team wasn't found", content = @Content),
            @ApiResponse(responseCode = "400", description = "Teamname was empty", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Team> updateTeam(@PathVariable long id, @RequestBody TeamDto teamDto) {
        Team team = teamMapper.toTeam(teamDto);
        return ResponseEntity.status(HttpStatus.OK).body(teamService.updateTeam(id, team));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved new team to db",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = TeamDto.class))}),
            @ApiResponse(responseCode = "400", description = "Can't create team with id or empty name or not allowed to pass id.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createTeam(@RequestBody TeamDto teamDto) {
        Team team = teamMapper.toTeam(teamDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(teamService.saveTeam(team));
    }

}
