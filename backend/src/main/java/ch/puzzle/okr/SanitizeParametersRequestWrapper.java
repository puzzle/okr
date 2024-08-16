package ch.puzzle.okr;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.*;
import java.util.stream.Collectors;

public class SanitizeParametersRequestWrapper extends HttpServletRequestWrapper {

    private final Map<String, String[]> sanitizedMap;

    public SanitizeParametersRequestWrapper(HttpServletRequest request) {
        super(request);
        sanitizedMap = Collections.unmodifiableMap(request.getParameterMap().entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> Arrays.stream(entry.getValue())
                        .map(StringEscapeUtils::escapeHtml4).toArray(String[]::new))));
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        return new HashMap<>();
    }

    @Override
    public String[] getParameterValues(String name) {
        return new String[0];
    }

    @Override
    public String getParameter(String name) {
        if (name.equals("state"))
            return null;

        return Optional.ofNullable(getParameterValues(name)).map(values -> values[0]).orElse(null);
    }

    @Override
    public Enumeration<String> getParameterNames() {
        return null;
    }
}