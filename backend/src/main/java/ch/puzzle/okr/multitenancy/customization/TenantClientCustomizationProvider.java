package ch.puzzle.okr.multitenancy.customization;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class TenantClientCustomizationProvider {
    private static final String CUSTOM_STYLES_PREFIX = "okr.tenants.{0}.clientcustomization.customstyles";
    private static final String TOPBAR_BACKGROUND_COLOR = ".okr-topbar-background-color";
    private static final String BANNER_BACKGROUND_COLOR = ".okr-banner-background-color";
    private static final String OVERVIEW_BACKGROUND_COLOR = ".okr-overview-background-color";
    private static final String TEAM_HEADER_COLOR = ".okr-team-header-color";
    private static final String ADD_OBJECTIVE_TEXT_COLOR = ".okr-add-objective-text-color";
    private static final String ADD_OBJECTIVE_ICON = ".okr-add-objective-icon";
    private static final String ADD_OBJECTIVE_OUTLINE_COLOR = ".okr-add-objective-outline-color";

    private final Map<String, TenantClientCustomization> tenantCustomizations = new HashMap<>();
    private final List<String> customCssStyles = List
            .of(TOPBAR_BACKGROUND_COLOR,
                BANNER_BACKGROUND_COLOR,
                OVERVIEW_BACKGROUND_COLOR,
                TEAM_HEADER_COLOR,
                ADD_OBJECTIVE_TEXT_COLOR,
                ADD_OBJECTIVE_ICON,
                ADD_OBJECTIVE_OUTLINE_COLOR);
    private final Environment env;

    public TenantClientCustomizationProvider(final @Value("${okr.tenant-ids}")
    String[] tenantIds, Environment env) {
        this.env = env;
        for (String tenantId : tenantIds) {
            readClientCustomizationConfig(tenantId);
            tenantCustomizations.put(tenantId, readClientCustomizationConfig(tenantId));
        }
    }

    private TenantClientCustomization readClientCustomizationConfig(String tenantId) {
        return new TenantClientCustomization(env
                .getProperty(MessageFormat.format("okr.tenants.{0}.clientcustomization.favicon", tenantId)),
                                             env
                                                     .getProperty(MessageFormat
                                                             .format("okr.tenants.{0}.clientcustomization.logo",
                                                                     tenantId)),
                                             env
                                                     .getProperty(MessageFormat
                                                             .format("okr.tenants.{0}.clientcustomization.triangles",
                                                                     tenantId)),
                                             env
                                                     .getProperty(MessageFormat
                                                             .format("okr.tenants.{0}.clientcustomization.background-logo",
                                                                     tenantId)),
                                             env
                                                     .getProperty(MessageFormat
                                                             .format("okr.tenants.{0}.clientcustomization.title",
                                                                     tenantId)),
                                             env
                                                     .getProperty(MessageFormat
                                                             .format("okr.tenants.{0}.clientcustomization.help-site-url",
                                                                     tenantId)),
                                             getCustomCssStyles(tenantId) //
        );
    }

    private Map<String, String> getCustomCssStyles(String tenantId) {
        Map<String, String> styles = new HashMap<>();
        for (String customStyle : customCssStyles) {
            String propertyName = formatWithTenant(CUSTOM_STYLES_PREFIX + customStyle, tenantId);
            CssConfigItem cssPropertyNameAndValue = getCssPropertyNameAndValue(propertyName, tenantId);
            styles.put(cssPropertyNameAndValue.cssName(), cssPropertyNameAndValue.cssValue());
        }
        return styles;
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

    record CssConfigItem(String cssName, String cssValue) implements Serializable {
    }
}