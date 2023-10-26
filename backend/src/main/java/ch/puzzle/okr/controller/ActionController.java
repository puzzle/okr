package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ActionDto;
import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.service.business.ActionBusinessService;
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
@RequestMapping("api/v2/action")
public class ActionController {
    private final ActionBusinessService actionBusinessService;
    private final ActionMapper actionMapper;

    public ActionController(ActionBusinessService actionBusinessService, ActionMapper actionMapper) {
        this.actionBusinessService = actionBusinessService;
        this.actionMapper = actionMapper;
    }

    @Operation(summary = "Get Actions of KeyResult", description = "Get all Actions by KeyResult ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned an Action with a specified KeyResult ID", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActionDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find an Action with a specified KeyResult ID", content = @Content) })
    @GetMapping("/{keyResultId}")
    public ResponseEntity<List<ActionDto>> getActionsByKeyResult(
            @Parameter(description = "The KeyResult ID for getting Actions of KeyResult.", required = true) @PathVariable Long keyResultId) {
        return ResponseEntity.status(HttpStatus.OK).body(
                actionBusinessService.getActionsByKeyResultId(keyResultId).stream().map(actionMapper::toDto).toList());
    }

    @Operation(summary = "Delete Action by Id", description = "Delete Action by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Action by Id"),
            @ApiResponse(responseCode = "404", description = "Did not find the Action with requested id") })
    @DeleteMapping("/{actionId}")
    public void deleteActionById(@PathVariable long actionId) {
        actionBusinessService.deleteActionById(actionId);
    }
}
