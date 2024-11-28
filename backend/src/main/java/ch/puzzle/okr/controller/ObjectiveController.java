package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.DuplicateObjectiveDto;
import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.mapper.keyresult.KeyResultMapper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.authorization.ActionAuthorizationService;
import ch.puzzle.okr.service.authorization.ObjectiveAuthorizationService;
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

import static org.springframework.http.HttpStatus.IM_USED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("api/v2/objectives")
public class ObjectiveController {
    private final ObjectiveAuthorizationService objectiveAuthorizationService;
    private final ObjectiveMapper objectiveMapper;
    private final KeyResultMapper keyResultMapper;
    private final ActionAuthorizationService actionAuthorizationService;

    public ObjectiveController(ObjectiveAuthorizationService objectiveAuthorizationService,
            ObjectiveMapper objectiveMapper, KeyResultMapper keyResultMapper,
            ActionAuthorizationService actionAuthorizationService) {
        this.objectiveAuthorizationService = objectiveAuthorizationService;
        this.objectiveMapper = objectiveMapper;
        this.keyResultMapper = keyResultMapper;
        this.actionAuthorizationService = actionAuthorizationService;
    }

    @Operation(summary = "Get Objective", description = "Get an Objective by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned an Objective with a specified ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to read an Objective", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective with a specified ID", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<ObjectiveDto> getObjective(
            @Parameter(description = "The ID for getting an Objective.", required = true) @PathVariable Long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(objectiveMapper.toDto(objectiveAuthorizationService.getEntityById(id)));
    }

    @Operation(summary = "Get KeyResults from Objective", description = "Get all KeyResults from one Objective by ObjectiveId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all KeyResult from Objective.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CheckInDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to read KeyResult from a Objective", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find a Objective with a specified ID to get KeyResults from.", content = @Content) })
    @GetMapping("/{id}/keyResults")
    public ResponseEntity<List<KeyResultDto>> getKeyResultsFromObjective(
            @Parameter(description = "The ID for getting all Check-ins from a KeyResult.", required = true) @PathVariable long id) {
        return ResponseEntity.status(OK)
                .body(objectiveAuthorizationService.getAllKeyResultsByObjective(id).stream().map(keyResult -> {
                    List<Action> actionList = actionAuthorizationService.getActionsByKeyResult(keyResult);
                    return keyResultMapper.toDto(keyResult, actionList);
                }).toList());
    }

    @Operation(summary = "Delete Objective by ID", description = "Delete Objective by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Objective by ID"),
            @ApiResponse(responseCode = "401", description = "Not authorized to delete an Objective", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the Objective with requested ID") })
    @DeleteMapping("/{id}")
    public void deleteObjectiveById(
            @Parameter(description = "The ID of an Objective to delete it.", required = true) @PathVariable long id) {
        objectiveAuthorizationService.deleteEntityById(id);
    }

    @Operation(summary = "Create Objective", description = "Create a new Objective")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Objective", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Objective, not allowed to give an ID", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized to create an Objective", content = @Content) })
    @PostMapping
    public ResponseEntity<ObjectiveDto> createObjective(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Objective as json to create a new Objective.", required = true) @RequestBody ObjectiveDto objectiveDTO) {
        Objective objective = objectiveMapper.toObjective(objectiveDTO);
        ObjectiveDto createdObjective = objectiveMapper.toDto(objectiveAuthorizationService.createEntity(objective));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdObjective);
    }

    @Operation(summary = "Duplicate Objective", description = "Duplicate a given Objective")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "Duplicated a given Objective", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }) })
    @PostMapping("/{id}")
    public ResponseEntity<ObjectiveDto> duplicateObjective(
            @Parameter(description = "The ID for duplicating an Objective.", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Objective which should be duplicated as json", required = true) @RequestBody DuplicateObjectiveDto duplicateObjectiveDto) {
        Objective objective = objectiveMapper.toObjective(duplicateObjectiveDto.objective());
        List<KeyResult> keyResults = duplicateObjectiveDto.keyResults().stream().map(keyResultMapper::toKeyResult)
                .toList();
        ObjectiveDto duplicatedObjectiveDto = objectiveMapper
                .toDto(objectiveAuthorizationService.duplicateEntity(id, objective, keyResults));
        return ResponseEntity.status(HttpStatus.CREATED).body(duplicatedObjectiveDto);
    }

    @Operation(summary = "Update Objective", description = "Update Objective by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Objective in db", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "226", description = "Updated Objective in db but quarter was not changed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Objective, attributes are not set or tried to set quarter", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized to update an Objective", content = @Content),
            @ApiResponse(responseCode = "404", description = "Given ID of Objective wasn't found", content = @Content),
            @ApiResponse(responseCode = "422", description = "Can't update Objective since Objective was updated or deleted by another user.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<ObjectiveDto> updateObjective(
            @Parameter(description = "The ID for updating an Objective.", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The objective as json to update an existing Objective.", required = true) @RequestBody ObjectiveDto objectiveDTO) {
        Objective objective = objectiveMapper.toObjective(objectiveDTO);
        boolean isObjectiveImUsed = objectiveAuthorizationService.isImUsed(objective);
        ObjectiveDto updatedObjective = objectiveMapper
                .toDto(objectiveAuthorizationService.updateEntity(id, objective));
        return ResponseEntity.status(isObjectiveImUsed ? IM_USED : OK).body(updatedObjective);
    }
}
