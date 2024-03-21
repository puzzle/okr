package ch.puzzle.okr.service.clientconfig;

import ch.puzzle.okr.dto.ClientConfigDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ClientConfigService {

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Value("${spring.profiles.active}")
    private String activeProfile;

    @Value("${spring.security.oauth2.resourceserver.opaquetoken.client-id}")
    private String clientId;
    private final ClientConfigProperties clientConfigProperties;

    public ClientConfigService(ClientConfigProperties clientConfigProperties) {
        this.clientConfigProperties = clientConfigProperties;
    }

    public ClientConfigDto getConfigBasedOnActiveEnv() {
        return new ClientConfigDto(activeProfile, issuer, clientId, this.clientConfigProperties.getFavicon(),
                this.clientConfigProperties.getLogo(), this.clientConfigProperties.getTitle(),
                this.clientConfigProperties.getCustomStyles());
    }

}
