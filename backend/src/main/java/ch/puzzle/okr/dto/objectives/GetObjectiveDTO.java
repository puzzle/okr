package ch.puzzle.okr.dto.objectives;

import java.time.LocalDateTime;

public class GetObjectiveDTO {
    @ApiModelProperty(notes = "Objective team ID", example = "1", required = true)
    private Integer teamId;
    @ApiModelProperty(notes = "Objective team name", example = "UFC", required = true)
    private String teamName;
    @ApiModelProperty(notes = "Objective title", example = "Make more money", required = true)
    private String title;
    @ApiModelProperty(notes = "Objective description", example = "This is a description", required = true)
    private String description;
    @ApiModelProperty(notes = "ID of objective owner", example = "1", required = true)
    private Integer ownerId;
    @ApiModelProperty(notes = "Name of objective owner", example = "Conor McGregor", required = true)
    private String ownerName;
    @ApiModelProperty(notes = "Quarter in which objective is currently placed", example = "22/1", required = true)
    private String quarter;
    @ApiModelProperty(notes = "Localdatetime when objective was created", example = "2022-07-01T:00:00", required = true)
    private LocalDateTime createdOn;

    public GetObjectiveDTO(Integer teamId, String teamName, String title, String description, Integer ownerId,
                           String ownerName, String quarter, LocalDateTime createdOn) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.title = title;
        this.description = description;
        this.ownerId = ownerId;
        this.ownerName = ownerName;
        this.quarter = quarter;
        this.createdOn = createdOn;
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

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }
}
