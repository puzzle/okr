package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ActionDto;
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
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v2/unit")
public class UnitController {
    private final UnitAuthorizationService unitAuthorizationService;
    private final UnitMapper unitMapper;

    public UnitController(UnitAuthorizationService unitAuthorizationService, UnitMapper unitMapper) {
        this.unitAuthorizationService = unitAuthorizationService;
        this.unitMapper = unitMapper;
    }

    @Operation(summary = "Update Actions", description = "Update Actions of KeyResult")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Actions of KeyResult", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActionDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't update Actions, attributes are not set", content = @Content) })
    @PostMapping
    public UnitDto createUnit(@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Action as json to update existing Actions.", required = true)
    @RequestBody UnitDto unitDto) {
        Unit unit = unitMapper.toUnit(unitDto);
        return unitMapper.toDto(unitAuthorizationService.createUnit(unit));
    }

    @Operation(summary = "Update Actions", description = "Update Actions of KeyResult")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Actions of KeyResult", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActionDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't update Actions,  attributes are not set", content = @Content) })
    @PutMapping("/{unitId}")
    public UnitDto updateUnit(@Parameter(description = "The ID for updating a Team.", required = true)
                                  @PathVariable long unitId,@io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Action as json to update existing Actions.", required = true)
    @RequestBody UnitDto unitDto) {
        Unit unit = unitMapper.toUnit(unitDto);
        return unitMapper.toDto(unitAuthorizationService.editUnit(unitId, unit));
    }
    //
    // @Operation(summary = "Delete Action by Id", description = "Delete Action by
    // Id")
    // @ApiResponses(value = { @ApiResponse(responseCode = "200", description =
    // "Deleted Action by Id"),
    // @ApiResponse(responseCode = "404", description = "Did not find the Action
    // with requested id") })
    // @DeleteMapping("/{unitId}")
    // public void deleteUnitById(@PathVariable long unitId) {
    // unitAuthorizationService.deleteActionByActionId(actionId);
    // }
}
