package ch.puzzle.okr.service;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
public class RegisterNewUserService {

    private final UserRepository userRepository;

    public RegisterNewUserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // TODO: Should not call by every http request! Use cache and maybe a interceptor.
    public void registerNewUser(SecurityContext securityContext) {
        Authentication authentication = securityContext.getAuthentication();
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Map<String, Object> claims = jwt.getClaims();
        getOrCreateUser(claims);
    }

    private synchronized User getOrCreateUser(Map<String, Object> claims) {
        Optional<User> user = userRepository.findByUsername(claims.get("preferred_username").toString());
        return user.orElseGet(() -> {
            User newUser = User.Builder.builder().withUsername(claims.get("preferred_username").toString())
                    .withFirstname(claims.get("given_name").toString())
                    .withLastname(claims.get("family_name").toString()).withEmail(claims.get("email").toString())
                    .build();
            return userRepository.save(newUser);
        });
    }
}
