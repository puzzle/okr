package ch.puzzle.okr.security;

import ch.puzzle.okr.multitenancy.TenantContext;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;

import static ch.puzzle.okr.multitenancy.TenantContext.DEFAULT_TENANT_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationEventsTest {

    public static final String TENANT_FROM_TOKEN = "pitc";

    @DisplayName("onSuccess() puts Token from AuthenticationSuccessEvent in TenantContext")
    @Test
    void onSuccessPutsTokenFromAuthenticationSuccessEventInTenantContext() {
        // arrange
        Jwt tokenMock = mock(Jwt.class);

        AuthenticationSuccessEvent successEvent = new AuthenticationSuccessEvent(mock(Authentication.class));
        when(successEvent.getAuthentication().getPrincipal()).thenReturn(tokenMock);

        JwtHelper jwtHelperMock = mock(JwtHelper.class);
        when(jwtHelperMock.getTenantFromToken(tokenMock)).thenReturn(TENANT_FROM_TOKEN);

        // pre-assert
        assertDefaultTenantIsInTenantContext();

        // act
        AuthenticationEvents authenticationEvents = new AuthenticationEvents(jwtHelperMock);
        authenticationEvents.onSuccess(successEvent);

        // assert
        assertTenantFromTokenIsInTenantContext();
        verifyGetTenantFromTokenIsCalledWithTokenFromAuthenticationSuccessEvent(jwtHelperMock, tokenMock);
    }

    private void assertDefaultTenantIsInTenantContext() {
        assertEquals(DEFAULT_TENANT_ID, TenantContext.getCurrentTenant());
    }

    private void assertTenantFromTokenIsInTenantContext() {
        assertEquals(TENANT_FROM_TOKEN, TenantContext.getCurrentTenant());
    }

    private void verifyGetTenantFromTokenIsCalledWithTokenFromAuthenticationSuccessEvent(JwtHelper jwtHelper,
            Jwt token) {
        verify(jwtHelper).getTenantFromToken(token);
    }

}
