package ch.puzzle.okr.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import java.util.Collection;
import java.util.List;

import static org.mockito.Mockito.*;

public class CacheServiceTest {

    @DisplayName("emptyAllCaches() should call for each defined cache the clear() method")
    @Test
    void emptyAllCachesShouldCallForEachDefinedCacheTheClearMethod() {
        // arrange
        var cache = mock(Cache.class);
        var cacheManager = new CacheManager() {
            @Override
            public Cache getCache(String name) {
                return cache;
            }

            @Override
            public Collection<String> getCacheNames() {
                return List.of("Cache1", "Cache2");
            }
        };

        // act
        var cacheService = new CacheService(cacheManager);
        cacheService.emptyAllCaches();

        // assert
        verify(cache, times(2)).clear();
    }
}
