package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ActionDto;
import ch.puzzle.okr.dto.checkin.CheckInDto;
import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.mapper.checkin.CheckInMapper;
import ch.puzzle.okr.mapper.keyresult.KeyResultMapper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.authorization.ActionAuthorizationService;
import ch.puzzle.okr.service.authorization.KeyResultAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.*;

@RestController
@RequestMapping("api/v2/keyresults")
public class KeyResultController {

    private final KeyResultAuthorizationService keyResultAuthorizationService;
    private final ActionAuthorizationService actionAuthorizationService;
    private final KeyResultMapper keyResultMapper;
    private final CheckInMapper checkInMapper;
    private final ActionMapper actionMapper;

    public KeyResultController(KeyResultAuthorizationService keyResultAuthorizationService,
            ActionAuthorizationService actionAuthorizationService, KeyResultMapper keyResultMapper,
            CheckInMapper checkInMapper, ActionMapper actionMapper) {
        this.keyResultAuthorizationService = keyResultAuthorizationService;
        this.actionAuthorizationService = actionAuthorizationService;
        this.keyResultMapper = keyResultMapper;
        this.checkInMapper = checkInMapper;
        this.actionMapper = actionMapper;
    }

    @Operation(summary = "Get KeyResult by Id", description = "Get KeyResult by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got KeyResult by Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(allOf = { KeyResultMetricDto.class,
                            KeyResultOrdinalDto.class })) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to read a KeyResult", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the KeyResult with requested id", content = @Content) })
    @GetMapping("/{id}")
    public KeyResultDto getKeyResultById(@PathVariable long id) {
        return keyResultMapper.toDto(keyResultAuthorizationService.getEntityById(id));
    }

    @Operation(summary = "Get Check-ins from KeyResult", description = "Get all Check-ins from one KeyResult by keyResultId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Check-ins from KeyResult.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CheckInDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to read Check-ins from a KeyResult", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find a KeyResult with a specified ID to get Check-ins from.", content = @Content) })
    @GetMapping("/{id}/checkins")
    public List<CheckInDto> getCheckInsFromKeyResult(
            @Parameter(description = "The ID for getting all Check-ins from a KeyResult.", required = true) @PathVariable long id) {
        return keyResultAuthorizationService.getAllCheckInsByKeyResult(id).stream().map(checkInMapper::toDto).toList();
    }

    @Operation(summary = "Create KeyResult", description = "Create a new KeyResult.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new KeyResult.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(allOf = { KeyResultDto.class,
                            KeyResultOrdinalDto.class })) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to create a KeyResult", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective on which the KeyResult tries to refer to.", content = @Content) })
    @PostMapping
    public ResponseEntity<KeyResultDto> createKeyResult(@RequestBody KeyResultDto keyResultDto) {
        KeyResult keyResult = keyResultAuthorizationService.createEntity(keyResultMapper.toKeyResult(keyResultDto));
        List<ActionDto> actionDTOs = keyResultDto.getActionList().stream()
                .map(actionDto -> actionDto.withKeyResultId(keyResult.getId())).toList();
        List<Action> actionList = actionMapper.toActions(actionDTOs);
        actionAuthorizationService.createEntities(keyResult, actionList);
        KeyResultDto createdKeyResult = keyResultMapper.toDto(keyResult);
        return ResponseEntity.status(CREATED).body(createdKeyResult);
    }

    @Operation(summary = "Update KeyResult", description = "Update a KeyResult by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated KeyResult in db.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(allOf = { KeyResultDto.class,
                            KeyResultOrdinalDto.class })) }),
            @ApiResponse(responseCode = "226", description = "Updated KeyResult in db but keyResultType was not changed", content = {
                    @Content(mediaType = "application/json", schema = @Schema(allOf = KeyResultDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Not authorized to update a KeyResult", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find a KeyResult with a specified ID to update.", content = @Content),
            @ApiResponse(responseCode = "422", description = "Can't update KeyResult since KeyResult was updated or deleted by another user.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<KeyResultDto> updateKeyResult(
            @Parameter(description = "The ID for updating a KeyResult.", required = true) @PathVariable long id,
            @RequestBody KeyResultDto keyResultDto) {
        KeyResult keyResult = keyResultMapper.toKeyResult(keyResultDto);
        boolean isKeyResultImUsed = keyResultAuthorizationService.isImUsed(id, keyResult);
        keyResult = keyResultAuthorizationService.updateEntity(id, keyResult);
        List<Action> actionList = actionMapper.toActions(keyResultDto.getActionList());
        actionAuthorizationService.updateEntities(keyResult, actionList);
        KeyResultDto updatedKeyResult = keyResultMapper.toDto(keyResult);
        return ResponseEntity.status(isKeyResultImUsed ? IM_USED : OK).body(updatedKeyResult);
    }

    @Operation(summary = "Delete KeyResult by Id", description = "Delete KeyResult by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted KeyResult by Id"),
            @ApiResponse(responseCode = "401", description = "Not authorized to delete a KeyResult", content = @Content),
            @ApiResponse(responseCode = "404", description = "Did not find the KeyResult with requested id") })
    @DeleteMapping("/{id}")
    public void deleteKeyResultById(@PathVariable long id) {
        keyResultAuthorizationService.deleteEntityById(id);
    }
}
