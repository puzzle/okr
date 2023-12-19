package ch.puzzle.okr.multitenant;

import ch.puzzle.okr.converter.JwtConverterFactory;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Component
class TenantFilter extends GenericFilterBean {

    private final JwtConverterFactory jwtConverterFactory;

    public TenantFilter(
            JwtConverterFactory jwtConverterFactory
    ) {
        this.jwtConverterFactory = jwtConverterFactory;
    }

    @Override
    @Order(2)
    public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();

        // authentication is in first request not set (before redirecting to OAuth)
        if (authentication instanceof AnonymousAuthenticationToken) {
            chain.doFilter(request, response);
            return;
        }

        setTenant(authentication);

        chain.doFilter(request, response);
    }

    private void setTenant(Authentication authentication) {
        Jwt token = (Jwt) authentication.getPrincipal();
        var converter = jwtConverterFactory.getJwtTenantConverter();
        var tenantName = converter.convert(token);
        TenantContext.setCurrentTenant(tenantName);
    }
}
