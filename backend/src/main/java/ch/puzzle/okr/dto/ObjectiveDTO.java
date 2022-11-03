package ch.puzzle.okr.dto;

public class ObjectiveDTO {
    private Long id;
    private String title;
    private Long ownerId;
    private String ownerFirstname;
    private String ownerLastname;
    private Long teamId;
    private String teamName;
    private Integer quarterNumber;
    private Integer quarterYear;
    private String description;
    private Double progress;

    public ObjectiveDTO(Long id, String title, Long ownerId, String ownerFirstname, String ownerLastname, Long teamId, String teamName,
                        Integer quarterNumber, Integer quarterYear, String description, Double progress) {
        this.id = id;
        this.title = title;
        this.ownerId = ownerId;
        this.ownerFirstname = ownerFirstname;
        this.ownerLastname = ownerLastname;
        this.teamId = teamId;
        this.teamName = teamName;
        this.quarterNumber = quarterNumber;
        this.quarterYear = quarterYear;
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

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(Long ownerId) {
        this.ownerId = ownerId;
    }

    public String getOwnerFirstname() {
        return ownerFirstname;
    }

    public void setOwnerFirstname(String ownerFirstname) {
        this.ownerFirstname = ownerFirstname;
    }

    public String getOwnerLastname() {
        return ownerLastname;
    }

    public void setOwnerLastname(String ownerLastname) {
        this.ownerLastname = ownerLastname;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public Integer getQuarterNumber() {
        return quarterNumber;
    }

    public void setQuarterNumber(Integer quarterNumber) {
        this.quarterNumber = quarterNumber;
    }

    public Integer getQuarterYear() {
        return quarterYear;
    }

    public void setQuarterYear(Integer quarterYear) {
        this.quarterYear = quarterYear;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }
}
