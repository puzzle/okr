package ch.puzzle.okr.dto.goal;

import ch.puzzle.okr.models.User;

public record GoalKeyResultDto(Long id, String title, String description, User owner) {
}
