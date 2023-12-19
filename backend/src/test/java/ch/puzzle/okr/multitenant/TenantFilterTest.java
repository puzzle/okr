package ch.puzzle.okr.multitenant;

import ch.puzzle.okr.converter.JwtConverterFactory;
import ch.puzzle.okr.converter.JwtTenantConverter;
import ch.puzzle.okr.models.Tenant;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.io.IOException;

import static ch.puzzle.okr.TestHelper.defaultJwtToken;
import static ch.puzzle.okr.TestHelper.setSecurityContext;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class TenantFilterTest {

    @Mock
    HttpServletRequest request;

    @Mock
    ServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    JwtConverterFactory jwtConverterFactory;

    @InjectMocks
    TenantFilter tenantFilter;

    @BeforeEach
    void setUp() {
    }

    @Test
    void doFilterShouldSetTenantIfJwtExists() throws ServletException, IOException {
        when(jwtConverterFactory.getJwtTenantConverter()).thenReturn(new JwtTenantConverter());
        setSecurityContext(defaultJwtToken());
        tenantFilter.doFilter(request, response, filterChain);
        assertEquals(Tenant.PUZZLE, TenantContext.getCurrentTenant());
    }

    @Test
    void doFilterShouldNotSetTenantIfNotJwt() throws ServletException, IOException {
        emptySecurityContext();
        tenantFilter.doFilter(request, response, filterChain);
        assertNull(TenantContext.getCurrentTenant());
    }

    private void emptySecurityContext() {
        SecurityContextHolder.setContext(
                new SecurityContextImpl(new AnonymousAuthenticationToken("anonymousUser", "anonymousUser", AuthorityUtils.createAuthorityList(
                        "ROLE_ANONYMOUS"))
                ));
    }
}