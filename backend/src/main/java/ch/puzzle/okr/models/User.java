package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "person")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_person")
    @NotNull
    private Long id;

    @NotBlank
    @Size(min = 2, max = 20)
    private String username;

    @NotBlank
    @Size(min = 2, max = 50)
    private String firstname;

    @NotBlank
    @Size(min = 2, max = 50)
    private String lastname;

    @Email
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

    public String getEmail() {
        return email;
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
        private @NotNull Long id;
        private @NotBlank @Size(min = 2, max = 20) String username;
        private @NotBlank @Size(min = 2, max = 50) String firstname;
        private @NotBlank @Size(min = 2, max = 50) String lastname;
        private @Email @Size(min = 2, max = 250) String email;

        public Builder() {
        }

        public static User.Builder builder() {
            return new User.Builder();
        }

        public Builder withId(@NotNull Long val) {
            id = val;
            return this;
        }

        public Builder withUsername(@NotBlank @Size(min = 2, max = 20) String val) {
            username = val;
            return this;
        }

        public Builder withFirstname(@NotBlank @Size(min = 2, max = 50) String val) {
            firstname = val;
            return this;
        }

        public Builder withLastname(@NotBlank @Size(min = 2, max = 50) String val) {
            lastname = val;
            return this;
        }

        public Builder withEmail(@Email @Size(min = 2, max = 250) String val) {
            email = val;
            return this;
        }

        public User build() {
            return new User(this);
        }
    }
}