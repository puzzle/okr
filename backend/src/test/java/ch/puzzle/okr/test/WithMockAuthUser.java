package ch.puzzle.okr.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAuthUserSecurityContextFactory.class)
public @interface WithMockAuthUser {
    String email() default "gl@gl.com";
}
