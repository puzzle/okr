package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ActionDto;
import ch.puzzle.okr.mapper.ActionMapper;
import ch.puzzle.okr.models.Action;
import ch.puzzle.okr.service.authorization.ActionAuthorizationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v2/action")
public class ActionController {
    private final ActionAuthorizationService actionAuthorizationService;
    private final ActionMapper actionMapper;

    public ActionController(ActionAuthorizationService actionAuthorizationService, ActionMapper actionMapper) {
        this.actionAuthorizationService = actionAuthorizationService;
        this.actionMapper = actionMapper;
    }

    @Operation(summary = "Update Actions", description = "Update Actions of KeyResult")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated Actions of KeyResult", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = ActionDto.class)) }),
            @ApiResponse(responseCode = "400", description = "Can't update Actions, attributes are not set", content = @Content) })
    @PutMapping
    public void updateActions(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(description = "The Action as json to update existing Actions.", required = true) @RequestBody List<ActionDto> actionDtoList) {
        List<Action> actionList = this.actionMapper.toActions(actionDtoList);
        actionAuthorizationService.updateEntities(actionList.get(0).getKeyResult(), actionList);
    }

    @Operation(summary = "Delete Action by Id", description = "Delete Action by Id")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Deleted Action by Id"),
            @ApiResponse(responseCode = "404", description = "Did not find the Action with requested id") })
    @DeleteMapping("/{actionId}")
    public void deleteActionById(@PathVariable long actionId) {
        actionAuthorizationService.deleteActionByActionId(actionId);
    }
}
