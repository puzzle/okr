package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
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

    KeyResultMapper keyResultMapper;

    public KeyResultController(KeyResultRepository keyResultRepository, KeyResultService keyResultService, KeyResultMapper keyResultMapper) {
        this.keyResultRepository = keyResultRepository;
        this.keyResultService = keyResultService;
        this.keyResultMapper = keyResultMapper;
    }

    @GetMapping
    public List<KeyResult> getAllKeyResults() {
        return (List<KeyResult>) keyResultRepository.findAll();
    }

    @PostMapping
    public KeyResult createKeyResult(@RequestBody KeyResultDto keyResultDto) {
        return this.keyResultService.createKeyResult(keyResultDto);
    }

    @PutMapping("/{id}")
    public KeyResult updateKeyResult(@PathVariable long id, @RequestBody KeyResultDto keyResultDto) {
        keyResultDto.setId(id);
        return this.keyResultService.updateKeyResult(keyResultMapper.toKeyResult(keyResultDto));
    }
}
