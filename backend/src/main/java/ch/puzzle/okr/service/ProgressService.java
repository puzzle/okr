package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.repository.ObjectiveRepository;
import org.springframework.stereotype.Service;

@Service
public class ProgressService {
    private final ObjectiveService objectiveService;
    private final ObjectiveRepository objectiveRepository;

    public ProgressService(ObjectiveService objectiveService, ObjectiveRepository objectiveRepository) {
        this.objectiveService = objectiveService;
        this.objectiveRepository = objectiveRepository;
    }

    public void updateObjectiveProgress(Long objectiveId) {
        Objective objective = this.objectiveService.getObjective(objectiveId);
        Double progress = this.getObjectiveProgressInPercent(objective);
        objective.setProgress(progress);
        this.objectiveRepository.save(objective);
    }

    public Double getObjectiveProgressInPercent(Objective objectiveToUpdate) {
        return this.objectiveRepository.getProgressOfObjective(objectiveToUpdate.getId());
    }
}
