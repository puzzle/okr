package ch.puzzle.okr.dto;

import ch.puzzle.okr.models.Objective;

public record CompletedDto(Long id, Objective objective, String comment) {
}
