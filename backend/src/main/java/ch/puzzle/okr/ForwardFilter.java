package ch.puzzle.okr;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.GenericFilterBean;

public class ForwardFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(ForwardFilter.class);

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        logger.debug(String.format("====> pass through the filter '%s'", request.getRequestURI()));
        filterChain.doFilter(servletRequest, servletResponse);
    }
}
