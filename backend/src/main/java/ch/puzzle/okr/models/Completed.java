package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Completed {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_completed")
    private Long id;

    @Version
    private int version;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @OneToOne
    private Objective objective;

    @Size(max = 4096, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String comment;

    public Completed() {
    }

    private Completed(Builder builder) {
        id = builder.id;
        version = builder.version;
        setObjective(builder.objective);
        setComment(builder.comment);
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public Objective getObjective() {
        return objective;
    }

    public void setObjective(Objective objective) {
        this.objective = objective;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public String toString() {
        return "Completed{" + "id=" + id + ", version=" + version + ", objective=" + objective + ", comment='" + comment
                + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Completed completed = (Completed) o;
        return Objects.equals(id, completed.id) && version == completed.version
                && Objects.equals(objective, completed.objective) && Objects.equals(comment, completed.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, objective, comment);
    }

    public static final class Builder {
        private Long id;
        private int version;
        private Objective objective;
        private String comment;

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

        public Builder withObjective(Objective objective) {
            this.objective = objective;
            return this;
        }

        public Builder withComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Completed build() {
            return new Completed(this);
        }
    }
}