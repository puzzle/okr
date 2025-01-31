package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.ClientConfigDto;
import ch.puzzle.okr.service.clientconfig.ClientConfigService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ClientConfigController {

    private final ClientConfigService clientConfigService;

    public ClientConfigController(ClientConfigService clientConfigService) {
        this.clientConfigService = clientConfigService;
    }

    @GetMapping("/config")
    public ResponseEntity<ClientConfigDto> getConfig(HttpServletRequest request) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(clientConfigService.getConfigBasedOnActiveEnv(request.getServerName()));
    }

    @RequestMapping(value = "/**/{[path:[^\\.]*}", method = { RequestMethod.HEAD, RequestMethod.GET,
            RequestMethod.OPTIONS })
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
