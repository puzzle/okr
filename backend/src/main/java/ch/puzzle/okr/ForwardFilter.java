package ch.puzzle.okr;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class ForwardFilter implements Filter {

    private static final Logger logger = LoggerFactory.getLogger(ForwardFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if (request.getParameter("state") != null) {
            logger.info("Keycloak state parameter detected ====> make a forward from {} to {}", request.getRequestURI(),
                    "/");
            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);
            return;
        }
        String getParameterError = request.getParameter("error");
        if (getParameterError != null) {
            logger.error("error from keycloak {}", getParameterError);
            return;
        }
        logger.debug("====> pass through the filter {}", request.getRequestURI());
        filterChain.doFilter(servletRequest, servletResponse);

        // could be simpyfied by using state-parameter in url for passing through filters....
        // if (request.getRequestURI().equals("/") || request.getRequestURI().startsWith("/api/")
        // || request.getRequestURI().startsWith("/index.html") || request.getRequestURI().startsWith("/runtime.")
        // || request.getRequestURI().startsWith("/polyfills.") || request.getRequestURI().startsWith("/main.")
        // || request.getRequestURI().startsWith("/scripts.") || request.getRequestURI().startsWith("/styles.")
        // || request.getRequestURI().startsWith("/favicon.ico")
        // || request.getRequestURI().startsWith("/3rdpartylicenses.txt")
        // || request.getRequestURI().startsWith("/assets/") || request.getRequestURI().startsWith("/v3/api-docs")
        // || request.getRequestURI().startsWith("/swagger-ui")) {
        // } else {
        // logger.info(String.format("====> make a forward from '%s' to '%s'", request.getRequestURI(), "/"));
        // servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);
        // }
    }
}
