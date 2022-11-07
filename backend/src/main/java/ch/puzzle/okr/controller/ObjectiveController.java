package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.ObjectiveDTO;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.service.ObjectiveService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/objectives")
public class ObjectiveController {
    private final ObjectiveService objectiveService;

    private final ObjectiveMapper objectiveMapper;

    private final KeyResultMapper keyResultMapper;


    public ObjectiveController(ObjectiveService objectiveService, ObjectiveMapper objectiveMapper, KeyResultMapper keyResultMapper) {
        this.objectiveService = objectiveService;
        this.objectiveMapper = objectiveMapper;
        this.keyResultMapper = keyResultMapper;
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
                            schema = @Schema(implementation = KeyResultDto.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find a objective with a specified ID.", content = @Content)
    })
    @GetMapping("{id}/keyresults")
    public List<KeyResultDto> getAllKeyResultsByObjective(@PathVariable Long id) {
        return objectiveService.getAllKeyResultsByObjective(id).stream()
                .map(keyResultMapper::toDto)
                .toList();
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned a objective with a specified ID.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = Objective.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find a objective with a specified ID.", content = @Content)
    })
    @GetMapping("/{id}")
    public ObjectiveDTO getObjective(@PathVariable Long id) {
        return objectiveMapper.toDto(objectiveService.getObjective(id));
    }
}
