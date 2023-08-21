package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Quarter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_quarter")
    private Long id;

    @NotNull
    @Column(unique = true)
    private String label;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    public Quarter() {
    }

    public Quarter(Builder builder) {
        this.id = builder.id;
        setLabel(builder.label);
        setStartDate(builder.startDate);
        setEndDate(builder.endDate);
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

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Quarter{" + "id=" + id + ", label='" + label + '\'' + ", startDate=" + startDate + ", endDate="
                + endDate + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Quarter quarter = (Quarter) o;
        return Objects.equals(id, quarter.id) && Objects.equals(label, quarter.label)
                && Objects.equals(startDate, quarter.startDate) && Objects.equals(endDate, quarter.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, startDate, endDate);
    }

    public static final class Builder {
        private Long id;
        private String label;
        private LocalDate startDate;
        private LocalDate endDate;

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

        public Builder withStartDate(@NotNull LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(@NotNull LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public Quarter build() {
            return new Quarter(this);
        }
    }
}
