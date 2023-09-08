package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
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
@RequestMapping("api/v1/keyresults")
public class KeyResultController {

    private final KeyResultBusinessService keyResultBusinessService;
    private final KeyResultMapper keyResultMapper;
    private final MeasureMapper measureMapper;

    public KeyResultController(KeyResultBusinessService keyResultBusinessService, KeyResultMapper keyResultMapper,
            MeasureMapper measureMapper) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.keyResultMapper = keyResultMapper;
        this.measureMapper = measureMapper;
    }

    @Operation(summary = "Get KeyResult by Id", description = "Get KeyResult by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got keyresult by Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = KeyResultDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find the keyresult with requested id", content = @Content) })
    @GetMapping("/{id}")
    public KeyResultDto getKeyResultbyId(@PathVariable long id) {
        return keyResultMapper.toDto(keyResultBusinessService.getKeyResultById(id));
    }

    @Operation(summary = "Create KeyResult", description = "Create a new KeyResult.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new KeyResult.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = KeyResultDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective on which the key result tries to refer to.", content = @Content) })
    @PostMapping
    public ResponseEntity<KeyResultDto> createKeyResult(@RequestBody KeyResultDto keyResultDto) {
        KeyResult keyResult = keyResultMapper.toKeyResult(keyResultDto);
        KeyResultDto createdKeyResult = keyResultMapper.toDto(keyResultBusinessService.saveKeyResult(keyResult));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdKeyResult);
    }

    @Operation(summary = "Update KeyResult", description = "Update a KeyResult by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated KeyResult in db.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = KeyResultDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find a KeyResult with a specified ID to update.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<KeyResultDto> updateKeyResult(
            @Parameter(description = "The ID for updating a KeyResult.", required = true) @PathVariable long id,
            @RequestBody KeyResultDto keyResultDto) {
        KeyResultDto updatedKeyResult = keyResultMapper
                .toDto(keyResultBusinessService.updateKeyResult(keyResultMapper.toKeyResult(keyResultDto)));
        return ResponseEntity.status(HttpStatus.OK).body(updatedKeyResult);
    }

    @Operation(summary = "Get Measures from KeyResult", description = "Get all Measures from one KeyResult by keyResultId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Measures from KeyResult.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MeasureDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find a KeyResult with a specified ID to get Measures from.", content = @Content) })
    @GetMapping("/{id}/measures")
    public List<MeasureDto> getMeasuresFromKeyResult(
            @Parameter(description = "The ID for getting all Measures from a KeyResult.", required = true) @PathVariable long id) {
        return keyResultBusinessService.getAllMeasuresByKeyResult(id).stream().map(measureMapper::toDto).toList();
    }

    @Operation(summary = "Delete KeyResult by Id", description = "Delete KeyResult by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted keyresult by Id"),
            @ApiResponse(responseCode = "404", description = "Did not find the keyresult with requested id") })
    @DeleteMapping("/{id}")
    public void deleteKeyResultById(@PathVariable long id) {
        // Gets reimplemented by Lias
    }
}
