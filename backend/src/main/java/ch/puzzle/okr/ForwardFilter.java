package ch.puzzle.okr;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class ForwardFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        System.out.println("*** ForwardFilter reached");
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        if(request.getParameter("state") != null){
            System.out.println(String.format("Keycloak state parameter detected ====> make a forward from '%s' to '%s'", request.getRequestURI(), "/"));
            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);
            return;
        };
        if(request.getParameter("error") != null){
            System.out.println("error from keycloak " +request.getParameter("error"));
            return;
        };
//
//        if (request.getRequestURI().equals("/") || request.getRequestURI().startsWith("/api/")
//                || request.getRequestURI().startsWith("/index.html") || request.getRequestURI().startsWith("/runtime.")
//                || request.getRequestURI().startsWith("/polyfills.") || request.getRequestURI().startsWith("/main.")
//                || request.getRequestURI().startsWith("/scripts.") || request.getRequestURI().startsWith("/styles.")
//                || request.getRequestURI().startsWith("/favicon.ico")
//                || request.getRequestURI().startsWith("/3rdpartylicenses.txt")
//                || request.getRequestURI().startsWith("/assets/") || request.getRequestURI().startsWith("/v3/api-docs")
//                || request.getRequestURI().startsWith("/swagger-ui")) {
//            // TODO: replace with a Logger
            System.out.println(String.format("====> pass through the filter '%s'", request.getRequestURI()));
            filterChain.doFilter(servletRequest, servletResponse);
//        } else {
//            // TODO: replace with a Logger
//            System.out.println(String.format("====> make a forward from '%s' to '%s'", request.getRequestURI(), "/"));
//            servletRequest.getRequestDispatcher("/").forward(servletRequest, servletResponse);
//        }
    }
}
