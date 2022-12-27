package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.OverviewDto;
import ch.puzzle.okr.service.OverviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/overview")
public class OverviewController {
    private final OverviewService overviewService;

    public OverviewController(OverviewService overviewService) {
        this.overviewService = overviewService;
    }

    @Operation(summary = "Get all teams and their objectives", description = "Get a List of teams with their objectives")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a List of teams and their objectives", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OverviewDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't return list of teams with their objectives", content = @Content) })
    @GetMapping("")
    public ResponseEntity<List<OverviewDto>> getOverview(
            @RequestParam(required = false, defaultValue = "", name = "team") List<Long> teamFilter,
            @RequestParam(required = false, defaultValue = "", name = "quarter") Long quarterFilter) {
        return ResponseEntity.status(HttpStatus.OK).body(overviewService.getOverview(teamFilter, quarterFilter));
    }

}
