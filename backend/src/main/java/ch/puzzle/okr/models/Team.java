package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
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

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "team_organisation", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "organisation_id"))
    private List<Organisation> authorizationOrganisation;

    public Team() {
    }

    private Team(Builder builder) {
        id = builder.id;
        setName(builder.name);
        setAuthorizationOrganisation(builder.authorizationOrganisation);
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

    public List<Organisation> getAuthorizationOrganisation() {
        return authorizationOrganisation;
    }

    public void setAuthorizationOrganisation(List<Organisation> authorizationOrganisation) {
        this.authorizationOrganisation = authorizationOrganisation;
    }

    @Override
    public String toString() {
        return "Team{" + "id=" + id + ", name='" + name + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) && Objects.equals(name, team.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }

    public static final class Builder {
        private Long id;
        private String name;

        private List<Organisation> authorizationOrganisation;

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

        public Builder withAuthorizationOrganisation(List<Organisation> authOrg) {
            this.authorizationOrganisation = authOrg;
            return this;
        }

        public Team build() {
            return new Team(this);
        }
    }
}