package ch.puzzle.okr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
// table cannot be named "user" since it is a reserved keyword of Postgres
@Table(name = "person")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_person")
    @SequenceGenerator(name = "sequence_person", allocationSize = 1)
    private Long id;

    @Version
    private int version;

    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 50, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String firstName;

    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 50, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String lastName;

    @Column(unique = true, nullable = false)
    @Email(message = MessageKey.ATTRIBUTE_NOT_VALID)
    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 250, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String email;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL)
    private List<UserTeam> userTeamList = new ArrayList<>();

    @Column(nullable = false, columnDefinition = "boolean default false")
    private boolean okrChampion = false;

    public User() {
    }

    private User(Builder builder) {
        id = builder.id;
        version = builder.version;
        setFirstName(builder.firstName);
        setLastName(builder.lastName);
        setEmail(builder.email);
        setUserTeamList(builder.userTeamList);
        setOkrChampion(builder.okrChampion);
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<UserTeam> getUserTeamList() {
        return userTeamList;
    }

    public void setUserTeamList(List<UserTeam> userTeamList) {
        this.userTeamList = userTeamList;
    }

    public boolean isOkrChampion() {
        return okrChampion;
    }

    public void setOkrChampion(boolean okrChampion) {
        this.okrChampion = okrChampion;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", version=" + version + ", firstName='" + firstName + '\'' + ", lastName='"
                + lastName + '\'' + ", email='" + email + '\'' + ", okrChampion='" + okrChampion + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(version, user.version)
                && Objects.equals(firstName, user.firstName) && Objects.equals(lastName, user.lastName)
                && Objects.equals(email, user.email) && Objects.equals(okrChampion, user.okrChampion);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, firstName, lastName, email, okrChampion);
    }

    public static final class Builder {
        private Long id;
        private int version;
        private String firstName;
        private String lastName;
        private String email;
        private List<UserTeam> userTeamList;
        private boolean okrChampion;

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

        public Builder withFirstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder withLastName(String lastName) {
            this.lastName = lastName;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public Builder withUserTeamList(List<UserTeam> userTeamList) {
            this.userTeamList = userTeamList;
            return this;
        }

        public Builder isOkrChampion(boolean okrChampion) {
            this.okrChampion = okrChampion;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}