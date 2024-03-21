package ch.puzzle.okr;

import ch.puzzle.okr.service.clientconfig.ClientConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableConfigurationProperties(ClientConfigProperties.class)
public class OkrApplication {
    public static void main(String[] args) {
        SpringApplication.run(OkrApplication.class, args);
    }
}
