package ch.puzzle.burningokr.models.entities;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "quarter")
public class Quarter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "year")
    private LocalDateTime year;

    @Column(name = "number")
    private Integer number;

    private Quarter(Builder builder) {
        id = builder.id;
        setYear(builder.year);
        setNumber(builder.number);
    }

    public Quarter() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Quarter quarter = (Quarter) o;
        return Objects.equals(id, quarter.id) && Objects.equals(year, quarter.year) && Objects.equals(number, quarter.number);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, year, number);
    }

    @Override
    public String toString() {
        return "Quarter{" +
                "id=" + id +
                ", year=" + year +
                ", number=" + number +
                '}';
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public LocalDateTime getYear() {
        return year;
    }

    public void setYear(LocalDateTime year) {
        this.year = year;
    }

    public static final class Builder {
        private Long id;
        private LocalDateTime year;
        private Integer number;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder year(LocalDateTime val) {
            year = val;
            return this;
        }

        public Builder number(Integer val) {
            number = val;
            return this;
        }

        public Quarter build() {
            return new Quarter(this);
        }
    }
}