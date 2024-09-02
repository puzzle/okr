package ch.puzzle.okr.controller;

import ch.puzzle.okr.ForwardFilter;
import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.service.clientconfig.ClientConfigService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class ClientConfigController {

    private final ClientConfigService configService;
    private static final Logger logger = LoggerFactory.getLogger(ClientConfigController.class);

    public ClientConfigController(ClientConfigService configService) {
        this.configService = configService;
    }

    @GetMapping("/config")
    public ResponseEntity<ClientConfigDto> getConfig() {
        return ResponseEntity.status(HttpStatus.OK).body(configService.getConfigBasedOnActiveEnv());
    }

    @RequestMapping(value = "/**/{[path:[^\\.]*}")
    public String redirect(HttpServletRequest request) {
        String path = request.getRequestURI();

        logger.debug("----------------------");
        logger.debug("Forwarding request to: " + path);
        logger.debug("----------------------");

        // Serve static resources or paths containing a dot directly
        if (path.startsWith("/assets/") || path.contains(".")) {
            return "forward:" + path;
        }

        // Forward all other requests to index.html
        return "forward:/";
    }

}
