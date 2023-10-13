package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.overview.OverviewDto;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.service.RegisterNewUserService;
import ch.puzzle.okr.service.business.OverviewBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v2/overview")
public class OverviewController {
    private final OverviewMapper overviewMapper;
    private final OverviewBusinessService overviewBusinessService;
    private final RegisterNewUserService registerNewUserService;

    public OverviewController(OverviewMapper overviewMapper, OverviewBusinessService overviewBusinessService,
            RegisterNewUserService registerNewUserService) {
        this.overviewMapper = overviewMapper;
        this.overviewBusinessService = overviewBusinessService;
        this.registerNewUserService = registerNewUserService;
    }

    @Operation(summary = "Get all teams and their objectives", description = "Get a List of teams with their objectives")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a List of teams and their objectives", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OverviewDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't return list of teams with their objectives", content = @Content),
            @ApiResponse(responseCode = "404", description = "The quarter or one of the teams were not found", content = @Content) })
    @GetMapping("")
    public ResponseEntity<List<OverviewDto>> getOverview(
            @RequestParam(required = false, defaultValue = "", name = "team") List<Long> teamFilter,
            @RequestParam(required = false, defaultValue = "", name = "quarter") Long quarterFilter) {
        registerNewUserService.registerNewUser(SecurityContextHolder.getContext());
        return ResponseEntity.status(HttpStatus.OK).body(overviewMapper
                .toDto(overviewBusinessService.getOverviewByQuarterIdAndTeamIds(quarterFilter, teamFilter)));
    }
}
