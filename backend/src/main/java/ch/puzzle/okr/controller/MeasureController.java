package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.MeasureService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/measures")
public class MeasureController {

    private final MeasureMapper measureMapper;
    private final MeasureService measureService;

    public MeasureController(MeasureMapper measureMapper, MeasureService measureService) {
        this.measureMapper = measureMapper;
        this.measureService = measureService;
    }

    @Operation(summary = "Get Measures",
            description = "Get all Measures from db.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Measures.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasureDto.class))}),
    })
    @GetMapping
    public List<MeasureDto> getAllMeasures() {
        return measureService.getAllMeasures().stream()
                .map(measureMapper::toDto)
                .toList();
    }

    @Operation(summary = "Create Measure",
            description = "Create a new Measure.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Measure.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasureDto.class))}),
            @ApiResponse(responseCode = "400", description = "Can't create new Measure, missing attributes or not allowed to give an ID.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createMeasure(@Valid @RequestBody MeasureDto measureDto) {
        try {
            Measure measure = measureMapper.toMeasure(measureDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(measureService.saveMeasure(measure));
        } catch (ResponseStatusException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create measure =>" + e.getMessage());
        }
    }

    @Operation(summary = "Update Measure",
            description = "Update a Measure by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Measure in db.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasureDto.class))}),
            @ApiResponse(responseCode = "400", description = "Can't create new Measure, attributes are not set.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Given ID of Measure wasn't found.", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<Object> updateMeasure(
            @Parameter(description = "The ID for updating a Measure.", required = true)
            @PathVariable Long id, @Valid @RequestBody MeasureDto measureDto) {
        measureDto.setId(id);
        Measure measure = measureMapper.toMeasure(measureDto);
        return ResponseEntity.status(HttpStatus.OK).body(measureService.updateMeasure(id, measure));
    }
}
