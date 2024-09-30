package ch.puzzle.okr;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class SpringCachingConfig {

    public static final String AUTHORIZATION_USER_CACHE = "authorizationUsers";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(AUTHORIZATION_USER_CACHE);
    }

    public static String cacheKey(User user) {
        return TenantContext.getCurrentTenant() + "_" + user.getEmail();
    }
}