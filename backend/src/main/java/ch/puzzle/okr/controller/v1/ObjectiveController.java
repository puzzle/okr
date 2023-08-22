package ch.puzzle.okr.controller.v1;

import ch.puzzle.okr.dto.KeyResultMeasureDto;
import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
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
@Deprecated
public class ObjectiveController {

    private final ObjectiveBusinessService objectiveBusinessService;
    private final ObjectiveMapper objectiveMapper;
    private final KeyResultBusinessService keyResultBusinessService;

    public ObjectiveController(ObjectiveBusinessService objectiveBusinessService, ObjectiveMapper objectiveMapper,
            KeyResultBusinessService keyResultBusinessService) {
        this.objectiveBusinessService = objectiveBusinessService;
        this.objectiveMapper = objectiveMapper;
        this.keyResultBusinessService = keyResultBusinessService;
    }

    @Operation(summary = "Get Objectives", description = "Get all Objectives from db.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned all Objectives.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }), })
    @GetMapping
    public List<ObjectiveDto> getAllObjectives() {
        return objectiveBusinessService.getAllObjectives().stream().map(objectiveMapper::toDto).toList();
    }

    @Operation(summary = "Get Objective", description = "Get an Objective by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned an Objective with a specified ID.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective with a specified ID.", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<ObjectiveDto> getObjective(
            @Parameter(description = "The ID for getting an Objective.", required = true) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.objectiveMapper.toDto(objectiveBusinessService.getObjectiveById(id)));
    }

    @Operation(summary = "Create Objective", description = "Create a new Objective.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Objective.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Objective, not allowed to give an ID.", content = @Content) })
    @PostMapping
    public ResponseEntity<ObjectiveDto> createObjective(@RequestBody ObjectiveDto objectiveDTO) {
        Objective objective = objectiveMapper.toObjective(objectiveDTO);
        ObjectiveDto createdObjective = this.objectiveMapper
                .toDto(this.objectiveBusinessService.saveObjective(objective));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdObjective);
    }

    @Operation(summary = "Update Objective", description = "Update Objective by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Objective in db.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "226", description = "Updated Objective in db but quarter was not changed.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Objective, attributes are not set or tried to set quarter.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Given ID of Objective wasn't found.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<ObjectiveDto> updateObjective(
            @Parameter(description = "The ID for updating an Objective.", required = true) @PathVariable Long id,
            @RequestBody ObjectiveDto objectiveDTO) {
        Objective objective = this.objectiveMapper.toObjective(objectiveDTO);
        boolean isImUsed = objectiveBusinessService.isQuarterImmutable(objective);
        ObjectiveDto updatedObjective = this.objectiveMapper
                .toDto(this.objectiveBusinessService.updateObjective(objective));
        return isImUsed ? ResponseEntity.status(HttpStatus.IM_USED).body(updatedObjective)
                : ResponseEntity.status(HttpStatus.OK).body(updatedObjective);

    }

    @Operation(summary = "Get KeyResults from Objective", description = "Get all KeyResults from Objective by objectiveID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all KeyResults from Objective.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = KeyResultMeasureDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective with a specified ID to get KeyResults from.", content = @Content) })
    @GetMapping("{id}/keyresults")
    public List<KeyResultMeasureDto> getAllKeyResultsByObjective(
            @Parameter(description = "The ID for getting all KeyResults from an Objective.", required = true) @PathVariable Long id) {
        return keyResultBusinessService.getAllKeyResultsByObjectiveWithMeasure(id);
    }

    @Operation(summary = "Delete Objective by Id", description = "Delete Objective by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted objective by Id"),
            @ApiResponse(responseCode = "404", description = "Did not find the objective with requested id") })
    @DeleteMapping("/{id}")
    public void deleteObjectiveById(@PathVariable long id) {
        this.objectiveBusinessService.deleteObjectiveById(id);
    }
}
