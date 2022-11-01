package ch.puzzle.okr.models;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.util.Objects;

@Entity
//table cannot be named "user" since it is a reserved keyword of Postgres
@Table(name= "client")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_client")
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


    private User(Builder builder) {
        id = builder.id;
        setUsername(builder.username);
        setFirstname(builder.firstname);
        setLastname(builder.lastname);
        setEmail(builder.email);
    }

    public User() {
    }

    public Long getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
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