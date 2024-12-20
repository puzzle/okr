package ch.puzzle.okr.models;

import jakarta.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "person_team")
public class UserTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_person_team")
    @SequenceGenerator(name = "sequence_person_team", allocationSize = 1)
    private Long id;

    @Version
    private int version;

    @ManyToOne()
    @JoinColumn(name = "person_id")
    private User user;

    @ManyToOne()
    @JoinColumn(name = "team_id")
    private Team team;

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean isTeamAdmin = false;

    private UserTeam() {
    }

    private UserTeam(Builder builder) {
        this.id = builder.id;
        this.version = builder.version;
        this.user = builder.user;
        this.team = builder.team;
        this.isTeamAdmin = builder.isTeamAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public boolean isTeamAdmin() {
        return isTeamAdmin;
    }

    public void setTeamAdmin(boolean teamAdmin) {
        isTeamAdmin = teamAdmin;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserTeam userTeam = (UserTeam) o;
        return version == userTeam.version && isTeamAdmin == userTeam.isTeamAdmin && Objects.equals(id, userTeam.id)
               && Objects.equals(user, userTeam.user) && Objects.equals(team, userTeam.team);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, user, team, isTeamAdmin);
    }

    @Override
    public String toString() {
        return "UserTeam{" + "id=" + id + ", version=" + version + ", user=" + user + ", team=" + team
               + ", isTeamAdmin=" + isTeamAdmin + '}';
    }

    public static class Builder {
        private Long id;
        private int version;
        private User user;
        private Team team;
        private boolean isTeamAdmin;

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

        public Builder withUser(User user) {
            this.user = user;
            return this;
        }

        public Builder withTeam(Team team) {
            this.team = team;
            return this;
        }

        public Builder withTeamAdmin(boolean isAdmin) {
            this.isTeamAdmin = isAdmin;
            return this;
        }

        public UserTeam build() {
            return new UserTeam(this);
        }
    }

}
