package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.dto.ObjectiveDTO;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.service.KeyResultService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/keyresults")
public class KeyResultController {
    KeyResultService keyResultService;

    KeyResultMapper keyResultMapper;

    public KeyResultController(KeyResultService keyResultService, KeyResultMapper keyResultMapper) {
        this.keyResultService = keyResultService;
        this.keyResultMapper = keyResultMapper;
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Updated a keyresult with a specified ID.",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = KeyResult.class))}),
            @ApiResponse(responseCode = "404", description = "Did not find a keyresult with a specified ID.", content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<KeyResult> updateKeyResult(@PathVariable long id, @RequestBody KeyResultDto keyResultDto) {
            keyResultDto.setId(id);
            return ResponseEntity.status(HttpStatus.OK).body(this.keyResultService.updateKeyResult(keyResultMapper.toKeyResult(keyResultDto)));
    }
}
