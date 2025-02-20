package ch.puzzle.okr.test;

import java.lang.annotation.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootTest
// @DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_CLASS)
@ContextConfiguration(initializers = TestContextInitializer.class)
@ActiveProfiles(value = "integration-test")
public @interface SpringIntegrationTest {
}
