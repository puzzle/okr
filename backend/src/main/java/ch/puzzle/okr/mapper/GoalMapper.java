package ch.puzzle.okr.mapper;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.goal.GoalDto;
import ch.puzzle.okr.dto.goal.KeyresultDto;
import ch.puzzle.okr.dto.goal.ObjectiveDto;
import ch.puzzle.okr.dto.goal.UserDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Objective;
import org.springframework.stereotype.Service;

@Service
public class GoalMapper {
    public GoalDto toDto(KeyResult keyResult) {
        return new GoalDto(new ObjectiveDto(keyResult.getObjective().getId(), keyResult.getObjective().getTitle(),
                keyResult.getObjective().getDescription(), new UserDto(keyResult.getObjective().getOwner().getId(),
                keyResult.getObjective().getOwner().getFirstname(), keyResult.getObjective().getOwner().getFirstname())),
                new KeyresultDto(keyResult.getId(), keyResult.getTitle(), keyResult.getDescription(),
                new UserDto(keyResult.getOwner().getId(), keyResult.getOwner().getFirstname(), keyResult.getOwner().getFirstname())),
                keyResult.getObjective().getTeam().getId(), keyResult.getObjective().getTeam().getName(), keyResult.getObjective().getProgress(),
                keyResult.getQuarter().getNumber(), keyResult.getQuarter().getYear(), keyResult.getExpectedEvolution(),
                keyResult.getUnit(), keyResult.getBasisValue(), keyResult.getTargetValue());
    }
}
