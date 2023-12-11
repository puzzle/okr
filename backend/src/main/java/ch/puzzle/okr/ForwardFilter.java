package ch.puzzle.okr;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;

public class ForwardFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(ForwardFilter.class);
    private final String[] allowedRoutes = { "/keyresult", "/objective", "/?state" };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String path = request.getRequestURI();

        if (Arrays.stream(this.allowedRoutes).anyMatch(path::startsWith)) {
            logger.info(String.format("Keycloak state parameter detected ====> make a forward from '%s' to '%s'",
                    request.getRequestURI(), "/"));
            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);
        }
        logger.debug(String.format("====> pass through the filter '%s'", request.getRequestURI()));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
