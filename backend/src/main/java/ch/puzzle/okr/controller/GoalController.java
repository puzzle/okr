package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.goal.GoalDto;
import ch.puzzle.okr.mapper.GoalMapper;
import ch.puzzle.okr.service.KeyResultService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/goals")
public class GoalController {

    private final GoalMapper goalMapper;
    private final KeyResultService keyResultService;

    public GoalController(GoalMapper goalMapper, KeyResultService keyResultService) {
        this.goalMapper = goalMapper;
        this.keyResultService = keyResultService;
    }

    @Operation(summary = "Get Goal", description = "Get a Goal by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a Goal with a specified ID.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = GoalDto.class)) }),
            @ApiResponse(responseCode = "404", description = "Did not find a Goal with a specified ID.", content = @Content) })
    @GetMapping("/{id}")
    public ResponseEntity<GoalDto> getGoalById(
            @Parameter(description = "The ID for getting a Goal.", required = true) @PathVariable long id) {
        return ResponseEntity.status(HttpStatus.OK).body(goalMapper.toDto(keyResultService.getKeyResultById(id)));
    }
}
