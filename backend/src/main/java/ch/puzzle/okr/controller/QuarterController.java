package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.TeamDto;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.service.business.QuarterService;
import ch.puzzle.okr.service.RegisterNewUserService;
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
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/quarters")
public class QuarterController {

    private final QuarterService quarterService;
    private final RegisterNewUserService registerNewUserService;

    public QuarterController(QuarterService quarterService, RegisterNewUserService registerNewUserService) {
        this.quarterService = quarterService;
        this.registerNewUserService = registerNewUserService;
    }

    @Operation(summary = "Get quarters by Team", description = "Get a List of quarters for current date")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a List of current quarters", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = TeamDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create quarters", content = @Content) })
    @GetMapping("")
    public ResponseEntity<List<Quarter>> getCurrentQuarters() {
        this.registerNewUserService.registerNewUser(SecurityContextHolder.getContext());
        return ResponseEntity.status(HttpStatus.OK).body(this.quarterService.getOrCreateQuarters());
    }

    @Operation(summary = "Get start and end date of quarter by keyResultId", description = "Get start and end date of quarter by keyResultId")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returns a object containing the start date and the end date of quarter", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StartEndDateDTO.class)) }),
            @ApiResponse(responseCode = "404", description = "Could not find given keyresult", content = @Content),
            @ApiResponse(responseCode = "422", description = "Label is invalid", content = @Content) })
    @GetMapping("/dates/{keyResultId}")
    public StartEndDateDTO getStartAndEndDateOfKeyResult(@PathVariable long keyResultId) {
        return this.quarterService.getStartAndEndDateOfKeyResult(keyResultId);
    }
}
