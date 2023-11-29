package ch.puzzle.okr;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import java.util.Map;

@Component
public class OkrErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
        Map<String, Object> errorAttributes = super.getErrorAttributes(webRequest, options);

        Throwable throwable = getError(webRequest);
        if (throwable instanceof OkrResponseStatusException exception) {
            errorAttributes.put("errors", exception.getErrors());
        }
        return errorAttributes;
    }
}
