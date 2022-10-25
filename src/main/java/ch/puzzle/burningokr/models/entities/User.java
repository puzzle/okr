package ch.puzzle.burningokr.models.entities;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Objects;

@Entity
@Table(name = "owner")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "username")
    private String username;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "firstname")
    private String firstname;

    @NotBlank
    @Size(min = 3, max = 20)
    @Column(name = "lastname")
    private String lastname;

    @Email
    @Column(name = "email")
    private String email;

    private User(Builder builder) {
        id = builder.id;
        setUsername(builder.username);
        setFirstname(builder.firstname);
        setLastname(builder.lastname);
        setEmail(builder.email);
    }

    public User() {
    }

    public String getEmail() {
        return email;
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

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public static final class Builder {
        private Long id;
        private @NotBlank @Size(min = 3, max = 20) String username;
        private @NotBlank @Size(min = 3, max = 20) String firstname;
        private @NotBlank @Size(min = 3, max = 20) String lastname;
        private @Email String email;

        public Builder() {
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public Builder username(@NotBlank @Size(min = 3, max = 20) String val) {
            username = val;
            return this;
        }

        public Builder firstname(@NotBlank @Size(min = 3, max = 20) String val) {
            firstname = val;
            return this;
        }

        public Builder lastname(@NotBlank @Size(min = 3, max = 20) String val) {
            lastname = val;
            return this;
        }

        public Builder email(@Email String val) {
            email = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}