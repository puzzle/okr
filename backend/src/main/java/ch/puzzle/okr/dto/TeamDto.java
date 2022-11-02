package ch.puzzle.okr.dto;

import javax.validation.constraints.NotNull;

public class TeamDto {
    private Long id;
    @NotNull
    private String name;

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
