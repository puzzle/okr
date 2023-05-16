package ch.puzzle.okr.dto.goal;

import ch.puzzle.okr.models.User;

public class GoalKeyResultDto {
    private Long id;
    private String title;
    private String description;

    private User owner;

    public GoalKeyResultDto(Long id, String title, String description, User owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public User getOwner() {
        return owner;
    }
}
