package ch.puzzle.okr.controller;

import ch.puzzle.okr.service.ClientConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

@RestController
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
