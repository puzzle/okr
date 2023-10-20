package ch.puzzle.okr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;

@Component
public class ForwardFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ForwardFilter.class);
    private final String[] allowedRoutes = { "/keyresult", "/objective" };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = new URL(request.getRequestURL().toString()).getPath();
        if (request.getParameter("state") != null) {
            logger.info(String.format("Keycloak state parameter detected ====> make a forward from '%s' to '%s'",
                    request.getRequestURI(), "/"));
            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);
            return;
        }
        if (Arrays.stream(this.allowedRoutes).anyMatch(path::startsWith)) {
            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);
            return;
        }
        if (request.getParameter("error") != null) {
            logger.error(String.format("error from keycloak %s", request.getParameter("error")));
            return;
        }
        logger.debug(String.format("====> pass through the filter '%s'", request.getRequestURI()));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
