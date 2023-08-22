package ch.puzzle.okr.controller.v2;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.ObjectiveService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/objectives")
public class ObjectiveControllerV2 {
    private final ObjectiveService objectiveService;
    private final ObjectiveMapper objectiveMapper;

    public ObjectiveControllerV2(ObjectiveService objectiveService, ObjectiveMapper objectiveMapper) {
        this.objectiveService = objectiveService;
        this.objectiveMapper = objectiveMapper;
    }

    @Operation(summary = "Get Objectives", description = "Get all Objectives from db")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned all Objectives", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }), })
    @GetMapping("/{id}")
    public List<ObjectiveDto> getAllObjectives(
            @Parameter(description = "The ID of an Objective.", required = true) @PathVariable Long id) {
        // Modify Method
        return objectiveService.getAllObjectives().stream().map(objectiveMapper::toDto).toList();
    }

    @Operation(summary = "Delete Objective by ID", description = "Delete Objective by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Objective by ID"),
            @ApiResponse(responseCode = "404", description = "Did not find the Objective with requested ID") })
    @DeleteMapping("/{id}")
    public void deleteObjectiveById(
            @Parameter(description = "The ID of an Objective to delete it.", required = true) @PathVariable long id) {
        this.objectiveService.deleteObjectiveById(id);
    }

    @Operation(summary = "Create Objective", description = "Create a new Objective")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Objective", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Objective, not allowed to give an ID", content = @Content) })
    @PostMapping
    public ResponseEntity<ObjectiveDto> createObjective(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Objective as json to create a new Objective.", required = true) @RequestBody ObjectiveDto objectiveDTO) {
        Objective objective = objectiveMapper.toObjective(objectiveDTO);
        ObjectiveDto createdObjective = this.objectiveMapper.toDto(this.objectiveService.saveObjective(objective));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdObjective);
    }

    @Operation(summary = "Update Objective", description = "Update Objective by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Objective in db", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "226", description = "Updated Objective in db but quarter was not changed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Objective, attributes are not set or tried to set quarter", content = @Content),
            @ApiResponse(responseCode = "404", description = "Given ID of Objective wasn't found", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<ObjectiveDto> updateObjective(
            @Parameter(description = "The ID for updating an Objective.", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The objective as json to update an existing Objective.", required = true) @RequestBody ObjectiveDto objectiveDTO) {
        Objective objective = this.objectiveMapper.toObjective(objectiveDTO);
        boolean isImUsed = objectiveService.isQuarterImmutable(objective);
        ObjectiveDto updatedObjective = this.objectiveMapper.toDto(this.objectiveService.updateObjective(objective));
        return isImUsed ? ResponseEntity.status(HttpStatus.IM_USED).body(updatedObjective)
                : ResponseEntity.status(HttpStatus.OK).body(updatedObjective);

    }
}
