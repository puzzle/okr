package ch.puzzle.okr.multitenant;

import ch.puzzle.okr.models.Tenant;

public class TenantContext {

    private static final ThreadLocal<Tenant> CURRENT_TENANT = new ThreadLocal<>();

    public static Tenant getCurrentTenant() {
        return CURRENT_TENANT.get();
    }

    public static void setCurrentTenant(Tenant tenant) {
        CURRENT_TENANT.set(tenant);
    }
}
