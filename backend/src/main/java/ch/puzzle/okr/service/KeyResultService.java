package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.ObjectiveRepository;
import ch.puzzle.okr.repository.QuarterRepository;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class KeyResultService {

    KeyResultRepository keyResultRepository;
    UserService userService;
    ObjectiveService objectiveService;
    QuarterRepository quarterRepository;
    UserRepository userRepository;
    ObjectiveRepository objectiveRepository;

    public KeyResultService(KeyResultRepository keyResultRepository, UserService userService, ObjectiveService objectiveService, QuarterRepository quarterRepository, UserRepository userRepository, ObjectiveRepository objectiveRepository) {
        this.keyResultRepository = keyResultRepository;
        this.userService = userService;
        this.objectiveService = objectiveService;
        this.quarterRepository = quarterRepository;
        this.userRepository = userRepository;
        this.objectiveRepository = objectiveRepository;
    }

    public KeyResult getKeyResultById(long id) {
        return keyResultRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("KeyResult with id %d not found", id))
        );
    }

    public KeyResult updateKeyResult(KeyResult keyResult) {
        if (keyResultExists(keyResult.getId())) {
            return this.keyResultRepository.save(keyResult);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find keyresult with id %d", keyResult.getId()));
        }
    }

    public Quarter getQuarterById(long id) {
        return this.quarterRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Could not find quarter with id %d", id)));
    }
    public User getOwnerById(long id){
        return  this.userRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Owner with id %d not found", id))
        );
    }

    public Objective getObjectivebyId(long id){
        return  this.objectiveRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Objective with id %d not found", id))
        );
    }

    public boolean keyResultExists(long id) {
        return keyResultRepository.findById(id).isPresent();
    }
}
