package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.service.clientconfig.ClientConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/config")
public class ClientConfigController {

    private final ClientConfigService configService;

    public ClientConfigController(ClientConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public ResponseEntity<ClientConfigDto> getConfig() {
        return ResponseEntity.status(HttpStatus.OK).body(configService.getConfigBasedOnActiveEnv());
    }
}
