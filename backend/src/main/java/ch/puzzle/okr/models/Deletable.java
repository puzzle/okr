package ch.puzzle.okr.models;

import jakarta.persistence.*;
import org.hibernate.annotations.ColumnDefault;

@MappedSuperclass
public abstract class Deletable {
    @Column(name = "is_deleted", nullable = false)
    @ColumnDefault("false")
    private boolean isDeleted = false;

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}
