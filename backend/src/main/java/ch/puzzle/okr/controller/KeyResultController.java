package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.checkIn.CheckInDto;
import ch.puzzle.okr.dto.keyresult.KeyResultAbstractDto;
import ch.puzzle.okr.dto.keyresult.KeyResultDto;
import ch.puzzle.okr.dto.keyresult.KeyResultMetricDto;
import ch.puzzle.okr.dto.keyresult.KeyResultOrdinalDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.checkIn.CheckInMapper;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.service.business.KeyResultBusinessService;
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

import java.util.List;

@RestController
@RequestMapping("api/v2/keyresults")
public class KeyResultController {

    private final KeyResultBusinessService keyResultBusinessService;
    private final KeyResultMapper keyResultMapper;
    private final CheckInMapper checkInMapper;

    public KeyResultController(KeyResultBusinessService keyResultBusinessService, KeyResultMapper keyResultMapper,
            CheckInMapper checkInMapper) {
        this.keyResultBusinessService = keyResultBusinessService;
        this.keyResultMapper = keyResultMapper;
        this.checkInMapper = checkInMapper;
    }

    @Operation(summary = "Get KeyResult by Id", description = "Get KeyResult by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got KeyResult by Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(allOf = { KeyResultMetricDto.class,
                            KeyResultOrdinalDto.class })) }),
            @ApiResponse(responseCode = "404", description = "Did not find the KeyResult with requested id", content = @Content) })
    @GetMapping("/{id}")
    public KeyResultDto getKeyResultById(@PathVariable long id) {
        return keyResultMapper.toDto(keyResultBusinessService.getKeyResultById(id));
    }

    @Operation(summary = "Get CheckIns from KeyResult", description = "Get all CheckIns from one KeyResult by keyResultId.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all CheckIns from KeyResult.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = CheckInDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find a KeyResult with a specified ID to get CheckIns from.", content = @Content) })
    @GetMapping("/{id}/checkins")
    public List<CheckInDto> getCheckInsFromKeyResult(
            @Parameter(description = "The ID for getting all CheckIns from a KeyResult.", required = true) @PathVariable long id) {
        return keyResultBusinessService.getAllCheckInsByKeyResult(id).stream().map(checkInMapper::toDto).toList();
    }

    @Operation(summary = "Create KeyResult", description = "Create a new KeyResult.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new KeyResult.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(allOf = { KeyResultAbstractDto.class,
                            KeyResultOrdinalDto.class })) }),
            @ApiResponse(responseCode = "404", description = "Did not find an Objective on which the KeyResult tries to refer to.", content = @Content) })
    @PostMapping
    public ResponseEntity<KeyResultDto> createKeyResult(@RequestBody KeyResultAbstractDto keyResultAbstractDto,
            @AuthenticationPrincipal Jwt jwt) {
        KeyResult keyResult = keyResultMapper.toKeyResult(keyResultAbstractDto);
        KeyResultDto createdKeyResult = keyResultMapper.toDto(keyResultBusinessService.createKeyResult(keyResult, jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdKeyResult);
    }

    @Operation(summary = "Update KeyResult", description = "Update a KeyResult by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated KeyResult in db.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(allOf = { KeyResultAbstractDto.class,
                            KeyResultOrdinalDto.class })) }),
            @ApiResponse(responseCode = "404", description = "Did not find a KeyResult with a specified ID to update.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<KeyResultDto> updateKeyResult(
            @Parameter(description = "The ID for updating a KeyResult.", required = true) @PathVariable long id,
            @RequestBody KeyResultAbstractDto keyResultAbstractDto) {
        KeyResult mappedKeyResult = keyResultMapper.toKeyResult(keyResultAbstractDto);
        boolean isKeyResultImUsed = keyResultBusinessService.isImUsed(id, mappedKeyResult);
        KeyResultDto updatedKeyResult = keyResultMapper
                .toDto(keyResultBusinessService.updateKeyResult(id, mappedKeyResult));
        return isKeyResultImUsed ? ResponseEntity.status(HttpStatus.IM_USED).body(updatedKeyResult)
                : ResponseEntity.status(HttpStatus.OK).body(updatedKeyResult);
    }

    @Operation(summary = "Delete KeyResult by Id", description = "Delete KeyResult by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted KeyResult by Id"),
            @ApiResponse(responseCode = "404", description = "Did not find the KeyResult with requested id") })
    @DeleteMapping("/{id}")
    public void deleteKeyResultById(@PathVariable long id) {
        keyResultBusinessService.deleteKeyResultById(id);
    }
}
