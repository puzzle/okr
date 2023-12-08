package ch.puzzle.okr;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Filter;
import java.util.logging.LogRecord;

@Component
public class ForwardFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ForwardFilter.class);
    private final String[] allowedRoutes = { "/keyresult", "/objective" };

    // TOdo make sure this methis is actually used
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();
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

    @Override
    public boolean isLoggable(LogRecord record) {
        return false;
    }
}
