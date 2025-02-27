package ch.puzzle.okr.test;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.service.persistence.UserPersistenceService;
import java.util.List;
import java.util.Optional;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.security.test.context.support.WithSecurityContextFactory;
import org.springframework.stereotype.Component;

@Component
public class WithMockAuthUserSecurityContextFactory implements WithSecurityContextFactory<WithMockAuthUser> {
    private UserPersistenceService userPersistenceService;

    public WithMockAuthUserSecurityContextFactory(UserPersistenceService userPersistenceService) {
        this.userPersistenceService = userPersistenceService;
    }

    @Override
    public SecurityContext createSecurityContext(WithMockAuthUser annotation) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        User user = User.Builder
                .builder()
                .withEmail(annotation.email())
                .withFirstName("mocked user first name for test")
                .withLastName("mocked user last name for test")
                .build();
        TenantContext.setCurrentTenant(annotation.tenatn());
        Optional<User> byEmail = this.userPersistenceService.findByEmail(annotation.email());
        byEmail.ifPresentOrElse((u) -> {
            user.setFirstName(u.getFirstName());
            user.setLastName(u.getLastName());
        }, () -> {});
        context.setAuthentication(new JwtAuthenticationToken(TestHelper.mockJwtToken(user), List.of()));
        return context;
    }
}
