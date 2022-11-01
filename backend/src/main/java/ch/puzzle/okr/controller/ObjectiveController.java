package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.objectives.GetObjectiveDTO;
import ch.puzzle.okr.service.ObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/objectives")
@ApiOperation("Objectives API")
public class ObjectiveController {
    private final ObjectiveService objectiveService;

    @Autowired
    public ObjectiveController(ObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    @ApiOperation(value = "Get all objectives", notes = "Returns List of ObjectiveDTOs")
    @APIResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved")
    })
    @GetMapping
    public ResponseEntity<List<GetObjectiveDTO>> getObjectiveDtoList() {
        return ResponseEntity.ok(this.objectiveService.getAllObjectives());
    }
}
