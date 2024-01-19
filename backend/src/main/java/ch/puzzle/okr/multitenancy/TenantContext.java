package ch.puzzle.okr.multitenancy;

import java.util.Optional;

public class TenantContext {

    public static final String DEFAULT_TENANT_ID = "public";
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static String getCurrentTenant() {
        return Optional.ofNullable(CURRENT_TENANT.get())
                .orElse("public");
    }

    public static void setCurrentTenant(String tenant) {
        CURRENT_TENANT.set(tenant);
    }
}