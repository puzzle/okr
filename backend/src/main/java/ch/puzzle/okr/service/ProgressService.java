package ch.puzzle.okr.service;

import ch.puzzle.okr.service.business.KeyResultBusinessService;
import ch.puzzle.okr.service.business.ObjectiveBusinessService;
import ch.puzzle.okr.service.business.ProgressBusinessService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProgressService {
    private final ObjectiveBusinessService objectiveBusinessService;
    private final KeyResultBusinessService keyResultBusinessService;
    private final ProgressBusinessService progressBusinessService;

    public ProgressService(ObjectiveBusinessService objectiveBusinessService,
            KeyResultBusinessService keyResultBusinessService, ProgressBusinessService progressBusinessService) {
        this.objectiveBusinessService = objectiveBusinessService;
        this.keyResultBusinessService = keyResultBusinessService;
        this.progressBusinessService = progressBusinessService;
    }

    public void updateObjectiveProgress(Long objectiveId) {
        objectiveBusinessService.updateObjectiveProgress(objectiveId);
    }

    @Transactional
    public void deleteKeyResultAndUpdateProgress(Long id) {
        Long objectiveId = keyResultBusinessService.getKeyResultById(id).getObjective().getId();
        keyResultBusinessService.deleteKeyResultById(id);
        updateObjectiveProgress(objectiveId);
    }
}
