package ch.puzzle.okr;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.security.JwtHelper;
import ch.puzzle.okr.service.authorization.AuthorizationService;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
class SpringSecurityAuditorAware implements AuditorAware<User> {

    private final JwtHelper jwtHelper;
    private final AuthorizationService authorizationService;

    public SpringSecurityAuditorAware(JwtHelper jwtHelper, AuthorizationService authorizationService) {
        this.jwtHelper = jwtHelper;
        this.authorizationService = authorizationService;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional
                .ofNullable(SecurityContextHolder.getContext())
                .map(SecurityContext::getAuthentication)
                .filter(Authentication::isAuthenticated)
                .map(Authentication::getPrincipal)
                .map(e -> (Jwt) e)
                .map(jwtHelper::getUserFromJwt)
                .map(e -> authorizationService.updateOrAddAuthorizationUser().user());
    }
}
