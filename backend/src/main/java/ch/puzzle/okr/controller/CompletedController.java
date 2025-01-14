package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.CompletedDto;
import ch.puzzle.okr.mapper.CompletedMapper;
import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.service.authorization.CompletedAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/completed")
public class CompletedController {

    private final CompletedAuthorizationService completedAuthorizationService;
    private final CompletedMapper completedMapper;

    public CompletedController(CompletedAuthorizationService completedAuthorizationService,
                               CompletedMapper completedMapper) {
        this.completedAuthorizationService = completedAuthorizationService;
        this.completedMapper = completedMapper;
    }

    @Operation(summary = "Create Completed", description = "Create a new Completed Reference.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Completed.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Completed.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to create Completed Reference", content = @Content),
            @ApiResponse(responseCode = "404", description = "Could not create Completed Reference", content = @Content) })
    @PostMapping
    public ResponseEntity<CompletedDto> createCompleted(@RequestBody CompletedDto completedDto) {
        Completed completed = completedMapper.toCompleted(completedDto);
        Completed createdCompleted = completedAuthorizationService.createCompleted(completed);
        return ResponseEntity.status(HttpStatus.CREATED).body(completedMapper.toDto(createdCompleted));
    }

    @Operation(summary = "Delete Completed by Objective Id", description = "Delete Completed Reference by Objective Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Completed by Objective Id"),
            @ApiResponse(responseCode = "401", description = "Not authorized to delete Completed Reference", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the Completed with requested Objective id") })
    @DeleteMapping("/{objectiveId}")
    public void deleteCompletedByObjectiveId(@PathVariable long objectiveId) {
        completedAuthorizationService.deleteCompletedByObjectiveId(objectiveId);
    }

    @Operation(summary = "Get Completed by Objective Id", description = "Get Completed from one Objective by objectiveId.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned Completed by Objective Id"),
            @ApiResponse(responseCode = "401", description = "Not authorized to get Completed Reference", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the Completed with requested Objective id") })
    @GetMapping("/{objectiveId}")
    public CompletedDto getCompletedByObjectiveId(@PathVariable long objectiveId) {
        Completed completedByObjectiveId = completedAuthorizationService.getCompletedByObjectiveId(objectiveId);
        return completedMapper.toDto(completedByObjectiveId);
    }
}
