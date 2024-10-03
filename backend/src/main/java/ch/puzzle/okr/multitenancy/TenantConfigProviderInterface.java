package ch.puzzle.okr.multitenancy;

import java.util.List;
import java.util.Optional;

public interface TenantConfigProviderInterface {
    List<TenantConfigProvider.TenantConfig> getTenantConfigs();

    Optional<TenantConfigProvider.TenantConfig> getTenantConfigById(String tenantId);

    Optional<String> getJwkSetUri(String tenantId);

}
