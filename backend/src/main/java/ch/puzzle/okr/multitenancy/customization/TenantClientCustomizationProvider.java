package ch.puzzle.okr.multitenancy.customization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.AbstractEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class TenantClientCustomizationProvider {
    private static final String CUSTOM_STYLES_PREFIX = "okr.tenants.{0}.clientcustomization.customstyles";

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
                env.getProperty(MessageFormat.format("okr.tenants.{0}.clientcustomization.helpSiteUrl", tenantId)),
                getCustomCssStyles(tenantId) //
        );
    }

    private Map<String, String> getCustomCssStyles(String tenantId) {
        MutablePropertySources propSrcs = ((AbstractEnvironment) env).getPropertySources();
        Map<String, String> styles = StreamSupport.stream(propSrcs.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource) // Filtert alle PropertySources
                .map(ps -> ((EnumerablePropertySource<?>) ps).getPropertyNames()).flatMap(Arrays::stream) // Stream
                                                                                                          // aller
                                                                                                          // Propertynamen
                .filter(propName -> propName.contains(formatWithTenant(CUSTOM_STYLES_PREFIX, tenantId))) // Filtert alle
                                                                                                         // CustomStyles
                                                                                                         // für den
                                                                                                         // Tenant
                .collect(Collectors.toMap(propName -> extractCssNameFromPropertyName(propName, tenantId), // Collected
                                                                                                          // alle
                                                                                                          // CustomStyles
                                                                                                          // in eine Map
                        env::getProperty));
        return styles;
    }

    public Optional<TenantClientCustomization> getTenantClientCustomizationsById(String tenantId) {
        return Optional.ofNullable(this.tenantCustomizations.get(tenantId));
    }

    String extractCssNameFromPropertyName(String propertyName, String tenantId) {
        String prefix = formatWithTenant(CUSTOM_STYLES_PREFIX, tenantId);
        int startPrefix = propertyName.indexOf(prefix);
        if (startPrefix == -1) {
            throw new IllegalArgumentException(propertyName + " is not a valid property name client customization");
        }

        int startCssPropertyName = startPrefix + prefix.length() + 1;
        return propertyName.substring(startCssPropertyName);
    }

    private String formatWithTenant(String stringWithTenantPlaceholder, String tenantId) {
        return MessageFormat.format(stringWithTenantPlaceholder, tenantId);
    }

}