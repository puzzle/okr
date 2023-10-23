package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.TeamMapper;
import ch.puzzle.okr.service.business.OrganisationBusinessService;
import ch.puzzle.okr.service.business.TeamBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v2/teams")
public class TeamController {
    private final TeamBusinessService teamBusinessService;
    private final TeamMapper teamMapper;

    private final OrganisationBusinessService service;

    public TeamController(TeamBusinessService teamBusinessService, TeamMapper teamMapper,
            OrganisationBusinessService service) {
        this.teamBusinessService = teamBusinessService;
        this.teamMapper = teamMapper;
        this.service = service;
    }

    @Operation(summary = "Get Teams", description = "Get all Teams from db as well as all active objectives from chosen quarter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Teams with active objective in quarter", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }), })
    @GetMapping
    public List<TeamDto> getAllTeams(@RequestParam(value = "quarterId", required = false) Long quarterId) {
        service.importOrgFromLDAP();
        return teamBusinessService.getAllTeams().stream().map(team -> teamMapper.toDto(team, quarterId)).toList();
    }
}
