package ch.puzzle.okr.dto.goal;

public class GoalObjectiveDto {
    private Long id;
    private String title;
    private String description;
    private GoalUserDto owner;

    public GoalObjectiveDto(Long id, String title, String description, GoalUserDto owner) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.owner = owner;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public GoalUserDto getOwner() {
        return owner;
    }

    public void setOwner(GoalUserDto owner) {
        this.owner = owner;
    }
}
