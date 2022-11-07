package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import org.springframework.http.HttpStatus;
import ch.puzzle.okr.repository.*;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class KeyResultService {

    KeyResultRepository keyResultRepository;
    KeyResultMapper keyResultMapper;
    UserService userService;
    ObjectiveService objectiveService;
    QuarterRepository quarterRepository;
    UserRepository userRepository;
    ObjectiveRepository objectiveRepository;

    public KeyResultService(KeyResultRepository keyResultRepository, KeyResultMapper keyResultMapper, UserService userService, ObjectiveService objectiveService, QuarterRepository quarterRepository, UserRepository userRepository, ObjectiveRepository objectiveRepository) {
        this.keyResultRepository = keyResultRepository;
        this.keyResultMapper = keyResultMapper;
        this.userService = userService;
        this.objectiveService = objectiveService;
        this.quarterRepository = quarterRepository;
        this.userRepository = userRepository;
        this.objectiveRepository = objectiveRepository;
    }

    public KeyResult createKeyResult(KeyResultDto keyResultDto) {
        Objective objective = this.objectiveService.getObjective(keyResultDto.getObjectiveId());
        KeyResult keyResult = this.keyResultMapper.toKeyResult(keyResultDto);
        keyResult.setObjective(objective);
        return this.keyResultRepository.save(keyResult);
    }

    public KeyResult getKeyResultById(long id) {
        return keyResultRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("KeyResult with id %d not found", id))
        );
    }

    public KeyResult updateKeyResult(KeyResult keyResult) {
        return this.keyResultRepository.save(keyResult);
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

}
