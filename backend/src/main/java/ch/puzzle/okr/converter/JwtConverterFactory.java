package ch.puzzle.okr.converter;

import ch.puzzle.okr.models.User;
import org.springframework.context.ApplicationContext;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtConverterFactory {

    private final ApplicationContext appContext;
    private Converter<Jwt, List<String>> jwtOrganisationConverter;
    private Converter<Jwt, User> jwtUserConverter;

    public JwtConverterFactory(ApplicationContext appContext) {
        this.appContext = appContext;
    }

    public synchronized Converter<Jwt, List<String>> getJwtOrganisationConverter() {
        if (jwtOrganisationConverter == null) {
            // place to load configured converter instead of default converter
            jwtOrganisationConverter = appContext.getBean(JwtOrganisationConverter.class);
        }
        return jwtOrganisationConverter;
    }

    public synchronized Converter<Jwt, User> getJwtUserConverter() {
        if (jwtUserConverter == null) {
            // place to load configured converter instead of default converter
            jwtUserConverter = appContext.getBean(JwtUserConverter.class);
        }
        return jwtUserConverter;
    }
}
