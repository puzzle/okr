package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
//table cannot be named "user" since it is a reserved keyword of Postgres
@Table(name= "person")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_person")
    @NotNull
    private Long id;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 20)
    private String username;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 50)
    private String firstname;

    @NotBlank
    @NotNull
    @Size(min = 2, max = 50)
    private String lastname;

    @Email
    @NotNull
    @Size(min = 2, max = 250)
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
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(username, user.username) && Objects.equals(firstname, user.firstname) && Objects.equals(lastname, user.lastname) && Objects.equals(email, user.email);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, username, firstname, lastname, email);
    }

    public static final class Builder {
        private @NotNull Long id;
        private @NotBlank @Size(min = 2, max = 20) String username;
        private @NotBlank @Size(min = 2, max = 50) String firstname;
        private @NotBlank @Size(min = 2, max = 50) String lastname;
        private @Email @Size(min = 2, max = 250) String email;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withId(@NotNull Long id) {
            this.id = id;
            return this;
        }

        public Builder withUsername(@NotBlank @Size(min = 2, max = 20) String username) {
            this.username = username;
            return this;
        }

        public Builder withFirstname(@NotBlank @Size(min = 2, max = 50) String firstname) {
            this.firstname = firstname;
            return this;
        }

        public Builder withLastname(@NotBlank @Size(min = 2, max = 50) String lastname) {
            this.lastname = lastname;
            return this;
        }

        public Builder withEmail(@Email @Size(min = 2, max = 250) String email) {
            this.email = email;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}