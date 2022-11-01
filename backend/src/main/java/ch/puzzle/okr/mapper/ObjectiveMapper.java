package ch.puzzle.okr.mapper;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.dto.objectives.GetObjectiveDTO;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveMapper {

    public GetObjectiveDTO entityToGetObjectiveDto(Objective objective) {
        return new GetObjectiveDTO(
                1, objective.getTeam().getName(),
                objective.getTitle(), objective.getDescription(),
                1, objective.getOwner().getUsername(),
                objective.getQuarter().toString(),
                objective.getCreatedOn());
    }
}
