package ch.puzzle.burningokr.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "objective")
public class Objective {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "title")
    private String title;

    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "quarter_id")
    private Quarter quarter;

    @Column(name = "description")
    private String description;

    @Column(name = "progress")
    private Double progress;

    @ManyToOne
    @JoinColumn(name = "created_by_id")
    private User createdBy;

    @Column(name = "created_on")
    private LocalDateTime createdOn;

    private Objective(Builder builder) {
        setId(builder.id);
        setTitle(builder.title);
        setOwner(builder.owner);
        setTeam(builder.team);
        setQuarter(builder.quarter);
        setDescription(builder.description);
        setProgress(builder.progress);
        setCreatedBy(builder.createdBy);
        setCreatedOn(builder.createdOn);
    }

    public Objective() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Objective objective = (Objective) o;
        return Objects.equals(id, objective.id) && Objects.equals(title, objective.title) && Objects.equals(owner, objective.owner) && Objects.equals(team, objective.team) && Objects.equals(quarter, objective.quarter) && Objects.equals(description, objective.description) && Objects.equals(progress, objective.progress) && Objects.equals(createdBy, objective.createdBy) && Objects.equals(createdOn, objective.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, owner, team, quarter, description, progress, createdBy, createdOn);
    }

    @Override
    public String toString() {
        return "Objective{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", owner=" + owner +
                ", team=" + team +
                ", quarter=" + quarter +
                ", description='" + description + '\'' +
                ", progress=" + progress +
                ", createdBy=" + createdBy +
                ", createdOn=" + createdOn +
                '}';
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static final class Builder {
        private Long id;
        private String title;
        private User owner;
        private Team team;
        private Quarter quarter;
        private String description;
        private Double progress;
        private User createdBy;
        private LocalDateTime createdOn;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder title(String val) {
            title = val;
            return this;
        }

        public Builder owner(User val) {
            owner = val;
            return this;
        }

        public Builder team(Team val) {
            team = val;
            return this;
        }

        public Builder quarter(Quarter val) {
            quarter = val;
            return this;
        }

        public Builder description(String val) {
            description = val;
            return this;
        }

        public Builder progress(Double val) {
            progress = val;
            return this;
        }

        public Builder createdBy(User val) {
            createdBy = val;
            return this;
        }

        public Builder createdOn(LocalDateTime val) {
            createdOn = val;
            return this;
        }

        public Objective build() {
            return new Objective(this);
        }
    }
}