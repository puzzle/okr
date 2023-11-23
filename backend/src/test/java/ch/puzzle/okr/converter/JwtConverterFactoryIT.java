package ch.puzzle.okr.converter;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

@SpringIntegrationTest
public class JwtConverterFactoryIT {

    @Autowired
    private JwtConverterFactory jwtConverterFactory;

    @Test
    void getJwtOrganisationConverter() {
        Converter<Jwt, List<String>> converter = jwtConverterFactory.getJwtOrganisationConverter();
        assertNotNull(converter);
        assertSame(converter, jwtConverterFactory.getJwtOrganisationConverter());
    }

    @Test
    void getJwtUserConverter() {
        Converter<Jwt, User> converter = jwtConverterFactory.getJwtUserConverter();
        assertNotNull(converter);
        assertSame(converter, jwtConverterFactory.getJwtUserConverter());
    }
}
