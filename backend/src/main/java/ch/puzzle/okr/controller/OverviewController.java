package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.ObjectiveService;
import ch.puzzle.okr.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/overview")
public class OverviewController {
    private TeamService teamService;
    private OverviewMapper overviewMapper;

    public OverviewController(TeamService teamService, OverviewMapper overviewMapper) {
        this.teamService = teamService;
        this.overviewMapper = overviewMapper;
    }

    @Operation(summary = "Get all teams and their objectives", description = "Get a List of teams with their objectives")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a List of teams and their objectives", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OverviewDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't return list of teams with their objectives", content = @Content) })
    @GetMapping("")
    public ResponseEntity<List<OverviewDto>> getOverview() {
        return  ResponseEntity.status(HttpStatus.OK)
                .body(teamService.getAllTeams().stream().map(overviewMapper::toDto).toList());
    }

}
