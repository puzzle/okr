package ch.puzzle.okr.service;

import ch.puzzle.okr.TenantConfigProvider;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class ClientConfigService {

    private final TenantConfigProvider tenantConfigProvider;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    public ClientConfigService(final TenantConfigProvider tenantConfigProvider) {
        this.tenantConfigProvider = tenantConfigProvider;
    }

    public Map<String, String> getConfigBasedOnActiveEnv(String hostName) {
        String subdomain = hostName.split("\\.")[0];

        Optional<TenantConfigProvider.TenantConfig> tenantConfig = tenantConfigProvider.getTenantConfigById(subdomain);
        if (tenantConfig.isEmpty()) {
            throw new EntityNotFoundException(MessageFormat.format("Could not find tenant for subdomain:{0}", subdomain));
        }

        HashMap<String, String> config = new HashMap<>();
        config.put("activeProfile", activeProfile);
        config.put("issuer", tenantConfig.get().issuerUrl());
        config.put("clientId", tenantConfig.get().clientId());
        return config;
    }

}
