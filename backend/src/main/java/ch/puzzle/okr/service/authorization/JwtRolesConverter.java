package ch.puzzle.okr.service.authorization;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@Component
public class JwtRolesConverter implements Converter<Jwt, Collection<String>> {

    private static final Logger logger = LoggerFactory.getLogger(JwtRolesConverter.class);

    @Value("${okr.jwt.claim.realm}")
    private String claimRealm;
    @Value("${okr.jwt.claim.roles}")
    private String claimRoles;
    @Value("${okr.team.role.name.prefix}")
    private String roleNamePrefix;

    @Override
    public List<String> convert(Jwt token) {
        Map<String, Collection<String>> realmAccess = token.getClaim(claimRealm);
        logger.debug("claimRealm {}", claimRealm);
        if (!CollectionUtils.isEmpty(realmAccess)) {
            Collection<String> roles = realmAccess.get(claimRoles);
            if (!CollectionUtils.isEmpty(roles)) {
                return roles.stream().filter(role -> role.startsWith(roleNamePrefix)).toList();
            }
        }
        return List.of();
    }
}
