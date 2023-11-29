package ch.puzzle.okr.converter;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;

import static ch.puzzle.okr.Constants.USER;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class JwtUserConverter implements Converter<Jwt, User> {

    private static final Logger logger = LoggerFactory.getLogger(JwtUserConverter.class);

    @Value("${okr.jwt.user.username}")
    private String username;
    @Value("${okr.jwt.user.firstname}")
    private String firstname;
    @Value("${okr.jwt.user.lastname}")
    private String lastname;
    @Value("${okr.jwt.user.email}")
    private String email;

    @Override
    public User convert(Jwt token) {
        Map<String, Object> claims = token.getClaims();
        logger.debug("claims {}", claims);

        try {
            return User.Builder.builder().withUsername(claims.get(username).toString())
                    .withFirstname(claims.get(firstname).toString()).withLastname(claims.get(lastname).toString())
                    .withEmail(claims.get(email).toString()).build();
        } catch (Exception e) {
            logger.warn("can not convert user from claims {}", claims);
            throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.CONVERT_TOKEN, USER);
        }
    }
}
