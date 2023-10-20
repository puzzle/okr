package ch.puzzle.okr;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForwardFilterTest {
    @InjectMocks
    ForwardFilter forwardFilter;

    @Mock
    HttpServletRequest request;

    @Mock
    ServletResponse response;

    @Mock
    FilterChain filterChain;

    @Mock
    RequestDispatcher requestDispatcher;

    private final String URL_BASE = "http://localhost:8080";

    @BeforeEach
    void setup() {
    }

    @ParameterizedTest
    @ValueSource(strings = { "/", "/api/", "/index.html", "/runtime.", "/polyfills.", "/main.", "/scripts.", "/styles.",
            "/favicon.ico", "/3rdpartylicenses.txt", "/assets/" })
    void shouldNotFilterTheRootPath(String requestUri) throws ServletException, IOException {
        // given
        when(request.getRequestURI()).thenReturn(requestUri);
        when(request.getRequestURL()).thenReturn(new StringBuffer(URL_BASE + requestUri));
        doNothing().when(filterChain).doFilter(Mockito.eq(request), Mockito.eq(response));

        // when
        forwardFilter.doFilter(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(Mockito.eq(request), Mockito.eq(response));
        verify(request, never()).getRequestDispatcher(anyString());
    }

    @Test
    void shouldFilterAuthPath() throws ServletException, IOException {
        // given
        when(request.getRequestURL()).thenReturn(new StringBuffer(URL_BASE + "/state=''"));
        when(request.getRequestURI()).thenReturn("/state=''");
        when(request.getParameter("state")).thenReturn("state");
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        doNothing().when(requestDispatcher).forward(Mockito.eq(request), Mockito.eq(response));

        // when
        forwardFilter.doFilter(request, response, filterChain);

        // then
        verify(filterChain, never()).doFilter(Mockito.eq(request), Mockito.eq(response));
        verify(request, times(1)).getRequestDispatcher(anyString());
    }
}