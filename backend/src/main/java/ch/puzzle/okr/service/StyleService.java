package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.StyleDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class StyleService {
    @Value("${okr.style.class}")
    private String bodyClass;
    public StyleDto getStyles() {
        return new StyleDto(bodyClass);
    }
}
