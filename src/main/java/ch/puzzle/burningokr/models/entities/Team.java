package ch.puzzle.burningokr.models.entities;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 15)
    @Column(name = "name")
    private String name;

    public Team() {
    }

    private Team(Builder builder) {
        setName(builder.name);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) && Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static final class Builder {
        private @NotBlank
        @Size(min = 3, max = 15) String name;

        public Builder() {
        }

        public Builder name(@NotBlank @Size(min = 3, max = 15) String val) {
            name = val;
            return this;
        }

        public Team build() {
            return new Team(this);
        }
    }
}