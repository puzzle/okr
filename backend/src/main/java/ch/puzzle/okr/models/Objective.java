package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(indexes = {
        @Index(name = "idx_objective_title", columnList = "title")
})
public class Objective {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_objective")
    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 2, max = 250)
    private String title;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "owner_id")
    private User owner;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "quarter_id")
    private Quarter quarter;

    @NotBlank
    @Size(min = 2, max = 1024 * 4)
    private String description;

    @NotNull
    private Double progress;

    @NotNull
    @ManyToOne
    private User createdBy;

    @NotNull
    private LocalDateTime createdOn;

    public Objective() {
    }

    private Objective(Builder builder) {
        this.id = builder.id;
        setTitle(builder.title);
        setOwner(builder.owner);
        setTeam(builder.team);
        setQuarter(builder.quarter);
        setDescription(builder.description);
        setProgress(builder.progress);
        setCreatedBy(builder.createdBy);
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

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
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

    public Double getProgress() {
        return progress;
    }

    public void setProgress(Double progress) {
        this.progress = progress;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(LocalDateTime createdOn) {
        this.createdOn = createdOn;
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
                ", createdBy='" + createdBy + '\'' +
                ", createdOn=" + createdOn +
                '}';
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

    public static final class Builder {
        private @NotNull Long id;
        private @NotBlank @Size(min = 2, max = 250) String title;
        private @NotNull User owner;
        private @NotNull Team team;
        private @NotNull Quarter quarter;
        private @NotBlank @Size(min = 2, max = 1024 * 4) String description;
        private @NotNull Double progress;
        private @NotBlank @Size(min = 2, max = 20) User createdBy;
        private @NotNull LocalDateTime createdOn;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(@NotNull Long id) {
            this.id = id;
            return this;
        }

        public Builder withTitle(@NotBlank @Size(min = 2, max = 250) String title) {
            this.title = title;
            return this;
        }

        public Builder withOwner(@NotNull User owner) {
            this.owner = owner;
            return this;
        }

        public Builder withTeam(@NotNull Team team) {
            this.team = team;
            return this;
        }

        public Builder withQuarter(@NotNull Quarter quarter) {
            this.quarter = quarter;
            return this;
        }

        public Builder withDescription(@NotBlank @Size(min = 2, max = 1024 * 4) String description) {
            this.description = description;
            return this;
        }

        public Builder withProgress(@NotNull Double progress) {
            this.progress = progress;
            return this;
        }

        public Builder withCreatedBy(@NotNull @Size(min = 2, max = 20) User createdBy) {
            this.createdBy = createdBy;
            return this;
        }

        public Builder withCreatedOn(@NotNull LocalDateTime createdOn) {
            this.createdOn = createdOn;
            return this;
        }

        public Objective build() {
            return new Objective(this);
        }
    }
}