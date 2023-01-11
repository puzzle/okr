package ch.puzzle.okr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.time.YearMonth;
import java.util.Calendar;

@SpringBootApplication
public class OkrApplication {
    public static void main(String[] args) {
        SpringApplication.run(OkrApplication.class, args);
    }

    @Bean
    YearMonth yearMonth() {
        return YearMonth.now();
    }
}
