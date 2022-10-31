package ch.puzzle.okr.controller;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/keyresults")
public class KeyResultController {

    @Autowired
    KeyResultRepository keyResultRepository;

    @GetMapping
    public List<KeyResult> getAllKeyResults() {
        return (List<KeyResult>) keyResultRepository.findAll();
    }
}
