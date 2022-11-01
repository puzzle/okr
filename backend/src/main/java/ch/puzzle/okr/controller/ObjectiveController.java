package ch.puzzle.okr.controller;

import ch.puzzle.okr.models.dto.objectives.GetObjectiveDto;
import ch.puzzle.okr.service.ObjectiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/objectives")
public class ObjectiveController {
    private ObjectiveService objectiveService;

    @Autowired
    public ObjectiveController(ObjectiveService objectiveService) {
        this.objectiveService = objectiveService;
    }

    @GetMapping
    public List<GetObjectiveDto> getObjectiveDtoList() {
        return this.objectiveService.getAllObjectives();
    }
}
