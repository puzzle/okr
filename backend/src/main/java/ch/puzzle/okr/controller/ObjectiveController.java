package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDTO;
import ch.puzzle.okr.dto.ObjectiveDTO;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.KeyResultService;
import ch.puzzle.okr.service.ObjectiveService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/objectives")
public class ObjectiveController {
    private final ObjectiveService objectiveService;

    private final KeyResultService keyResultService;

    private final ObjectiveMapper objectiveMapper;

    public ObjectiveController(ObjectiveService objectiveService, ObjectiveMapper objectiveMapper, KeyResultService keyResultService) {
        this.objectiveService = objectiveService;
        this.objectiveMapper = objectiveMapper;
        this.keyResultService = keyResultService;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all Objectives",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Objective.class))}),
    })
    @GetMapping
    public List<ObjectiveDTO> getAllObjectives() {
        return objectiveService.getAllObjectives().stream()
                .map(objectiveMapper::toDto)
                .toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned all KeyResultsFromObject",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Objective.class))}),
    })
    @GetMapping("{id}/keyresults")
    public List<KeyResultDTO> getAllKeyResultsFromObjective() {
        return null;
    }
}
