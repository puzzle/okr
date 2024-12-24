package ch.puzzle.okr.service.clientconfig;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import ch.puzzle.okr.multitenancy.customization.TenantClientCustomization;
import ch.puzzle.okr.multitenancy.customization.TenantClientCustomizationProvider;
import jakarta.persistence.EntityNotFoundException;
import java.text.MessageFormat;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClientConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ClientConfigService.class);
    private final TenantClientCustomizationProvider tenantClientCustomizationProvider;
    private final TenantConfigProvider tenantConfigProvider;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public ClientConfigService(final TenantClientCustomizationProvider clientCustomizationProvider,
                               final TenantConfigProvider tenantConfigProvider) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.tenantClientCustomizationProvider = clientCustomizationProvider;
    }

    public ClientConfigDto getConfigBasedOnActiveEnv(String hostName) {
        String subDomain = hostName.split("\\.")[0];
        String domainPrefixByHyphen = hostName.split("-")[0];

        Optional<TenantConfigProvider.TenantConfig> tenantConfig = getTenantConfig(hostName,
                                                                                   subDomain,
                                                                                   domainPrefixByHyphen);

        if (tenantConfig.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat
                    .format("Could not find tenant config for subDomain:{0}", subDomain));
        }

        Optional<TenantClientCustomization> tenantClientCustomization = getTenantClientCustomization(hostName,
                                                                                                     subDomain,
                                                                                                     domainPrefixByHyphen);

        if (tenantClientCustomization.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat
                    .format("Could not find tenant client customization for subDomain:{0}", subDomain));
        }

        return new ClientConfigDto(activeProfile, //
                                   tenantConfig.get().issuerUrl(), //
                                   tenantConfig.get().clientId(), //
                                   tenantClientCustomization.get().favicon(), //
                                   tenantClientCustomization.get().logo(), //
                                   tenantClientCustomization.get().triangles(), //
                                   tenantClientCustomization.get().backgroundLogo(), //
                                   tenantClientCustomization.get().title(), //
                                   tenantClientCustomization.get().helpSiteUrl(), //
                                   tenantClientCustomization.get().customStyles()); //
    }

    private Optional<TenantConfigProvider.TenantConfig> getTenantConfig(String hostname, String... tenantsFromUrl) {
        for (String tenant : tenantsFromUrl) {
            Optional<TenantConfigProvider.TenantConfig> tenantConfig = tenantConfigProvider.getTenantConfigById(tenant);
            if (tenantConfig.isPresent()) {
                logger.info("get config for " + tenant + ": OK");
                return tenantConfig;
            }
            logger.info("get config found for " + tenant + ": failed");
        }
        logger.info("no config found for " + hostname + ": failed");
        return Optional.empty();
    }

    private Optional<TenantClientCustomization> getTenantClientCustomization(String hostname,
                                                                             String... tenantsFromUrl) {
        for (String tenant : tenantsFromUrl) {
            Optional<TenantClientCustomization> tenantCustomization = tenantClientCustomizationProvider
                    .getTenantClientCustomizationsById(tenant);
            if (tenantCustomization.isPresent()) {
                logger.info("get client customization for " + tenant + ": OK");
                return tenantCustomization;
            }
            logger.info("get client customization for " + tenant + ": failed");
        }
        logger.info("no client customization found for " + hostname + ": failed");
        return Optional.empty();
    }
}
