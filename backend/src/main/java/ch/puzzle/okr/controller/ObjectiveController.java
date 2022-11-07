package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ObjectiveDTO;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.service.ObjectiveService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/objectives")
public class ObjectiveController {
    private final ObjectiveService objectiveService;

    private final ObjectiveMapper objectiveMapper;

    public ObjectiveController(ObjectiveService objectiveService, ObjectiveMapper objectiveMapper) {
        this.objectiveService = objectiveService;
        this.objectiveMapper = objectiveMapper;
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

    @GetMapping("/{id}")
    public ObjectiveDTO getObjective(@PathVariable Long id) {
        return objectiveMapper.toDto(objectiveService.getObjective(id));
    }
}
