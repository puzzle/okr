package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.service.clientconfig.ClientConfigService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ClientConfigController {

    private final ClientConfigService configService;

    public ClientConfigController(ClientConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/config")
    public ResponseEntity<ClientConfigDto> getConfig() {
        return ResponseEntity.status(HttpStatus.OK).body(configService.getConfigBasedOnActiveEnv());
    }

    @RequestMapping(value = "/{path:[^\\.]*}")
    public String redirect() {
        return "forward:/index.html";
    }
}
