package ch.puzzle.okr.multitenancy;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class TenantContextTest {

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Default tenant should be 'public'")
    @Test
    void defaultTenantIsPublic() {
        Assertions.assertEquals("public", TenantContext.getCurrentTenant());
    }

    @DisplayName("Should return the tenant set in the context")
    @Test
    void shouldGetTenantFromContextAfterItWasSet() {
        String tenant = "okr_acme";
        TenantContext.setCurrentTenant(tenant);
        Assertions.assertEquals(tenant, TenantContext.getCurrentTenant());
    }
}
