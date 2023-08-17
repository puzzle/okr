package ch.puzzle.okr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.time.YearMonth;

@SpringBootApplication
@EnableScheduling
public class OkrApplication {
    public static void main(String[] args) {
        SpringApplication.run(OkrApplication.class, args);
    }

    @Bean
    YearMonth yearMonth() {
        return YearMonth.now();
    }
}
