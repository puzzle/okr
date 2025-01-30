package ch.puzzle.okr.test;

import ch.puzzle.okr.models.User;
import java.util.List;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAuthUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockAuthUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        User user = User.Builder
                .builder()
                .withEmail(annotation.email())
                .withFirstName("Mocked user first name for test")
                .withLastName("mocked user last name for test")
                .build();
        context.setAuthentication(new JwtAuthenticationToken(TestHelper.mockJwtToken(user), List.of()));
        return context;
    }
}
