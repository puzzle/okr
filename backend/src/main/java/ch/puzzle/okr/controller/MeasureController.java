package ch.puzzle.okr.controller;

import ch.puzzle.okr.dto.MeasureDto;
import ch.puzzle.okr.mapper.MeasureMapper;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.MeasureRepository;
import ch.puzzle.okr.service.MeasureService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/measures")
public class MeasureController {
    MeasureRepository measureRepository;
    private final MeasureMapper measureMapper;
    private final MeasureService measureService;

    public MeasureController(MeasureMapper measureMapper, MeasureService measureService) {
        this.measureMapper = measureMapper;
        this.measureService = measureService;
    }

    @GetMapping
    public List<Measure> getAllMeasures() {
        return (List<Measure>) measureRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Object> createMeasure(@Valid @RequestBody MeasureDto measureDto){
        try{
            Measure measure = measureMapper.toMeasure(measureDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(measureService.saveMeasure(measure));
        }catch (ResponseStatusException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to create measure =>" + e.getMessage());
        }
    }

}
