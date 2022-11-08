package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.goal.GoalDto;
import ch.puzzle.okr.mapper.GoalMapper;
import ch.puzzle.okr.service.KeyResultService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/goals")
public class GoalController {

    private final GoalMapper goalMapper;
    private final KeyResultService keyResultService;

    public GoalController(GoalMapper goalMapper, KeyResultService keyResultService) {
        this.goalMapper = goalMapper;
        this.keyResultService = keyResultService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a goal with a specified ID.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = GoalDto.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find a Goal with a specified ID.", content = @Content)
    })
    @GetMapping("/{id}")
    public GoalDto getGoalById(@PathVariable long id) {
        return goalMapper.toDto(keyResultService.getKeyResultById(id));
    }
}
