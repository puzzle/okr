package ch.puzzle.okr.converter;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Tenant;
import org.junit.jupiter.api.Test;
import org.springframework.security.oauth2.jwt.Jwt;

import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static ch.puzzle.okr.TestHelper.mockJwtToken;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class JwtTenantConverterTest {

    private final JwtTenantConverter converter = new JwtTenantConverter();

    private final Jwt jwt = defaultJwtToken();

    @Test
    void convertShouldReturnTenantWhenValidIss() {
        var tenant = converter.convert(jwt);
        assertEquals(tenant, Tenant.PUZZLE);
        tenant = converter.convert(mockDefaultJwtWithIss("http://localhost:8544/realms/pitc"));
        assertEquals(Tenant.PUZZLE, tenant);
    }

    @Test
    void convertShouldReturnErrorWhenNoIss() {
        assertThrows(OkrResponseStatusException.class, () -> {
            converter.convert(mockDefaultJwtWithIss(null));
        });
    }

    @Test
    void convertShouldReturnErrorWhenWrongIss() {
        assertThrows(OkrResponseStatusException.class, () -> {
            converter.convert(mockDefaultJwtWithIss("wrongIss"));
        });
    }

    private Jwt mockDefaultJwtWithIss(String iss) {
        return mockJwtToken("username", "firstname", "lastname", "email@email.com", iss);
    }
}