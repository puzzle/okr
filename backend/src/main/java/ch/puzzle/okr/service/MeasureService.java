package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.repository.MeasureRepository;
import org.springframework.stereotype.Service;

@Service
public class MeasureService {

    MeasureRepository measureRepository;

    public MeasureService(MeasureRepository measureRepository){this.measureRepository = measureRepository;}

    public Measure saveMeasure(Measure measure){
        return measureRepository.save(measure);
    }
}
