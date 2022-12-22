package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Quarter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_quarter")
    @NotNull
    private Long id;

    @NotNull
    private String label;

    public Quarter() {
    }

    public Quarter(Builder builder) {
        this.id = builder.id;
        setLabel(builder.label);
    }

    public Long getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "Quarter{" + "id=" + id + ", year=" + label + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Quarter quarter = (Quarter) o;
        return Objects.equals(id, quarter.id) && Objects.equals(label, quarter.label);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label);
    }

    public static final class Builder {
        private @NotNull Long id;
        private @NotNull String label;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(@NotNull Long id) {
            this.id = id;
            return this;
        }

        public Builder withLabel(@NotNull String label) {
            this.label = label;
            return this;
        }

        public Quarter build() {
            return new Quarter(this);
        }
    }
}
