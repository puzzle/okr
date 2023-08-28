package ch.puzzle.okr.service;

import ch.puzzle.okr.service.business.KeyResultBusinessService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProgressService {
    private final KeyResultBusinessService keyResultBusinessService;

    public ProgressService(KeyResultBusinessService keyResultBusinessService) {
        this.keyResultBusinessService = keyResultBusinessService;
    }

    @Transactional
    public void deleteKeyResultAndUpdateProgress(Long id) {
        Long objectiveId = keyResultBusinessService.getKeyResultById(id).getObjective().getId();
        keyResultBusinessService.deleteKeyResultById(id);
    }
}
