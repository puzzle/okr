package ch.puzzle.okr;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.FilterChain;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

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

    @BeforeEach
    void setup() {
    }

    @ParameterizedTest
    @ValueSource(strings = { "/", "/api/", "/index.html", "/runtime.", "/polyfills.", "/main.", "/scripts.", "/styles.",
            "/favicon.ico", "/3rdpartylicenses.txt", "/assets/" })
    void shouldNotFilterTheRootPath(String requestUri) throws ServletException, IOException {
        // given
        when(request.getRequestURI()).thenReturn(requestUri);
        doNothing().when(filterChain).doFilter(Mockito.eq(request), Mockito.eq(response));

        // when
        forwardFilter.doFilter(request, response, filterChain);

        // then
        verify(filterChain, times(1)).doFilter(Mockito.eq(request), Mockito.eq(response));
        verify(request, never()).getRequestDispatcher(anyString());
    }
}