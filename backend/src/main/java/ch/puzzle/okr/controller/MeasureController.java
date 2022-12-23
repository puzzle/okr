package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.MeasureService;
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

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/measures")
public class MeasureController {

    private final MeasureMapper measureMapper;
    private final MeasureService measureService;
    private final ProgressService progressService;

    public MeasureController(MeasureMapper measureMapper, MeasureService measureService,
            ProgressService progressService) {
        this.measureMapper = measureMapper;
        this.measureService = measureService;
        this.progressService = progressService;
    }

    @Operation(summary = "Get Measures", description = "Get all Measures from db.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returned all Measures.", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = MeasureDto.class)) }), })
    @GetMapping
    public List<MeasureDto> getAllMeasures() {
        return measureService.getAllMeasures().stream().map(measureMapper::toDto).toList();
    }

    @Operation(summary = "Get Measure by Id", description = "Get Measure by Id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Got Measure by Id", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MeasureDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find the Measure with requested id", content = @Content) })
    @GetMapping("/{id}")
    public MeasureDto getMeasureById(@PathVariable long id) {
        return this.measureMapper.toDto(this.measureService.getMeasureById(id));
    }

    @Operation(summary = "Create Measure", description = "Create a new Measure.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Measure.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MeasureDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Measure, missing attributes or not allowed to give an ID.", content = @Content) })
    @PostMapping
    public ResponseEntity<MeasureDto> createMeasure(@Valid @RequestBody MeasureDto measureDto) {
        Measure measure = measureMapper.toMeasure(measureDto);
        MeasureDto createdMeasure = this.measureMapper.toDto(this.measureService.saveMeasure(measure));
        this.progressService.updateObjectiveProgress(measure.getKeyResult().getObjective().getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeasure);
    }

    @Operation(summary = "Update Measure", description = "Update a Measure by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Measure in db.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MeasureDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Measure, attributes are not set.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Given ID of Measure wasn't found.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<MeasureDto> updateMeasure(
            @Parameter(description = "The ID for updating a Measure.", required = true) @PathVariable Long id,
            @Valid @RequestBody MeasureDto measureDto) {
        measureDto.setId(id);
        Measure measure = measureMapper.toMeasure(measureDto);
        MeasureDto updatedMeasure = this.measureMapper.toDto(this.measureService.updateMeasure(id, measure));
        this.progressService.updateObjectiveProgress(measure.getKeyResult().getObjective().getId());
        return ResponseEntity.status(HttpStatus.OK).body(updatedMeasure);
    }
}
