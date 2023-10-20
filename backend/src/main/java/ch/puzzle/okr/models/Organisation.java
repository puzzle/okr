package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Entity
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_organisation")
    private Long id;

    @NotNull
    @NotBlank
    private String orgName;

    @ManyToMany(mappedBy = "authorizationOrganisation")
    private List<Team> teams;

    public Organisation() {
    }

    private Organisation(Builder builder) {
        id = builder.id;
        setOrgName(builder.orgName);
        setTeams(builder.teams);
    }

    public Long getId() {
        return id;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String name) {
        this.orgName = name;
    }

    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Organisation that = (Organisation) o;
        return Objects.equals(id, that.id) && Objects.equals(orgName, that.orgName)
                && Objects.equals(teams, that.teams);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orgName, teams);
    }

    @Override
    public String toString() {
        return "Organisation{" + "id=" + id + ", orgName='" + orgName + '\'' + ", teams=" + teams + '}';
    }

    public static final class Builder {
        private Long id;
        private String orgName;

        private List<Team> teams;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withOrgName(String name) {
            this.orgName = name;
            return this;
        }

        public Builder withTeams(List<Team> teams) {
            this.teams = teams;
            return this;
        }

        public Organisation build() {
            return new Organisation(this);
        }
    }
}
