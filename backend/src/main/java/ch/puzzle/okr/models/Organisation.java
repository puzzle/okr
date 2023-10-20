package ch.puzzle.okr.models;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Organisation {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "sequence_organisation")
    private Long id;
}
