package ch.puzzle.okr.service.clientconfig;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TenantClientCustomizationProvider {
    private final Map<String, TenantClientCustomization> tenantCustomizations = new HashMap<>();
    private final Environment env;

    public TenantClientCustomizationProvider(final @Value("${okr.tenant-ids}") String[] tenantIds, Environment env) {
        this.env = env;
        for (String tenantId : tenantIds) {
            readClientCustomizationConfig(tenantId);
            tenantCustomizations.put(tenantId, readClientCustomizationConfig(tenantId));
        }
    }

    private TenantClientCustomization readClientCustomizationConfig(String tenantId) {
        return new TenantClientCustomization(
                env.getProperty(MessageFormat.format("okr.tenants.{0}.clientcustomization.favicon", tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.clientcustomization.logo", tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.clientcustomization.triangles", tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.clientcustomization.background-logo", tenantId)),
                env.getProperty(MessageFormat.format("okr.tenants.{0}.clientcustomization.title", tenantId)),
                new HashMap<>());
    }

    public Optional<TenantClientCustomization> getTenantClientCustomizationsById(String tenantId) {
        return Optional.ofNullable(this.tenantCustomizations.get(tenantId));
    }

}