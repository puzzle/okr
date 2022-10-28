package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "quarter")
public class Quarter {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_quarter")
    @NotNull
    private Long id;

    @NotNull
    private int year;

    @NotNull
    private Integer number;

    public Quarter() {
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}
