package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.service.KeyResultService;
import ch.puzzle.okr.service.ProgressService;
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

    private final KeyResultService keyResultService;
    private final KeyResultMapper keyResultMapper;
    private final MeasureMapper measureMapper;
    private final ProgressService progressService;

    public KeyResultController(KeyResultService keyResultService, KeyResultMapper keyResultMapper,
            MeasureMapper measureMapper, ProgressService progressService) {
        this.keyResultService = keyResultService;
        this.keyResultMapper = keyResultMapper;
        this.measureMapper = measureMapper;
        this.progressService = progressService;
    }

    @Operation(summary = "Create KeyResult", description = "Create a new KeyResult.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new KeyResult.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = KeyResultDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective on which the key result tries to refer to.", content = @Content) })
    @PostMapping
    public ResponseEntity<KeyResultDto> createKeyResult(@RequestBody KeyResultDto keyResultDto) {
        KeyResult keyResult = this.keyResultMapper.toKeyResult(keyResultDto);
        KeyResultDto createdKeyResult = this.keyResultMapper.toDto(this.keyResultService.createKeyResult(keyResult));
        this.progressService.updateObjectiveProgress(keyResult.getObjective().getId());
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
        keyResultDto.setId(id);
        KeyResultDto updatedKeyResult = this.keyResultMapper
                .toDto(this.keyResultService.updateKeyResult(keyResultMapper.toKeyResult(keyResultDto)));
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
        return keyResultService.getAllMeasuresByKeyResult(id).stream().map(measureMapper::toDto).toList();
    }

}
