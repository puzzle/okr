package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.StyleDto;
import ch.puzzle.okr.service.StyleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v2/styles")
public class StyleController {

    private final StyleService styleService;

    public StyleController(StyleService styleService) {
        this.styleService = styleService;
    }

    @Operation(summary = "Get all configurable styles for the UI", description = "Gets all styles for the UI as an object")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Returned an object of the styles", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = StyleDto.class))}),
            @ApiResponse(responseCode = "400", description = "Can't return the styles", content = @Content),
            @ApiResponse(responseCode = "401", description = "Not authorized to read the styles", content = @Content)
    })
    @GetMapping("")
    public ResponseEntity<StyleDto> getStyles() {
        return ResponseEntity.ok()
                .body(styleService.getStyles());
    }

}
