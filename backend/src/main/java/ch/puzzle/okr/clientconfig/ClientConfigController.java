package ch.puzzle.okr.clientconfig;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;

@Controller
@RequestMapping("/config")
public class ClientConfigController {

    @Autowired
    private org.springframework.core.env.Environment environment;

    private final ClientConfigService configService;

    public ClientConfigController(ClientConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public ResponseEntity<HashMap<String, String>> getConfig() {
        return ResponseEntity.status(HttpStatus.OK).body(configService.getConfigBasedOnActiveEnv(environment));
    }

}
