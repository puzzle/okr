package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Team implements WriteableInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_team")
    private Long id;

    @NotBlank(message = ErrorMsg.ATTRIBUTE_EMPTY_ON_MODEL)
    @NotNull(message = ErrorMsg.ATTRIBUTE_NULL_ON_MODEL)
    @Size(min = 2, max = 250, message = ErrorMsg.ATTRIBUTE_SIZE_BETWEEN)
    private String name;

    @Version
    private int version;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "team_organisation", joinColumns = @JoinColumn(name = "team_id"), inverseJoinColumns = @JoinColumn(name = "organisation_id"))
    private List<Organisation> authorizationOrganisation;

    private transient boolean writeable;

    public Team() {
    }

    private Team(Builder builder) {
        id = builder.id;
        version = builder.version;
        setName(builder.name);
        setAuthorizationOrganisation(builder.authorizationOrganisation);
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
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
    public boolean isWriteable() {
        return writeable;
    }

    @Override
    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    @Override
    public String toString() {
        return "Team{" + "id=" + id + ", version=" + version + ", name='" + name + ", writeable=" + writeable + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Team team = (Team) o;
        return Objects.equals(id, team.id) && Objects.equals(version, team.version) && Objects.equals(name, team.name)
                && Objects.equals(writeable, team.writeable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, name, writeable);
    }

    public static final class Builder {
        private Long id;
        private int version;
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

        public Builder withVersion(int version) {
            this.version = version;
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