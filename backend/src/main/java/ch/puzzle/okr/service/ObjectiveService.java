package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ObjectiveService {
    private final ObjectiveRepository objectiveRepository;

    @Autowired
    public ObjectiveService(ObjectiveRepository objectiveRepository) {
        this.objectiveRepository = objectiveRepository;
    }

    public List<Objective> getAllObjectives() {
        return (List<Objective>) objectiveRepository.findAll();
    }
}
