package ch.puzzle.okr.converter;

import ch.puzzle.okr.TestHelper;
import ch.puzzle.okr.dto.ErrorDto;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
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
    void convertShouldReturnUserWhenValidJwt() {
        User user = converter.convert(jwt);

        assertEquals(User.Builder.builder().withUsername("bkaufmann").withFirstname("Bob").withLastname("Kaufmann")
                .withEmail("kaufmann@puzzle.ch").build(), user);
    }

    @Test
    void convertShouldThrowExceptionWhenClaimNameDoesNotMatch() {
        setUsername("foo_name");

        OkrResponseStatusException exception = assertThrows(OkrResponseStatusException.class,
                () -> converter.convert(jwt));

        List<ErrorDto> expectedErrors = List.of(new ErrorDto("CONVERT_TOKEN", List.of("User")));

        assertEquals(BAD_REQUEST, exception.getStatus());
        assertThat(expectedErrors).hasSameElementsAs(exception.getErrors());
        assertTrue(TestHelper.getAllErrorKeys(expectedErrors).contains(exception.getReason()));
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
