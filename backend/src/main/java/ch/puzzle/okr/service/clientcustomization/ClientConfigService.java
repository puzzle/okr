package ch.puzzle.okr.service.clientcustomization;

import ch.puzzle.okr.dto.ClientConfigDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ClientConfigService {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    private final ClientCustomizationProperties clientCustomizationProperties;

    public ClientConfigService(ClientCustomizationProperties clientCustomizationProperties) {
        this.clientCustomizationProperties = clientCustomizationProperties;
    }

    public ClientConfigDto getConfigBasedOnActiveEnv() {
        return new ClientConfigDto(activeProfile, issuer, clientId, this.clientCustomizationProperties.getFavicon(),
                this.clientCustomizationProperties.getLogo(), this.clientCustomizationProperties.getTitle(),
                this.clientCustomizationProperties.getCustomStyles());
    }

}
