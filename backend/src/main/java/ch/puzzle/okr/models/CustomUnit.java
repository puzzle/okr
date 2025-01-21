package ch.puzzle.okr.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class CustomUnit {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_action")
    @SequenceGenerator(name = "sequence_action", allocationSize = 1)
    private Long id;

    @Version
    private int version;

    @NotNull(message = MessageKey.ATTRIBUTE_NOT_NULL)
    @Size(max = 4096, min = 3, message = MessageKey.ATTRIBUTE_SIZE_BETWEEN)
    private String unitName;


    public CustomUnit() {
    }

    private CustomUnit(Builder builder) {
        id = builder.id;
    }


    public static final class Builder {
        private Long id;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder id(Long val) {
            id = val;
            return this;
        }

        public CustomUnit build() {
            return new CustomUnit(this);
        }
    }
}