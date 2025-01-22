package ch.puzzle.okr;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaAuditing
public class OkrApplication {
    public static void main(String[] args) {

        new SpringApplicationBuilder(OkrApplication.class) //
                .initializers(new OkrApplicationContextInitializer()) //
                .run(args);
    }
}
