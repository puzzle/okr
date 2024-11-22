package ch.puzzle.okr;

import java.lang.reflect.Method;

import ch.puzzle.okr.multitenancy.TenantContext;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static ch.puzzle.okr.test.KeyResultTestHelpers.user;

class UserKeyGeneratorTest {

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(null);
    }

    @Test
    void generate_shouldReturnTenantAndUserInfo() {
        Object notUsedObject = new Object();
        Method notUsedMethod = notUsedObject.getClass()
                                            .getEnclosingMethod();

        UserKeyGenerator generator = new UserKeyGenerator();
        String userKey = (String) generator.generate(notUsedObject, notUsedMethod, user);
        Assertions.assertEquals("publickaufmann@puzzle.ch1", userKey);
    }
}