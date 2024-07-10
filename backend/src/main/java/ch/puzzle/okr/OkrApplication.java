package ch.puzzle.okr;

import ch.puzzle.okr.service.clientconfig.ClientCustomizationProperties;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ClientCustomizationProperties.class)
public class OkrApplication {
    public static void main(String[] args) {

        new SpringApplicationBuilder(OkrApplication.class) //
                .initializers(new OkrApplicationContextInitializer()) //
                .run(args);
    }
}
