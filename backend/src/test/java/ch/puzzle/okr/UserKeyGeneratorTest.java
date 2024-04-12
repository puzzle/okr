package ch.puzzle.okr;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

import static ch.puzzle.okr.KeyResultTestHelpers.user;

class UserKeyGeneratorTest {
    @Test
    void generate_shouldReturnTenantAndUserInfo() {
        Object notUsedObject = new Object();
        Method notUsedMethod = notUsedObject.getClass().getEnclosingMethod();

        UserKeyGenerator generator = new UserKeyGenerator();
        String userKey = (String) generator.generate(notUsedObject, notUsedMethod, user);
        Assertions.assertEquals("publickaufmann@puzzle.ch1", userKey);
    }
}