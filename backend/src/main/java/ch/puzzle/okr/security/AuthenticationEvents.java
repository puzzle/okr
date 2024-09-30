package ch.puzzle.okr.security;

import ch.puzzle.okr.multitenancy.TenantContext;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationEvents {
    private final JwtHelper jwtHelper;

    public AuthenticationEvents(JwtHelper jwtHelper) {
        this.jwtHelper = jwtHelper;
    }

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent success) {
        Jwt token = (Jwt) success.getAuthentication().getPrincipal();

        TenantContext.setCurrentTenant(jwtHelper.getTenantFromToken(token));
    }
}