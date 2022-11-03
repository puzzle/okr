package ch.puzzle.okr.mapper;

import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.dto.ObjectiveDTO;
import org.springframework.stereotype.Service;

@Service
public class ObjectiveMapper {

    public ObjectiveDTO toDto(Objective objective) {
        return new ObjectiveDTO(
                objective.getId(), objective.getTitle(), objective.getOwner().getId(),
                objective.getOwner().getFirstname(), objective.getOwner().getLastname(),
                objective.getTeam().getId(), objective.getTeam().getName(),
                objective.getQuarter().getNumber(), objective.getQuarter().getYear(),
                objective.getDescription(), objective.getProgress());
    }
}
