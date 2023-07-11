package ch.puzzle.okr.dto;

import java.time.LocalDate;

public record StartEndDateDTO(LocalDate startDate, LocalDate endDate) {

    private StartEndDateDTO(Builder builder) {
        this(builder.startDate, builder.endDate);
    }

    public static final class Builder {
        private LocalDate startDate;
        private LocalDate endDate;

        private Builder() {
        }

        public static Builder builder() {
            return new Builder();
        }

        public Builder withStartDate(LocalDate startDate) {
            this.startDate = startDate;
            return this;
        }

        public Builder withEndDate(LocalDate endDate) {
            this.endDate = endDate;
            return this;
        }

        public StartEndDateDTO build() {
            return new StartEndDateDTO(this);
        }
    }
}
