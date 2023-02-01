package ch.puzzle.okr;

import ch.puzzle.okr.dto.ObjectiveDto;
import ch.puzzle.okr.models.Objective;
import ch.puzzle.okr.models.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController("/public")
public class PublicController {
    @GetMapping
    public List<Objective> getProducts() {
        return new ArrayList<>(Arrays.asList(
                Objective.Builder.builder().withId(1L).withTitle("Objective 1").build(),
                Objective.Builder.builder().withId(2L).withTitle("Objective 2").build()
        ));
    }
}
