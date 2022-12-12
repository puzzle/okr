package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.KeyResultService;
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
@RequestMapping("api/v1/objectives")
public class ObjectiveController {

    private final ObjectiveService objectiveService;
    private final ObjectiveMapper objectiveMapper;
    private final KeyResultService keyResultService;

    public ObjectiveController(ObjectiveService objectiveService, ObjectiveMapper objectiveMapper,
            KeyResultService keyResultService) {
        this.objectiveService = objectiveService;
        this.objectiveMapper = objectiveMapper;
        this.keyResultService = keyResultService;
    }

    @Operation(summary = "Get Objectives", description = "Get all Objectives from db.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned all Objectives.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }), })
    @GetMapping
    public List<ObjectiveDto> getAllObjectives() {
        return objectiveService.getAllObjectives().stream().map(objectiveMapper::toDto).toList();
    }

    @Operation(summary = "Get Objective", description = "Get an Objective by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned an Objective with a specified ID.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective with a specified ID.", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<ObjectiveDto> getObjective(
            @Parameter(description = "The ID for getting an Objective.", required = true) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK).body(this.objectiveMapper.toDto(objectiveService.getObjective(id)));
    }

    @Operation(summary = "Create Objective", description = "Create a new Objective.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Objective.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Objective, not allowed to give an ID.", content = @Content) })
    @PostMapping
    public ResponseEntity<ObjectiveDto> createObjective(@RequestBody ObjectiveDto objectiveDTO) {
        Objective objective = objectiveMapper.toObjective(objectiveDTO);
        ObjectiveDto createdObjective = this.objectiveMapper.toDto(this.objectiveService.saveObjective(objective));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdObjective);
    }

    @Operation(summary = "Update Objective", description = "Update Objective by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Objective in db.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Objective, attributes are not set.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Given ID of Objective wasn't found.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<ObjectiveDto> updateObjective(
            @Parameter(description = "The ID for updating an Objective.", required = true) @PathVariable Long id,
            @RequestBody ObjectiveDto objectiveDTO) {
        objectiveDTO.setId(id);
        Objective objective = this.objectiveMapper.toObjective(objectiveDTO);
        ObjectiveDto updatedObjective = this.objectiveMapper
                .toDto(this.objectiveService.updateObjective(id, objective));
        return ResponseEntity.status(HttpStatus.OK).body(updatedObjective);
    }

    @Operation(summary = "Get KeyResults from Objective", description = "Get all KeyResults from Objective by objectiveID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all KeyResults from Objective.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = KeyResultMeasureDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective with a specified ID to get KeyResults from.", content = @Content) })
    @GetMapping("{id}/keyresults")
    public List<KeyResultMeasureDto> getAllKeyResultsByObjective(
            @Parameter(description = "The ID for getting all KeyResults from an Objective.", required = true) @PathVariable Long id) {
        return keyResultService.getAllKeyResultsByObjectiveWithMeasure(id);
    }
}
