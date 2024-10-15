package ch.puzzle.okr.multitenancy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

public class TenantContext {
    public static final String DEFAULT_TENANT_ID = "public";
    private static final Logger logger = LoggerFactory.getLogger(TenantContext.class);
    private static final ThreadLocal<String> CURRENT_TENANT = new ThreadLocal<>();

    public static String getCurrentTenant() {
        return Optional.ofNullable(CURRENT_TENANT.get()).orElse(DEFAULT_TENANT_ID);
    }

    public static void setCurrentTenant(String tenant) {
        logger.debug("Setting current TenantContext to tenant: {}", tenant);
        CURRENT_TENANT.set(tenant);
    }
}