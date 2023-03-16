package ch.puzzle.okr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

/**
 * Is needed to redirect the callback of the Oauth-process in order to avoid 404
 */

@Controller
@RequestMapping("/")
public class RedirectController {

    @GetMapping("/**")
    public ModelAndView redirectWithUsingForwardPrefix() {
        return new ModelAndView("forward:/");
    }
}
