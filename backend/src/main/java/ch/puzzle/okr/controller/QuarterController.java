package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.business.QuarterBusinessService;
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

    private final QuarterBusinessService quarterBusinessService;

    public QuarterController(QuarterBusinessService quarterBusinessService) {
        this.quarterBusinessService = quarterBusinessService;
    }

    @Operation(summary = "Get quarters", description = "Get a List of quarters depending on current date")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned a List of quarters", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }) })
    @GetMapping("")
    public ResponseEntity<List<Quarter>> getCurrentQuarters() {
        return ResponseEntity.status(HttpStatus.OK).body(this.quarterBusinessService.getQuarters());
    }
}
