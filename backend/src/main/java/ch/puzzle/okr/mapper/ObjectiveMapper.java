package ch.puzzle.okr.mapper;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.dto.objectives.GetObjectiveDto;
import org.springframework.stereotype.Component;

@Component
public class ObjectiveMapper {

    public GetObjectiveDto entityToGetObjectiveDto(Objective objective) {
        return new GetObjectiveDto(
                1, objective.getTeam().getName(),
                objective.getTitle(), objective.getDescription(),
                1, objective.getOwner().getUsername(),
                objective.getQuarter().toString());
    }
}
