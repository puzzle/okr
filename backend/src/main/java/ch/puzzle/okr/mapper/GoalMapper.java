package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.goal.GoalDto;
import ch.puzzle.okr.dto.goal.GoalKeyResultDto;
import ch.puzzle.okr.dto.goal.GoalObjectiveDto;
import ch.puzzle.okr.dto.goal.GoalUserDto;
import ch.puzzle.okr.models.KeyResult;
import org.springframework.stereotype.Service;

@Service
public class GoalMapper {
    public GoalDto toDto(KeyResult keyResult) {
        return new GoalDto(new GoalObjectiveDto(keyResult.getObjective().getId(), keyResult.getObjective().getTitle(),
                keyResult.getObjective().getDescription(), new GoalUserDto(keyResult.getObjective().getOwner().getId(),
                keyResult.getObjective().getOwner().getFirstname(), keyResult.getObjective().getOwner().getFirstname())),
                new GoalKeyResultDto(keyResult.getId(), keyResult.getTitle(), keyResult.getDescription(),
                new GoalUserDto(keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(), keyResult.getOwner().getFirstname())),
                keyResult.getObjective().getTeam().getId(), keyResult.getObjective().getTeam().getName(), keyResult.getObjective().getProgress(),
                keyResult.getQuarter().getNumber(), keyResult.getQuarter().getYear(), keyResult.getExpectedEvolution(),
                keyResult.getUnit(), keyResult.getBasisValue(), keyResult.getTargetValue());
    }
}
