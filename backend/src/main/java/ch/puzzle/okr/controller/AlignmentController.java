package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.alignment.AlignmentObjectiveDto;
import ch.puzzle.okr.service.AlignmentSelectionService;
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
    private final AlignmentSelectionService alignmentSelectionService;

    public AlignmentController(AlignmentSelectionService alignmentSelectionService) {
        this.alignmentSelectionService = alignmentSelectionService;
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
        return ResponseEntity.status(HttpStatus.OK).body(
                alignmentSelectionService.getAlignmentSelectionByQuarterIdAndTeamIdNot(quarterFilter, teamFilter));
    }
}
