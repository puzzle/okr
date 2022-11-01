package ch.puzzle.okr.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }
}
