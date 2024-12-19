package ch.puzzle.okr;

import ch.puzzle.okr.multitenancy.TenantContext;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static ch.puzzle.okr.test.KeyResultTestHelpers.user;

class UserKeyGeneratorTest {

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(null);
    }

    @DisplayName("Should generate tenant and user information correctly")
    @Test
    void shouldGenerateTenantAndUserInfo() {
        Object notUsedObject = new Object();
        Method notUsedMethod = notUsedObject.getClass().getEnclosingMethod();

        UserKeyGenerator generator = new UserKeyGenerator();
        String userKey = (String) generator.generate(notUsedObject, notUsedMethod, user);
        Assertions.assertEquals("publickaufmann@puzzle.ch1", userKey);
    }
}