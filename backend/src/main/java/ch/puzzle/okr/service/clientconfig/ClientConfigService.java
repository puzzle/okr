package ch.puzzle.okr.service.clientconfig;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
public class ClientConfigService {

    private static final Logger logger = LoggerFactory.getLogger(ClientConfigService.class);
    private final ClientCustomizationProperties clientCustomizationProperties;
    private final TenantConfigProvider tenantConfigProvider;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public ClientConfigService(final ClientCustomizationProperties clientCustomizationProperties,
            final TenantConfigProvider tenantConfigProvider) {
        this.tenantConfigProvider = tenantConfigProvider;
        this.clientCustomizationProperties = clientCustomizationProperties;
    }

    public ClientConfigDto getConfigBasedOnActiveEnv(String hostName) {
        String subdomain = hostName.split("\\.")[0];
        String domainPrefixByHyphen = hostName.split("-")[0];

        Optional<TenantConfigProvider.TenantConfig> tenantConfig = getTenantConfig(hostName, subdomain,
                domainPrefixByHyphen);

        if (tenantConfig.isEmpty()) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Could not find tenant for subdomain:{0}", subdomain));
        }

        return new ClientConfigDto(activeProfile, tenantConfig.get().issuerUrl(), tenantConfig.get().clientId(),
                clientCustomizationProperties.getFavicon(), clientCustomizationProperties.getLogo(),
                clientCustomizationProperties.getTitle(), clientCustomizationProperties.getCustomStyles());
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
}
