package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Entity
public class Quarter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_quarter")
    @NotNull
    private Long quarterId;

    @NotNull
    private String quarterLabel;

    public Quarter() {
    }

    public Quarter(Builder builder) {
        this.quarterId = builder.id;
        setQuarterLabel(builder.label);
    }

    public Long getQuarterId() {
        return quarterId;
    }

    public String getQuarterLabel() {
        return quarterLabel;
    }

    public void setQuarterLabel(String label) {
        this.quarterLabel = label;
    }

    @Override
    public String toString() {
        return "Quarter{" + "id=" + quarterId + ", year=" + quarterLabel + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Quarter quarter = (Quarter) o;
        return Objects.equals(quarterId, quarter.quarterId) && Objects.equals(quarterLabel, quarter.quarterLabel);
    }

    @Override
    public int hashCode() {
        return Objects.hash(quarterId, quarterLabel);
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
