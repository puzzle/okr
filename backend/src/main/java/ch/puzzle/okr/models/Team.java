package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_team")
    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 2, max = 250)
    private String name;

    public Team() {
    }

    private Team(Builder builder) {
        id = builder.id;
        setName(builder.name);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Team{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public static final class Builder {
        private @NotNull Long id;
        private @NotBlank @Size(min = 2, max = 250) String name;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(@NotNull Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(@NotBlank @Size(min = 2, max = 250) String name) {
            this.name = name;
            return this;
        }

        public Team build() {
            return new Team(this);
        }
    }
}