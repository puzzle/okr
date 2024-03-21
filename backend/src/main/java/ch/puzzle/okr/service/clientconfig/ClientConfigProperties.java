package ch.puzzle.okr.service.clientconfig;

import ch.puzzle.okr.dto.ClientConfigDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@ConfigurationProperties("okr.clientconfig")
public class ClientConfigProperties {
    private String favicon;

    private String logo;

    private HashMap<String, String> customStyles;

}
