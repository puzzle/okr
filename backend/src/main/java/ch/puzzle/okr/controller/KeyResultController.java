package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.service.KeyResultService;
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

    public KeyResultController(KeyResultService keyResultService, KeyResultMapper keyResultMapper, MeasureMapper measureMapper) {
        this.keyResultService = keyResultService;
        this.keyResultMapper = keyResultMapper;
        this.measureMapper = measureMapper;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated a keyresult with a specified ID.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = KeyResultDto.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find a keyresult with a specified ID.", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<KeyResult> updateKeyResult(@PathVariable long id, @RequestBody KeyResultDto keyResultDto) {
        keyResultDto.setId(id);
        return ResponseEntity.status(HttpStatus.OK).body(this.keyResultService.updateKeyResult(keyResultMapper.toKeyResult(keyResultDto)));
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Measures from KeyResult",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasureDto.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find a KeyResult with a specified ID.", content = @Content)
    })
    @GetMapping("/{id}/measures")
    public List<MeasureDto> getMeasuresFromKeyResult(@PathVariable long id) {
        return keyResultService.getAllMeasuresByKeyResult(id).stream()
                .map(measureMapper::toDto)
                .toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get all key results",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = KeyResultDto.class))}),
    })
    @GetMapping
    public List<KeyResultDto> getAllKeyResults() {
        return this.keyResultService
                .getAllKeyResults()
                .stream()
                .map(keyResultMapper::toDto)
                .toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Create a keyresult.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = KeyResultDto.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find an object on which the key result tries to refer to ", content = @Content)
    })
    @PostMapping
    public KeyResult createKeyResult(@RequestBody KeyResultDto keyResultDto) {
        KeyResult keyResult = this.keyResultMapper.toKeyResult(keyResultDto);
        return this.keyResultService.createKeyResult(keyResult);
    }
}
