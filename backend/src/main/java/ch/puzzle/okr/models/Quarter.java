package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
public class Quarter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_quarter")
    @NotNull
    private Long id;

    @NotNull
    @Column(unique = true)
    private String label;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private LocalDateTime endDate;

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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @Override
    public String toString() {
        return "Quarter{" + "id=" + id + ", year=" + label + ", startdate=" + startDate + ", enddate=" + endDate + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Quarter quarter = (Quarter) o;
        return Objects.equals(id, quarter.id) && Objects.equals(label, quarter.label) && Objects.equals(startDate, quarter.startDate) && Objects.equals(endDate, quarter.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, label, startDate, endDate);
    }

    public static final class Builder {
        private @NotNull Long id;
        private @NotNull String label;
        private @NotNull LocalDateTime startDate;
        private @NotNull LocalDateTime endDate;

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

        public Builder withStartDate(@NotNull LocalDateTime startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(@NotNull LocalDateTime endDate) {
            this.endDate = endDate;
            return this;
        }

        public Quarter build() {
            return new Quarter(this);
        }
    }
}
