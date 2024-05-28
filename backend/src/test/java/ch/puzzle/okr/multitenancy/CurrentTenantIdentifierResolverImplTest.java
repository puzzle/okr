package ch.puzzle.okr.multitenancy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurrentTenantIdentifierResolverImplTest {

    @DisplayName("CurrentTenantIdentifier ss public")
    @Test
    void currentTenantIdentifierIsPublic() {
        CurrentTenantIdentifierResolverImpl resolver = new CurrentTenantIdentifierResolverImpl();
        String tenantIdentifier = resolver.resolveCurrentTenantIdentifier();
        assertEquals("public", tenantIdentifier);
    }

    @DisplayName("validateExistingCurrentSessions is true")
    @Test
    void validateExistingCurrentSessionsIsTrue() {
        CurrentTenantIdentifierResolverImpl resolver = new CurrentTenantIdentifierResolverImpl();
        assertTrue(resolver.validateExistingCurrentSessions());
    }
}
