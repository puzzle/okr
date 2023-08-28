package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.service.RegisterNewUserService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v2/teams")
public class TeamController {
    private final TeamBusinessService teamBusinessService;
    private final TeamMapper teamMapper;
    private final RegisterNewUserService registerNewUserService;

    public TeamController(TeamBusinessService teamBusinessService, TeamMapper teamMapper,
            RegisterNewUserService registerNewUserService) {
        this.teamBusinessService = teamBusinessService;
        this.teamMapper = teamMapper;
        this.registerNewUserService = registerNewUserService;
    }

    @Operation(summary = "Get Teams", description = "Get all Teams from db as well as all active objectives from chosen quarter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Teams with active objective in quarter", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }), })
    @GetMapping("/{quarterId}")
    public List<TeamDto> getAllTeams(
            @Parameter(description = "The ID of a Team to get a list of its Objectives.", required = false) @PathVariable Long quarterId) {
        this.registerNewUserService.registerNewUser(SecurityContextHolder.getContext());
        return teamBusinessService.getAllTeams().stream().map(team -> teamMapper.toDto(team, quarterId)).toList();
    }
}
