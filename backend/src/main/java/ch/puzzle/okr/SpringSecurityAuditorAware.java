package ch.puzzle.okr;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.service.authorization.AuthorizationService;
import java.util.Optional;
import org.springframework.data.domain.AuditorAware;
import org.springframework.stereotype.Component;

@Component
class SpringSecurityAuditorAware implements AuditorAware<User> {

    private final AuthorizationService authorizationService;

    public SpringSecurityAuditorAware(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public Optional<User> getCurrentAuditor() {
        return Optional.ofNullable(authorizationService.updateOrAddAuthorizationUser().user());
    }
}
