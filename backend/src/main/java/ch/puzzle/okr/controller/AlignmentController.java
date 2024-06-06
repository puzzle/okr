package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.alignment.AlignmentLists;
import ch.puzzle.okr.service.business.AlignmentBusinessService;
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
    private final AlignmentBusinessService alignmentBusinessService;

    public AlignmentController(AlignmentBusinessService alignmentBusinessService) {
        this.alignmentBusinessService = alignmentBusinessService;
    }

    @Operation(summary = "Get AlignmentLists from filter", description = "Get a list of AlignmentObjects with all AlignmentConnections, which match current quarter, team and objective filter")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned AlignmentLists, which match current filters", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AlignmentLists.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't generate AlignmentLists from current filters", content = @Content) })
    @GetMapping("/alignmentLists")
    public ResponseEntity<AlignmentLists> getAlignments(
            @RequestParam(required = false, defaultValue = "", name = "teamFilter") List<Long> teamFilter,
            @RequestParam(required = false, defaultValue = "", name = "quarterFilter") Long quarterFilter,
            @RequestParam(required = false, defaultValue = "", name = "objectiveQuery") String objectiveQuery) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(alignmentBusinessService.getAlignmentListsByFilters(quarterFilter, teamFilter, objectiveQuery));
    }
}
