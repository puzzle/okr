package ch.puzzle.okr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.Objects;

@Entity
// table cannot be named "user" since it is a reserved keyword of Postgres
@Table(name = "person")
public class User implements WriteableInterface {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_person")
    private Long id;

    @Version
    private int version;

    @Column(unique = true)
    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 20, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String username;

    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 50, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String firstname;

    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 50, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String lastname;

    @Email(message = MessageKey.ATTRIBUTE_NOT_VALID)
    @NotBlank(message = MessageKey.ATTRIBUTE_NOT_BLANK)
    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(min = 2, max = 250, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String email;

    private transient boolean writeable;

    public User() {
    }

    private User(Builder builder) {
        id = builder.id;
        version = builder.version;
        setUsername(builder.username);
        setFirstname(builder.firstname);
        setLastname(builder.lastname);
        setEmail(builder.email);
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
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
    public boolean isWriteable() {
        return writeable;
    }

    @Override
    public void setWriteable(boolean writeable) {
        this.writeable = writeable;
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", version=" + version + ", username='" + username + '\'' + ", firstname='"
                + firstname + '\'' + ", lastname='" + lastname + '\'' + ", email='" + email + '\'' + ", writeable="
                + writeable + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id) && Objects.equals(version, user.version)
                && Objects.equals(username, user.username) && Objects.equals(firstname, user.firstname)
                && Objects.equals(lastname, user.lastname) && Objects.equals(email, user.email)
                && Objects.equals(writeable, user.writeable);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, version, username, firstname, lastname, email, writeable);
    }

    public static final class Builder {
        private Long id;
        private int version;
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

        public Builder withVersion(int version) {
            this.version = version;
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