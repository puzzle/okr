package ch.puzzle.okr;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class SpringCachingConfig {

    public static final String AUTHORIZATION_USER_CACHE = "authorizationUsers";
    public static final String USER_CACHE = "users";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(AUTHORIZATION_USER_CACHE, USER_CACHE);
    }
}