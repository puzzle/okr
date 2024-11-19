package ch.puzzle.okr;

import java.lang.reflect.Method;
import java.text.MessageFormat;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;

import org.springframework.cache.interceptor.KeyGenerator;

public class UserKeyGenerator implements KeyGenerator {
    @Override
    public Object generate(Object target, Method method, Object... params) {
        User user = (User) params[0];
        return MessageFormat.format("{0}{1}{2}", TenantContext.getCurrentTenant(), user.getEmail(), user.getId());
    }
}