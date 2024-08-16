package ch.puzzle.okr;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;
import java.util.Arrays;

public class ForwardFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(ForwardFilter.class);
    private final String[] allowedRoutes = { "/keyresult", "/objective", "/dashboard" };

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        SanitizeParametersRequestWrapper request = new SanitizeParametersRequestWrapper(
                (HttpServletRequest) servletRequest);
        String path = request.getRequestURI();

        logger.debug(String.format("This is the URI '%s'", path));
        if (path.startsWith("/?state")) {
            logger.debug("change path to /dashboard");

            request.getRequestDispatcher("/dashboard");
        }

        // if (Arrays.stream(this.allowedRoutes).anyMatch(path::startsWith)) {
        // logger.info(String.format("Keycloak state parameter detected ====> make a forward from '%s' to '%s'",
        // request.getRequestURI(), "/"));
        // request.getRequestDispatcher("/").forward(request, servletResponse);
        // return;
        // }
        logger.debug(String.format("====> pass through the filter '%s'", request.getRequestURI()));
        filterChain.doFilter(request, servletResponse);
    }
}
