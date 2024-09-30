package ch.puzzle.okr.test;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest
@ContextConfiguration(initializers = TestContextInitializer.class)
@ActiveProfiles(value = "integration-test")
public @interface SpringIntegrationTest {
}
