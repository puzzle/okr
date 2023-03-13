package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.repository.UserRepository;
import ch.puzzle.okr.service.KeyResultService;
import ch.puzzle.okr.service.ProgressService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KeyResultMapper {

    private final KeyResultService keyResultService;
    // TODO: Remove UserRepository when Login works and use logged in user for createdBy in toKeyResult method
    private final UserRepository userRepository;

    private final ProgressService progressService;

    public KeyResultMapper(KeyResultService keyResultService, UserRepository userRepository,
            ProgressService progressService) {
        this.keyResultService = keyResultService;
        this.userRepository = userRepository;
        this.progressService = progressService;
    }

    public KeyResultDto toDto(KeyResult keyResult) {
        return new KeyResultDto(keyResult.getId(), keyResult.getObjective().getId(), keyResult.getTitle(),
                keyResult.getDescription(), keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(),
                keyResult.getOwner().getLastname(), keyResult.getExpectedEvolution(), keyResult.getUnit(),
                keyResult.getBasisValue(), keyResult.getTargetValue(),
                progressService.calculateKeyResultProgress(keyResult));
    }

    public KeyResult toKeyResult(KeyResultDto keyResultDto) {
        return KeyResult.Builder.builder().withId(keyResultDto.getId()).withTitle(keyResultDto.getTitle())
                .withOwner(this.keyResultService.getOwnerById(keyResultDto.getOwnerId()))
                .withObjective(this.keyResultService.getObjectivebyId(keyResultDto.getObjectiveId()))
                .withDescription(keyResultDto.getDescription()).withTargetValue(keyResultDto.getTargetValue())
                .withBasisValue(keyResultDto.getBasicValue()).withExpectedEvolution(keyResultDto.getExpectedEvolution())
                .withUnit(keyResultDto.getUnit()).withCreatedOn(LocalDateTime.now())
                .withCreatedBy(userRepository.findById(1L).get()).build();
    }
}
