package ch.puzzle.okr;

import ch.puzzle.okr.service.clientconfig.ClientCustomizationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ClientCustomizationProperties.class)
public class OkrApplication {
    public static void main(String[] args) {
        SpringApplication.run(OkrApplication.class, args);
    }
}
