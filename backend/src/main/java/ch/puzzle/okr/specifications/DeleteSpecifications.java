package ch.puzzle.okr.specifications;

import org.springframework.data.jpa.domain.Specification;

public class DeleteSpecifications {
    private DeleteSpecifications() {
    }

    public static <E> Specification<E> isNotMarkedAsDeleted() {
        return (root, query, criteriaBuilder) -> criteriaBuilder.isFalse(root.get("isDeleted"));
    }

}
