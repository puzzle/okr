package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.service.KeyResultService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/keyresults")
public class KeyResultController {


    KeyResultRepository keyResultRepository;

    KeyResultService keyResultService;

    public KeyResultController(KeyResultRepository keyResultRepository, KeyResultService keyResultService) {
        this.keyResultRepository = keyResultRepository;
        this.keyResultService = keyResultService;
    }

    @GetMapping
    public List<KeyResult> getAllKeyResults() {
        return (List<KeyResult>) keyResultRepository.findAll();
    }

    @PostMapping
    public KeyResult createKeyResult(@RequestBody KeyResultDto keyResultDto) {
        return this.keyResultService.createKeyResult(keyResultDto);
    }
}
