package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.service.business.CheckInBusinessService;
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
@RequestMapping("api/v2/checkIns")
public class CheckInController {

    private final MeasureMapper measureMapper;
    private final CheckInBusinessService checkInBusinessService;

    public CheckInController(MeasureMapper measureMapper, CheckInBusinessService checkInBusinessService) {
        this.measureMapper = measureMapper;
        this.checkInBusinessService = checkInBusinessService;
    }

    @Operation(summary = "Get Check-In", description = "Get Check-In by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a Check-In with a specified ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MeasureDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find a Check-In with a specified ID", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<MeasureDto> getCheckInById(@PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(measureMapper.toDto(this.checkInBusinessService.getMeasureById(id)));
    }

    @Operation(summary = "Create Check-In", description = "Create a new Check-In")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created new Check-In.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MeasureDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Check-In, not allowed to give an ID", content = @Content) })
    @PostMapping
    public ResponseEntity<MeasureDto> createMeasure(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Check-In as json to create a new Check-In.", required = true) @RequestBody MeasureDto measureDto,
            @AuthenticationPrincipal Jwt jwt) {
        Measure measure = measureMapper.toMeasure(measureDto);
        MeasureDto createdMeasure = measureMapper.toDto(checkInBusinessService.saveMeasure(measure, jwt));
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeasure);
    }

    @Operation(summary = "Update Check-In", description = "Update a Check-In by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Check-In in db", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = MeasureDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't create new Check-In, attributes are not set", content = @Content),
            @ApiResponse(responseCode = "404", description = "Given ID of Check-In wasn't found.", content = @Content) })
    @PutMapping("/{id}")
    public ResponseEntity<MeasureDto> updateMeasure(
            @Parameter(description = "The ID for updating a Check-In.", required = true) @PathVariable Long id,
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Check-In as json to update an existing Check-In.", required = true) @RequestBody MeasureDto measureDto,
            @AuthenticationPrincipal Jwt jwt) {
        Measure measure = measureMapper.toMeasure(measureDto);
        MeasureDto updatedMeasure = this.measureMapper.toDto(this.checkInBusinessService.updateMeasure(id, measure, jwt));
        return ResponseEntity.status(HttpStatus.OK).body(updatedMeasure);
    }
}
