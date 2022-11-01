package ch.puzzle.okr.service;

import ch.puzzle.okr.mapper.ObjectiveMapper;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.dto.objectives.GetObjectiveDto;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;
    private final ObjectiveMapper objectiveMapper;

    @Autowired
    public ObjectiveService(ObjectiveRepository objectiveRepository, ObjectiveMapper objectiveMapper) {
        this.objectiveRepository = objectiveRepository;
        this.objectiveMapper = objectiveMapper;
    }

    public List<GetObjectiveDto> getAllObjectives() {
        return StreamSupport.stream(this.objectiveRepository.findAll().spliterator(), false)
                        .map(this.objectiveMapper::entityToGetObjectiveDto).collect(Collectors.toList());
    }
}
