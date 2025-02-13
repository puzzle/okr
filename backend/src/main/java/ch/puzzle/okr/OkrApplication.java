package ch.puzzle.okr;

import ch.puzzle.okr.models.Deletable;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class OkrApplication {
    public static void main(String[] args) {

        new SpringApplicationBuilder(OkrApplication.class) //
                .initializers(new OkrApplicationContextInitializer()) //
                .run(args);
    }

    public static <E> Specification<E> isNotMarkedAsDeleted() {
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.isFalse(root.get("isDeleted"));
    }
}
