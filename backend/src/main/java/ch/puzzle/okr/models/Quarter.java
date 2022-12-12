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
    private Integer year;

    @NotNull
    private Integer number;

    public Quarter() {
    }

    public Quarter(Builder builder) {
        this.id = builder.id;
        setYear(builder.year);
        setNumber(builder.number);
    }

    public Long getId() {
        return id;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "Quarter{" + "id=" + id + ", year=" + year + ", number=" + number + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Quarter quarter = (Quarter) o;
        return Objects.equals(id, quarter.id) && Objects.equals(year, quarter.year)
                && Objects.equals(number, quarter.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, number);
    }

    public static final class Builder {
        private @NotNull Long id;
        private @NotNull Integer year;
        private @NotNull Integer number;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(@NotNull Long id) {
            this.id = id;
            return this;
        }

        public Builder withYear(@NotNull Integer year) {
            this.year = year;
            return this;
        }

        public Builder withNumber(@NotNull Integer number) {
            this.number = number;
            return this;
        }

        public Quarter build() {
            return new Quarter(this);
        }
    }
}
