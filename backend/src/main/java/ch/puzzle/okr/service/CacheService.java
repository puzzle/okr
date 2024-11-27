package ch.puzzle.okr.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import static ch.puzzle.okr.SpringCachingConfig.AUTHORIZATION_USER_CACHE;

@EnableScheduling
@Service
public class CacheService {

    private static final Logger logger = LoggerFactory.getLogger(CacheService.class);

    private final CacheManager cacheManager;

    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @CacheEvict(value = AUTHORIZATION_USER_CACHE, allEntries = true)
    @Scheduled(fixedRateString = "${caching.authorization.users.TTL}")
    public void emptyAuthorizationUsersCache() {
        logger.info("emptying authorization users cache");
    }

    public void emptyAllCaches() {
        cacheManager.getCacheNames()
                    .forEach(cacheName -> {
                        cacheManager.getCache(cacheName)
                                    .clear();
                        logger.info("emptying {} cache", cacheName);
                    });
    }
}
