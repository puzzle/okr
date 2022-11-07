package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.MeasureService;
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
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description ="returned all measures",
            content = {@Content(mediaType = "application/json",
                schema = @Schema(implementation = MeasureDto.class))}),
    })
    @GetMapping
    public List<MeasureDto> getAllMeasures() {
        return measureService.getAllMeasures().stream()
                .map(measureMapper::toDto)
                .toList();
    }
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Saved new measure to db",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = MeasureDto.class))}),
            @ApiResponse(responseCode = "400", description = "Can't create measure with id or empty name or not allowed to pass id.", content = @Content)
    })
    @PostMapping
    public ResponseEntity<Object> createMeasure(@Valid @RequestBody MeasureDto measureDto){
        try{
            Measure measure = measureMapper.toMeasure(measureDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(measureService.saveMeasure(measure));
        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create measure =>" + e.getMessage());
        }
    }

}
