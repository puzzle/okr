package ch.puzzle.okr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;
import java.util.Objects;

@Entity
public class Team implements WriteableInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_team")
    @SequenceGenerator(name = "sequence_team", allocationSize = 1)
    private Long id;

    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 250, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String name;

    @Version
    private int version;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "team", cascade = CascadeType.ALL)
    private List<UserTeam> userTeamList;

    @Transient
    private boolean writeable;

    public Team() {
    }

    private Team(Builder builder) {
        id = builder.id;
        version = builder.version;
        setName(builder.name);
        setUserTeamList(builder.userTeamList);
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

    public List<UserTeam> getUserTeamList() {
        return userTeamList;
    }

    public void setUserTeamList(List<UserTeam> userTeamList) {
        this.userTeamList = userTeamList;
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

        private List<UserTeam> userTeamList;

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

        public Builder withUserTeamList(List<UserTeam> userTeamList) {
            this.userTeamList = userTeamList;
            return this;
        }

        public Team build() {
            return new Team(this);
        }
    }
}