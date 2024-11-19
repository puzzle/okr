package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.service.clientconfig.ClientConfigService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ClientConfigController {

    private final ClientConfigService configService;

    public ClientConfigController(ClientConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/config")
    public ResponseEntity<ClientConfigDto> getConfig(HttpServletRequest request) {
        return ResponseEntity.status(HttpStatus.OK)
                             .body(configService.getConfigBasedOnActiveEnv(request.getServerName()));
    }

    @RequestMapping(value = "/**/{[path:[^\\.]*}")
    public String redirect(HttpServletRequest request) {
        String path = request.getRequestURI();
        // Serve static resources or paths containing a dot directly
        if (path.startsWith("/assets/") || path.contains(".")) {
            return "forward:" + path;
        }

        // Forward all other requests to index.html
        return "forward:/";
    }

}
