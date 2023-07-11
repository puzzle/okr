package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.service.KeyResultService;
import ch.puzzle.okr.service.ProgressService;
import ch.puzzle.okr.service.UserService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class KeyResultMapper {

    private final KeyResultService keyResultService;
    // TODO: Remove UserService when Login works and use logged in user for createdBy in toKeyResult method
    private final UserService userService;

    private final ProgressService progressService;

    public KeyResultMapper(KeyResultService keyResultService, UserService userService,
            ProgressService progressService) {
        this.keyResultService = keyResultService;
        this.userService = userService;
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
        return KeyResult.Builder.builder().withId(keyResultDto.id()).withTitle(keyResultDto.title())
                .withOwner(keyResultService.getOwnerById(keyResultDto.ownerId()))
                .withObjective(keyResultService.getObjectivebyId(keyResultDto.objectiveId()))
                .withDescription(keyResultDto.description()).withTargetValue(keyResultDto.targetValue())
                .withBasisValue(keyResultDto.basicValue()).withExpectedEvolution(keyResultDto.expectedEvolution())
                .withUnit(keyResultDto.unit()).withModifiedOn(LocalDateTime.now())
                .withCreatedBy(userService.getOwnerById(keyResultDto.ownerId())).build();
    }
}
