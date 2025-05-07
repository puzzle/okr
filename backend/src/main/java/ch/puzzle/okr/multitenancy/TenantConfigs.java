package ch.puzzle.okr.multitenancy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TenantConfigs {

    private static final Map<String, TenantConfigProvider.TenantConfig> tenantConfigs = new ConcurrentHashMap<>();

    public static void add(String tenantId, TenantConfigProvider.TenantConfig tenantConfigProvider) {
        tenantConfigs.putIfAbsent(tenantId, tenantConfigProvider);
    }

    public static TenantConfigProvider.TenantConfig get(String tenantId) {
        return tenantConfigs.get(tenantId);
    }

    public static void clear() {
        tenantConfigs.clear();
    }
}
