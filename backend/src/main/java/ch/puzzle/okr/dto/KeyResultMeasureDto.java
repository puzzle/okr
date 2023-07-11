package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.ExpectedEvolution;
import ch.puzzle.okr.models.Unit;

public record KeyResultMeasureDto(Long id, Long objectiveId, String title, String description, Long ownerId,
        String ownerFirstname, String ownerLastname, ExpectedEvolution expectedEvolution, Unit unit, Double basicValue,
        Double targetValue, MeasureDto measure, Long progress) {
}
