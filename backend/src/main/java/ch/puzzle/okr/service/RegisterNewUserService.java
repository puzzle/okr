package ch.puzzle.okr.service;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.business.UserBusinessService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class RegisterNewUserService {

    private final UserBusinessService userBusinessService;

    public RegisterNewUserService(UserBusinessService userBusinessService) {
        this.userBusinessService = userBusinessService;
    }

    public void registerNewUser(SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwt.getClaims();
        User newUser = User.Builder.builder().withUsername(claims.get("preferred_username").toString())
                .withFirstname(claims.get("given_name").toString()).withLastname(claims.get("family_name").toString())
                .withEmail(claims.get("email").toString()).build();
        userBusinessService.getOrCreateUser(newUser);
    }
}
