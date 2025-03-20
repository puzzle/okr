package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.mapper.EvaluationViewMapper;
import ch.puzzle.okr.service.business.EvaluationViewBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/evaluation")
public class EvaluationViewController {

    private final EvaluationViewMapper evaluationViewMapper;
    private final EvaluationViewBusinessService evaluationViewBusinessService;

    public EvaluationViewController(EvaluationViewMapper evaluationViewMapper,
                                    EvaluationViewBusinessService evaluationViewBusinessService) {
        this.evaluationViewMapper = evaluationViewMapper;
        this.evaluationViewBusinessService = evaluationViewBusinessService;
    }

    @Operation(summary = "Get statistics data for multiple teams in a specific quarter", description = "Get a List of teams with their objectives")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned the statistics data for the requested teams and quarter", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = EvaluationDto.class)) }),
            @ApiResponse(responseCode = "404", description = "The quarter or one of the teams were not found", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not Authorized", content = @Content) })
    @GetMapping("")
    public ResponseEntity<EvaluationDto> getOverview(@RequestParam(name = "team")
    @Parameter(description = "List of Team ids the statistics are requested for") List<Long> teamIds,
                                                     @RequestParam(name = "quarter")
                                                     @Parameter(description = "Quarter id the statistics are requested for ") Long quarterId) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(evaluationViewMapper
                        .toDto(evaluationViewBusinessService
                                .findByIds(evaluationViewMapper.fromDto(teamIds, quarterId))));
    }
}
