package ch.puzzle.okr.dto;

import java.io.Serial;
import java.io.Serializable;

public class TeamDto implements Serializable {
    @Serial
    private static final long serialVersionUID = -4405923832408766849L;
    private Long id;
    private String name;

    public TeamDto() {
    }

    public TeamDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
