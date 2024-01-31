package ch.puzzle.okr.controller;

import ch.puzzle.okr.service.ClientConfigService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/config")
public class ClientConfigController {

    private final ClientConfigService configService;

    public ClientConfigController(ClientConfigService configService) {
        this.configService = configService;
    }

    @GetMapping
    public ResponseEntity<Map<String, String>> getConfig(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(configService.getConfigBasedOnActiveEnv(request.getServerName()));
    }
}
