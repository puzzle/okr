package ch.puzzle.okr.dto;

public class ObjectiveDto {
    private Long id;
    private String title;
    private Long ownerId;
    private String ownerFirstname;
    private String ownerLastname;
    private Long teamId;
    private String teamName;
    private Long quarterId;
    private String quarterLabel;
    private String description;
    private Long progress;

    public ObjectiveDto(Long id, String title, Long ownerId, String ownerFirstname, String ownerLastname, Long teamId,
            String teamName, Long quarterId, String quarterLabel, String description, Long progress) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.ownerFirstname = ownerFirstname;
        this.ownerLastname = ownerLastname;
        this.teamId = teamId;
        this.teamName = teamName;
        this.quarterId = quarterId;
        this.quarterLabel = quarterLabel;
        this.description = description;
        this.progress = progress;
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

    public Long getOwnerId() {
        return ownerId;
    }

    public String getOwnerFirstname() {
        return ownerFirstname;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public Long getTeamId() {
        return teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public String getQuarterLabel() {
        return quarterLabel;
    }

    public String getDescription() {
        return description;
    }

    public Long getProgress() {
        return progress;
    }
}
