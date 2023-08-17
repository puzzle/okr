package ch.puzzle.okr.controller.v1;

import ch.puzzle.okr.service.CacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/caches")
public class CacheController {

    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Operation(summary = "Delete users cache", description = "Delete users cache")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Users cache deleted") })
    @PostMapping("emptyUsersCache")
    public void emptyUsersCache() {
        cacheService.emptyUsersCache();
    }

    @Operation(summary = "Delete all caches", description = "Delete all caches")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "All caches deleted") })
    @PostMapping("emptyAllCaches")
    public void emptyAllCaches() {
        cacheService.emptyAllCaches();
    }
}