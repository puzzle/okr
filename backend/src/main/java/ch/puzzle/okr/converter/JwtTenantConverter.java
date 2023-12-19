package ch.puzzle.okr.converter;

import ch.puzzle.okr.ErrorKey;
import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.Tenant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Map;

import static ch.puzzle.okr.Constants.TENANT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Component
public class JwtTenantConverter implements Converter<Jwt, Tenant> {

    private static final Logger logger = LoggerFactory.getLogger(JwtTenantConverter.class);
    private static final String JWT_ISS = "iss";

    @Override
    public Tenant convert(Jwt token) {
        Map<String, Object> claims = token.getClaims();
        logger.debug("claims {}", claims);

        String iss;
        try {
            iss = claims.get(JWT_ISS).toString();
        } catch (Exception e) {
            logger.error("can not convert tenant from claims {}", claims);
            throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.CONVERT_TOKEN, TENANT);
        }
        return issToTenant(iss);
    }

    private Tenant issToTenant(String iss) {
        if (iss.endsWith("/pitc")) {
            return Tenant.PUZZLE;
        } else if (iss.endsWith("/aareguru")) {
            return Tenant.AAREGURU;
        }
        logger.error("can not determine Tenant from iss");
        throw new OkrResponseStatusException(BAD_REQUEST, ErrorKey.CONVERT_TOKEN, iss);
    }
}
