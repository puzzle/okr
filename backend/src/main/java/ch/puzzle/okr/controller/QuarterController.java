package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.QuarterService;
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
@RequestMapping("api/v1/quarters")
public class QuarterController {

    private final QuarterService quarterService;

    public QuarterController(QuarterService quarterService) {
        this.quarterService = quarterService;
    }

    @Operation(summary = "Get quarters by Team", description = "Get a List of quarters for current date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a List of current quarters", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Could not find or create quarters", content = @Content) })
    @GetMapping("")
    public ResponseEntity<List<Quarter>> getCurrentQuarters() {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.quarterService.getCurrentQuarters());
    }
}
