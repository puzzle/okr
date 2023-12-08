package ch.puzzle.okr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(indexes = { @Index(name = "idx_objective_title", columnList = "title") })
public class Objective implements WriteableInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_objective")
    private Long id;

    @Version
    private int version;

    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 250, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String title;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Enumerated(EnumType.STRING)
    private State state;

    @Size(max = 4096, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String description;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
    private Team team;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
    private Quarter quarter;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @ManyToOne
    private User createdBy;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    private LocalDateTime createdOn;

    private LocalDateTime modifiedOn;

    @ManyToOne
    private User modifiedBy;

    private transient boolean writeable;

    public Objective() {
    }

    private Objective(Builder builder) {
        id = builder.id;
        version = builder.version;
        setTitle(builder.title);
        setCreatedBy(builder.createdBy);
        setTeam(builder.team);
        setQuarter(builder.quarter);
        setDescription(builder.description);
        setModifiedOn(builder.modifiedOn);
        setState(builder.state);
        setCreatedOn(builder.createdOn);
        setModifiedBy(builder.modifiedBy);
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
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

    public User getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(User modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    @Override
    public boolean isWriteable() {
        return writeable;
    }

    @Override
    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    @Override
    public String toString() {
        return "Objective{" + "id=" + id + ", version=" + version + ", title='" + title + '\'' + ", createdBy="
                + createdBy + ", team=" + team + ", quarter=" + quarter + ", description='" + description + '\''
                + ", modifiedOn=" + modifiedOn + ", state=" + state + ", createdOn=" + createdOn + ", modifiedBy="
                + modifiedBy + ", writeable=" + writeable + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Objective objective = (Objective) o;
        return Objects.equals(id, objective.id) && version == objective.version
                && Objects.equals(title, objective.title) && Objects.equals(createdBy, objective.createdBy)
                && Objects.equals(team, objective.team) && Objects.equals(quarter, objective.quarter)
                && Objects.equals(description, objective.description)
                && Objects.equals(modifiedOn, objective.modifiedOn) && state == objective.state
                && Objects.equals(createdOn, objective.createdOn) && Objects.equals(modifiedBy, objective.modifiedBy);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, title, createdBy, team, quarter, description, modifiedOn, state, createdOn,
                modifiedBy);
    }

    public static final class Builder {
        private Long id;
        private int version;
        private String title;
        private User createdBy;
        private Team team;
        private Quarter quarter;
        private String description;
        private LocalDateTime modifiedOn;
        private State state;
        private LocalDateTime createdOn;
        private User modifiedBy;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withVersion(int version) {
            this.version = version;
            return this;
        }

        public Builder withTitle(String title) {
            this.title = title;
            return this;
        }

        public Builder withCreatedBy(User createdBy) {
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

        public Builder withModifiedBy(User modifiedBy) {
            this.modifiedBy = modifiedBy;
            return this;
        }

        public Objective build() {
            return new Objective(this);
        }
    }
}