package ch.puzzle.okr.controller;

import ch.puzzle.okr.models.Completed;
import ch.puzzle.okr.service.business.CompletedBusinessService;
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

    private final CompletedBusinessService completedBusinessService;

    public CompletedController(CompletedBusinessService completedBusinessService) {
        this.completedBusinessService = completedBusinessService;
    }

    @Operation(summary = "Create Completed", description = "Create a new Completed Reference.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Completed.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = Completed.class)) }),
            @ApiResponse(responseCode = "404", description = "Could not create Completed Reference", content = @Content) })
    @PostMapping
    public ResponseEntity<Completed> createCompleted(@RequestBody Completed completed) {
        Completed createdCompleted = completedBusinessService.createCompleted(completed);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCompleted);
    }

    @Operation(summary = "Delete Completed by Id", description = "Delete Completed Reference by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Completed by Id"),
            @ApiResponse(responseCode = "404", description = "Did not find the Completed with requested id") })
    @DeleteMapping("/{id}")
    public void deleteCompletedById(@PathVariable long id) {
        completedBusinessService.deleteCompletedById(id);
    }
}
