package ch.puzzle.okr.controller;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.MeasureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/measures")
public class MeasureController {

    @Autowired
    MeasureRepository measureRepository;

    @GetMapping
    public List<Measure> getAllTeams() {
        return (List<Measure>) measureRepository.findAll();
    }
}
