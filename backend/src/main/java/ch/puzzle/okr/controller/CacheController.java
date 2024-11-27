package ch.puzzle.okr.controller;

import ch.puzzle.okr.service.CacheService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

@RestController
@RequestMapping("api/v2/caches")
public class CacheController {
    private final CacheService cacheService;

    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @Operation(summary = "Delete authorization users cache", description = "Delete authorization users cache")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Authorization users cache deleted")})
    @PostMapping("emptyAuthorizationUsersCache")
    public void emptyAuthorizationUsersCache() {
        cacheService.emptyAuthorizationUsersCache();
    }

    @Operation(summary = "Delete all caches", description = "Delete all caches")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "All caches deleted")})
    @PostMapping("emptyAllCaches")
    public void emptyAllCaches() {
        cacheService.emptyAllCaches();
    }
}