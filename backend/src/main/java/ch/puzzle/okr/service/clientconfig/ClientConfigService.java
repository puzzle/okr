package ch.puzzle.okr.service.clientconfig;

import ch.puzzle.okr.dto.ClientConfigDto;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${okr.clientconfig.favicon}")
    private String favicon;

    @Value("${okr.clientconfig.logo}")
    private String logo;

    @Value("${okr.clientconfig.customStyles}")
    private HashMap<String, String> customStyles;

    public ClientConfigDto getConfigBasedOnActiveEnv() {
        return new ClientConfigDto(activeProfile, issuer, clientId, favicon, logo, customStyles);
    }

}
