package ch.puzzle.okr.converter;

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
public class JwtOrganisationConverter implements Converter<Jwt, List<String>> {

    private static final Logger logger = LoggerFactory.getLogger(JwtOrganisationConverter.class);

    @Value("${okr.jwt.claim.realm}")
    private String claimRealm;
    @Value("${okr.jwt.claim.organisations}")
    private String claimOrganisations;
    @Value("${okr.jwt.claim.organisation.name.prefix}")
    private String organisationNamePrefix;

    @Override
    public List<String> convert(Jwt token) {
        Map<String, Collection<String>> realmAccess = token.getClaim(claimRealm);
        logger.debug("realmAccess {}", realmAccess);
        if (!CollectionUtils.isEmpty(realmAccess)) {
            Collection<String> organisations = realmAccess.get(claimOrganisations);
            if (!CollectionUtils.isEmpty(organisations)) {
                return organisations.stream().filter(o -> o.startsWith(organisationNamePrefix)).toList();
            }
        }
        logger.warn("empty list of realms or organisations {}", realmAccess);
        return List.of();
    }
}
