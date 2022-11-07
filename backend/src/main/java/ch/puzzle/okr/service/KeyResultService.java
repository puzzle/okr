package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class KeyResultService {

    KeyResultRepository keyResultRepository;
    KeyResultMapper keyResultMapper;
    UserService userService;
    ObjectiveService objectiveService;

    public KeyResultService(KeyResultRepository keyResultRepository, KeyResultMapper keyResultMapper, UserService userService, ObjectiveService objectiveService) {
        this.keyResultRepository = keyResultRepository;
        this.keyResultMapper = keyResultMapper;
        this.userService = userService;
        this.objectiveService = objectiveService;
    }

    public KeyResult createKeyResult(KeyResultDto keyResultDto) {
        User owner = this.userService.getUserById(keyResultDto.getOwnerId());
        Objective objective = this.objectiveService.getObjective(keyResultDto.getObjectiveId());
        KeyResult keyResult = this.keyResultMapper.toKeyResult(keyResultDto);
        keyResult.setOwner(owner);
        keyResult.setObjective(objective);
        return this.keyResultRepository.save(keyResult);
    }

    public KeyResult getKeyResultById(long id) {
        return keyResultRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("KeyResult with id %d not found", id))
        );
    }
}
