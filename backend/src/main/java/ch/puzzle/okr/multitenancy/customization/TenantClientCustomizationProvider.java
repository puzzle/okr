package ch.puzzle.okr.multitenancy.customization;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Component
public class TenantClientCustomizationProvider {
    private final Map<String, TenantClientCustomization> tenantCustomizations = new HashMap<>();
    private final Environment env;
    private static String CUSTOMSTYLES_PREFIX = "okr.tenants.{0}.clientcustomization.customstyles";

    record CssConfigItem(String cssName, String cssValue) implements Serializable {
    }

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
                getCustomStyles(tenantId) //
        );
    }

    private Map<String, String> getCustomStyles(String tenantId) {
        Map<String, String> customStyles = new HashMap<>();

        CssConfigItem topbarBackgroundColor = getTopbarBackgroundColor(tenantId);
        customStyles.put(topbarBackgroundColor.cssName(), topbarBackgroundColor.cssValue());

        return customStyles;
    }

    private CssConfigItem getTopbarBackgroundColor(String tenantId) {
        String topbarColorPropertyName = formatWithTenant( //
                CUSTOMSTYLES_PREFIX + ".okr-topbar-background-color", //
                tenantId);
        return getCssPropertyNameAndValue(topbarColorPropertyName, tenantId);
    }

    private CssConfigItem getCssPropertyNameAndValue(String propertyName, String tenantId) {
        String cssName = extractCssNameFromPropertyName(propertyName, tenantId);
        String cssValue = env.getProperty(formatWithTenant(propertyName, tenantId));
        return new CssConfigItem(cssName, cssValue);
    }

    public Optional<TenantClientCustomization> getTenantClientCustomizationsById(String tenantId) {
        return Optional.ofNullable(this.tenantCustomizations.get(tenantId));
    }

    String extractCssNameFromPropertyName(String propertyName, String tenantId) {
        String prefix = formatWithTenant("okr.tenants.{0}.clientcustomization.customstyles", tenantId);
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