package ch.puzzle.okr.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_team")
    private Long id;

    @NotBlank(message = "Missing attribute name when saving team")
    @NotNull(message = "Attribute name can not be null when saving team")
    @Size(min = 2, max = 250, message = "Attribute name must have size between 2 and 250 characters when saving team")
    private String name;

    @NotBlank(message = "Missing attribute role name when saving team")
    @NotNull(message = "Attribute role name can not be null when saving team")
    @Size(min = 2, max = 250, message = "Attribute role name must have size between 5 and 250 characters when saving team")
    private String roleName;

    public Team() {
    }

    private Team(Builder builder) {
        id = builder.id;
        setName(builder.name);
        setRoleName(builder.roleName);
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

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "Team{" + "id=" + id + ", name='" + name + ", roleName='" + roleName + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) && Objects.equals(name, team.name)
                && Objects.equals(roleName, team.roleName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, roleName);
    }

    public static final class Builder {
        private Long id;
        private String name;

        private String roleName;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withRoleName(String roleName) {
            this.roleName = roleName;
            return this;
        }

        public Team build() {
            return new Team(this);
        }
    }
}