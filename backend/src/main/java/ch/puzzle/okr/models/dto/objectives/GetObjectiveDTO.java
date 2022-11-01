package ch.puzzle.okr.models.dto.objectives;

public class GetObjectiveDTO {
    private Integer teamId;
    private String teamName;
    private String title;
    private String description;
    private Integer ownerId;
    private String ownerName;
    private String quarter;

    public GetObjectiveDTO(Integer teamId, String teamName, String title, String description, Integer ownerId, String ownerName, String quarter) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.quarter = quarter;
    }

    public GetObjectiveDTO() {
    }

    public Integer getTeamId() {
        return teamId;
    }

    public void setTeamId(Integer teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
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

    public Integer getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Integer ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getQuarter() {
        return quarter;
    }

    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }
}
