package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
// table cannot be named "user" since it is a reserved keyword of Postgres
@Table(name = "person")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_person")
    private Long id;

    @Column(unique = true)
    @NotBlank(message = "Missing attribute username when saving user")
    @NotNull(message = "Attribute username can not be null when saving user")
    @Size(min = 2, max = 20, message = "Attribute username must have size between 2 and 20 characters when saving user")
    private String username;

    @NotBlank(message = "Missing attribute firstname when saving user")
    @NotNull(message = "Attribute firstname can not be null when saving user")
    @Size(min = 2, max = 50, message = "Attribute firstname must have size between 2 and 50 characters when saving user")
    private String firstname;

    @NotBlank(message = "Missing attribute lastname when saving user")
    @NotNull(message = "Attribute lastname can not be null when saving user")
    @Size(min = 2, max = 50, message = "Attribute lastname must have size between 2 and 50 characters when saving user")
    private String lastname;

    @Email(message = "Attribute email should be valid when saving user")
    @NotNull(message = "Attribute email can not be null when saving user")
    @Size(min = 2, max = 250, message = "Attribute email must have size between 2 and 250 characters when saving user")
    private String email;

    public User() {
    }

    private User(Builder builder) {
        id = builder.id;
        setUsername(builder.username);
        setFirstname(builder.firstname);
        setLastname(builder.lastname);
        setEmail(builder.email);
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", username='" + username + '\'' + ", firstname='" + firstname + '\''
                + ", lastname='" + lastname + '\'' + ", email='" + email + '\'' + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username)
                && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname)
                && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, lastname, email);
    }

    public static final class Builder {
        private Long id;
        private String username;
        private String firstname;
        private String lastname;
        private String email;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withUsername(String username) {
            this.username = username;
            return this;
        }

        public Builder withFirstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder withLastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder withEmail(String email) {
            this.email = email;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}