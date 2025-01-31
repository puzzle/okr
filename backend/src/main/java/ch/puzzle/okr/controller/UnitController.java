package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.UnitDto;
import ch.puzzle.okr.mapper.UnitMapper;
import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.service.authorization.UnitAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.util.List;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/units")
public class UnitController {
    private final UnitAuthorizationService unitAuthorizationService;
    private final UnitMapper unitMapper;

    public UnitController(UnitAuthorizationService unitAuthorizationService, UnitMapper unitMapper) {
        this.unitAuthorizationService = unitAuthorizationService;
        this.unitMapper = unitMapper;
    }

    @Operation(summary = "Get Units by User", description = "Retrieves a list of units associated with the currently authenticated user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved user's units", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UnitDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
    @GetMapping("/user")
    public List<UnitDto> getUnitsByUser() {
        return unitAuthorizationService.getUnitsOfUser().stream().map(unitMapper::toDto).toList();
    }

    @Operation(summary = "Get All Units", description = "Retrieves a list of all available units.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved all units", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UnitDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Bad request", content = @Content) })
    @GetMapping
    public List<UnitDto> getAllUnits() {
        return unitAuthorizationService.getAllUnits().stream().map(unitMapper::toDto).toList();
    }

    @Operation(summary = "Create a Unit", description = "Creates a new unit with the provided details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Unit successfully created", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UnitDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content) })
    @PostMapping
    public UnitDto createUnit(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Unit data in JSON format.", required = true)
    @RequestBody UnitDto unitDto) {
        Unit unit = unitMapper.toUnit(unitDto);
        return unitMapper.toDto(unitAuthorizationService.createUnit(unit));
    }

    @Operation(summary = "Update a Unit", description = "Updates an existing unit with new details.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Unit successfully updated", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UnitDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input data", content = @Content),
            @ApiResponse(responseCode = "404", description = "Unit not found", content = @Content) })
    @PutMapping("/{unitId}")
    public UnitDto updateUnit(@Parameter(description = "ID of the unit to be updated.", required = true)
    @PathVariable long unitId, @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "Updated unit data in JSON format.", required = true) @RequestBody UnitDto unitDto) {
        Unit unit = unitMapper.toUnit(unitDto);
        return unitMapper.toDto(unitAuthorizationService.updateUnit(unitId, unit));
    }

    @Operation(summary = "Delete a Unit", description = "Deletes an existing unit by its ID.")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Unit successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Unit not found") })
    @DeleteMapping("/{unitId}")
    public void deleteUnitById(@Parameter(description = "ID of the unit to be deleted.", required = true)
    @PathVariable long unitId) {
        unitAuthorizationService.deleteUnitById(unitId);
    }
}
