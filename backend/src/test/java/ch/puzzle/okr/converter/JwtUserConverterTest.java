package ch.puzzle.okr.converter;

import ch.puzzle.okr.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.server.ResponseStatusException;

import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.test.util.ReflectionTestUtils.setField;

class JwtUserConverterTest {
    private final JwtUserConverter converter = new JwtUserConverter();
    private final Jwt jwt = defaultJwtToken();

    @BeforeEach
    public void setup() {
        setUsername("preferred_username");
        setFirstname("given_name");
        setLastname("family_name");
        setEmail("email");
    }

    @Test
    void convert_shouldReturnUser_whenValidJwt() {
        User user = converter.convert(jwt);

        assertEquals(User.Builder.builder().withUsername("bkaufmann").withFirstname("Bob").withLastname("Kaufmann")
                .withEmail("kaufmann@puzzle.ch").build(), user);
    }

    @Test
    void convert_shouldThrowException_whenClaimNameDoesNotMatch() {
        setUsername("foo_name");

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> converter.convert(jwt));
        assertEquals(BAD_REQUEST, exception.getStatus());
        assertEquals("can not convert user from token", exception.getReason());
    }

    private void setUsername(String claimRealm) {
        setField(converter, "username", claimRealm);
    }

    private void setFirstname(String claimRoles) {
        setField(converter, "firstname", claimRoles);
    }

    private void setLastname(String roleNamePrefix) {
        setField(converter, "lastname", roleNamePrefix);
    }

    private void setEmail(String email) {
        setField(converter, "email", email);
    }
}
