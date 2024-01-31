package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.alignment.AlignmentObjectiveDto;
import ch.puzzle.okr.dto.overview.DashboardDto;
import ch.puzzle.okr.mapper.AlignmentSelectionMapper;
import ch.puzzle.okr.mapper.DashboardMapper;
import ch.puzzle.okr.mapper.OverviewMapper;
import ch.puzzle.okr.models.alignment.AlignmentView;
import ch.puzzle.okr.service.authorization.OverviewAuthorizationService;
import ch.puzzle.okr.service.business.AlignmentSelectionBusinessService;
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
@RequestMapping("api/v2/alignments")
public class AlignmentController {
    private final AlignmentSelectionMapper alignmentSelectionMapper;
    private final OverviewMapper overviewMapper;
    private final DashboardMapper dashboardMapper;

    private final AlignmentSelectionBusinessService alignmentSelectionBusinessService;
    private final OverviewAuthorizationService overviewAuthorizationService;

    public AlignmentController(OverviewMapper overviewMapper, AlignmentSelectionMapper alignmentSelectionMapper,
            AlignmentSelectionBusinessService alignmentSelectionBusinessService,
            OverviewAuthorizationService overviewAuthorizationService, DashboardMapper dashboardMapper) {
        this.alignmentSelectionMapper = alignmentSelectionMapper;
        this.dashboardMapper = dashboardMapper;
        this.overviewMapper = overviewMapper;

        this.alignmentSelectionBusinessService = alignmentSelectionBusinessService;
        this.overviewAuthorizationService = overviewAuthorizationService;

    }

    @Operation(summary = "Get all objectives and their key results to select the alignment", description = "Get a list of objectives with their key results to select the alignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a list of objectives with their key results to select the alignment", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlignmentObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't return list of objectives with their key results to select the alignment", content = @Content) })
    @GetMapping("/selections")
    public ResponseEntity<List<AlignmentObjectiveDto>> getAlignmentSelections(
            @RequestParam(required = false, defaultValue = "", name = "quarter") Long quarterFilter,
            @RequestParam(required = false, defaultValue = "", name = "team") Long teamFilter) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(alignmentSelectionMapper.toDto(alignmentSelectionBusinessService
                        .getAlignmentSelectionByQuarterIdAndTeamIdNot(quarterFilter, teamFilter)));
    }

    @Operation(summary = "Get all objectives with their aligned keyresult or objective for alignment", description = "Get all objectives with their aligned keyresult or objective for alignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a list of objectives with their aligned objectives or key results for the alignment", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlignmentView.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't return list of objectives with their aligned key results or objectives for the alignment", content = @Content) })
    @GetMapping("/filtered")
    public ResponseEntity<List<AlignmentView>> getAlignmentViews(
            @RequestParam(required = false, defaultValue = "", name = "quarter") Long quarterFilter,
            @RequestParam(required = false, defaultValue = "", name = "teamIds") List<Long> teamIds) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(alignmentSelectionBusinessService.getAlignmentViewByQuarterId(quarterFilter, teamIds));
    }

    @Operation(summary = "Get all objectives and their key results to select the alignment", description = "Get a list of objectives with their key results to select the alignment")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a list of objectives with their key results to select the alignment", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlignmentObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't return list of objectives with their key results to select the alignment", content = @Content) })
    @GetMapping("/all")
    public ResponseEntity<DashboardDto> getOverviewForAlignment(
            @RequestParam(required = false, defaultValue = "", name = "team") List<Long> teamFilter,
            @RequestParam(required = false, defaultValue = "", name = "quarter") Long quarterFilter,
            @RequestParam(required = false, defaultValue = "", name = "objectiveQuery") String objectiveQuery) {
        boolean hasWriteAllAccess = overviewAuthorizationService.hasWriteAllAccess();
        return ResponseEntity.status(HttpStatus.OK)
                .body(dashboardMapper.toDto(overviewMapper.toDto(
                        overviewAuthorizationService.getFilteredOverview(quarterFilter, teamFilter, objectiveQuery)),
                        hasWriteAllAccess));
    }
}
