package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.authorization.ObjectiveAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/objectives")
public class ObjectiveController {
    private final ObjectiveAuthorizationService objectiveAuthorizationService;
    private final ObjectiveMapper objectiveMapper;

    public ObjectiveController(ObjectiveAuthorizationService objectiveAuthorizationService,
            ObjectiveMapper objectiveMapper) {
        this.objectiveAuthorizationService = objectiveAuthorizationService;
        this.objectiveMapper = objectiveMapper;
    }

    @Operation(summary = "Get Objective", description = "Get an Objective by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned an Objective with a specified ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective with a specified ID", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<ObjectiveDto> getObjective(
            @Parameter(description = "The ID for getting an Objective.", required = true) @PathVariable Long id,
            @AuthenticationPrincipal Jwt token) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(this.objectiveMapper.toDto(objectiveAuthorizationService.getEntityById(id, token)));
    }

    @Operation(summary = "Delete Objective by ID", description = "Delete Objective by ID")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Objective by ID"),
            @ApiResponse(responseCode = "404", description = "Did not find the Objective with requested ID") })
    @DeleteMapping("/{id}")
    public void deleteObjectiveById(
            @Parameter(description = "The ID of an Objective to delete it.", required = true) @PathVariable long id,
            @AuthenticationPrincipal Jwt token) {
        this.objectiveAuthorizationService.deleteEntityById(id, token);
    }

    @Operation(summary = "Create Objective", description = "Create a new Objective")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Objective", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ObjectiveDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Objective, not allowed to give an ID", content = @Content) })
    @PostMapping
    public ResponseEntity<ObjectiveDto> createObjective(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Objective as json to create a new Objective.", required = true) @RequestBody ObjectiveDto objectiveDTO,
            @AuthenticationPrincipal Jwt token) {
        Objective objective = objectiveMapper.toObjective(objectiveDTO);
        ObjectiveDto createdObjective = this.objectiveMapper
                .toDto(this.objectiveAuthorizationService.createEntity(objective, token));
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
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The objective as json to update an existing Objective.", required = true) @RequestBody ObjectiveDto objectiveDTO,
            @AuthenticationPrincipal Jwt token) {
        Objective objective = this.objectiveMapper.toObjective(objectiveDTO);
        ObjectiveDto updatedObjective = this.objectiveMapper
                .toDto(this.objectiveAuthorizationService.updateEntity(id, objective, token));
        return ResponseEntity.status(HttpStatus.OK).body(updatedObjective);
    }
}
