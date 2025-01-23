package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.QuarterDto;
import ch.puzzle.okr.mapper.QuarterMapper;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.business.QuarterBusinessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/quarters")
public class QuarterController {

    private final QuarterMapper quarterMapper;

    private final QuarterBusinessService quarterBusinessService;

    public QuarterController(QuarterBusinessService quarterBusinessService) {
        this.quarterBusinessService = quarterBusinessService;
        this.quarterMapper = new QuarterMapper();
    }

    @Operation(summary = "Get quarters", description = "Get a List of quarters depending on current date")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned a List of quarters", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Quarter.class)) }) })
    @GetMapping("")
    public ResponseEntity<List<QuarterDto>> getCurrentQuarters() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(quarterMapper.toDtos(this.quarterBusinessService.getQuarters()));
    }

    @Operation(summary = "Get current quarter", description = "Get the current quarter depending on current date")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned the current quarter", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = Quarter.class)) }) })
    @GetMapping("/current")
    public ResponseEntity<QuarterDto> getCurrentQuarter() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(quarterMapper.toDto(this.quarterBusinessService.getCurrentQuarter()));
    }
}
