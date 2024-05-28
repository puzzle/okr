package ch.puzzle.okr.multitenancy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CurrentTenantIdentifierResolverImplTest {

    @DisplayName("current TenantId is TenantId from TenantContext")
    @Test
    void currentTenantIdIsTenantIdFromTenantContext() {
        CurrentTenantIdentifierResolverImpl resolver = new CurrentTenantIdentifierResolverImpl();
        String tenantIdentifier = resolver.resolveCurrentTenantIdentifier();
        assertEquals(TenantContext.getCurrentTenant(), tenantIdentifier);
    }

    @DisplayName("validateExistingCurrentSessions is true")
    @Test
    void validateExistingCurrentSessionsIsTrue() {
        CurrentTenantIdentifierResolverImpl resolver = new CurrentTenantIdentifierResolverImpl();
        assertTrue(resolver.validateExistingCurrentSessions());
    }
}
