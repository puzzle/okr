package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(indexes = { @Index(name = "idx_objective_title", columnList = "title") })
public class Objective {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_objective")
    @NotNull(message = "Id must not be null")
    private Long id;

    @NotBlank
    @NotNull(message = "Title must not be null")
    @Size(min = 2, max = 250)
    private String title;

    @NotNull(message = "CreatedBy must not be null")
    @ManyToOne
    private User createdBy;

    @NotNull(message = "Team must not be null")
    @ManyToOne
    private Team team;

    @NotNull(message = "Quarter must not be null")
    @ManyToOne
    private Quarter quarter;

    @Size(max = 4096)
    private String description;

    private Long progress;

    private LocalDateTime modifiedOn;

    @NotNull(message = "State must not be null")
    @Enumerated(EnumType.STRING)
    private State state;

    @NotNull(message = "CreatedOn must not be null")
    private LocalDateTime createdOn;

    public Objective() {
    }

    private Objective(Builder builder) {
        id = builder.id;
        setTitle(builder.title);
        setCreatedBy(builder.createdBy);
        setTeam(builder.team);
        setQuarter(builder.quarter);
        setDescription(builder.description);
        setProgress(builder.progress);
        setModifiedOn(builder.modifiedOn);
        setState(builder.state);
        setCreatedOn(builder.createdOn);
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Quarter getQuarter() {
        return quarter;
    }

    public void setQuarter(Quarter quarter) {
        this.quarter = quarter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getProgress() {
        return progress;
    }

    public void setProgress(Long progress) {
        this.progress = progress;
    }

    public LocalDateTime getModifiedOn() {
        return modifiedOn;
    }

    public void setModifiedOn(LocalDateTime modifiedOn) {
        this.modifiedOn = modifiedOn;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
    }

    @Override
    public String toString() {
        return "Objective{" + "id=" + id + ", title='" + title + '\'' + ", createdBy=" + createdBy + ", team=" + team
                + ", quarter=" + quarter + ", description='" + description + '\'' + ", progress=" + progress
                + ", modifiedOn=" + modifiedOn + ", state=" + state + ", createdOn=" + createdOn + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Objective objective = (Objective) o;
        return Objects.equals(id, objective.id) && Objects.equals(title, objective.title)
                && Objects.equals(createdBy, objective.createdBy) && Objects.equals(team, objective.team)
                && Objects.equals(quarter, objective.quarter) && Objects.equals(description, objective.description)
                && Objects.equals(progress, objective.progress) && Objects.equals(modifiedOn, objective.modifiedOn)
                && state == objective.state && Objects.equals(createdOn, objective.createdOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, createdBy, team, quarter, description, progress, modifiedOn, state, createdOn);
    }

    public static final class Builder {
        private Long id;
        private String title;
        private User createdBy;
        private Team team;
        private Quarter quarter;
        private String description;
        private Long progress;
        private LocalDateTime modifiedOn;
        private State state;
        private LocalDateTime createdOn;

        public Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withOwner(User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder withTeam(Team team) {
            this.team = team;
            return this;
        }

        public Builder withQuarter(Quarter quarter) {
            this.quarter = quarter;
            return this;
        }

        public Builder withDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder withProgress(Long progress) {
            this.progress = progress;
            return this;
        }

        public Builder withModifiedOn(LocalDateTime modifiedOn) {
            this.modifiedOn = modifiedOn;
            return this;
        }

        public Builder withState(State state) {
            this.state = state;
            return this;
        }

        public Builder withCreatedOn(LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Objective build() {
            return new Objective(this);
        }
    }
}