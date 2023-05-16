package ch.puzzle.okr.models;

import ch.puzzle.okr.mapper.KeyResultMeasureMapper;

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
    @NotNull
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 250)
    private String title;

    @NotNull
    @ManyToOne
    private User owner;

    @NotNull
    @ManyToOne
    private Team team;

    @NotNull
    @ManyToOne
    private Quarter quarter;

    @Size(max = 4096)
    private String description;

    private Long progress;

    @NotNull
    private LocalDateTime modifiedOn;

    public Objective() {
    }

    private Objective(Builder builder) {
        id = builder.id;
        setTitle(builder.title);
        setOwner(builder.owner);
        setTeam(builder.team);
        setQuarter(builder.quarter);
        setDescription(builder.description);
        setProgress(builder.progress);
        setModifiedOn(builder.modifiedOn);
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

    @Override
    public String toString() {
        return "Objective{" + "id=" + id + ", title='" + title + '\'' + ", owner=" + owner + ", team=" + team
                + ", quarter=" + quarter + ", description='" + description + '\'' + ", progress=" + progress
                + ", modifiedOn=" + modifiedOn + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Objective objective = (Objective) o;
        return Objects.equals(id, objective.id) && Objects.equals(title, objective.title)
                && Objects.equals(owner, objective.owner) && Objects.equals(team, objective.team)
                && Objects.equals(quarter, objective.quarter) && Objects.equals(description, objective.description)
                && Objects.equals(progress, objective.progress) && Objects.equals(modifiedOn, objective.modifiedOn);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, owner, team, quarter, description, progress, modifiedOn);
    }

    public static final class Builder {
        private Long id;
        private String title;
        private User owner;
        private Team team;
        private Quarter quarter;
        private String description;
        private Long progress;
        private LocalDateTime modifiedOn;

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

        public Builder withOwner(User owner) {
            this.owner = owner;
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

        public Objective build() {
            return new Objective(this);
        }
    }
}