package ch.puzzle.okr.service.clientconfig;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.multitenancy.TenantConfigProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.Optional;

@Service
public class ClientConfigService {

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

        Optional<TenantConfigProvider.TenantConfig> tenantConfig = tenantConfigProvider.getTenantConfigById(subdomain);
        if (tenantConfig.isEmpty()) {
            throw new EntityNotFoundException(
                    MessageFormat.format("Could not find tenant for subdomain:{0}", subdomain));
        }

        return new ClientConfigDto(activeProfile, tenantConfig.get().issuerUrl(), tenantConfig.get().clientId(),
                clientCustomizationProperties.getFavicon(), clientCustomizationProperties.getLogo(),
                clientCustomizationProperties.getTitle(), clientCustomizationProperties.getCustomStyles());
    }
}
