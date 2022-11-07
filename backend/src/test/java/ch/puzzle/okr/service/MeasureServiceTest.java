package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.MeasureRepository;
import ch.puzzle.okr.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class MeasureServiceTest {
    @MockBean
    MeasureRepository measureRepository = Mockito.mock(MeasureRepository.class);

    @InjectMocks
    private MeasureService measureService;

    private Measure measure;
    private Measure falseMeasure;

    @BeforeEach
    void setUp() {
       this.measure = Measure.Builder.builder()
                .withCreatedBy(User.Builder.builder().withId(1L).withFirstname("Frank").build())
                .withCreatedOn(LocalDateTime.MAX)
                .withKeyResult(KeyResult.Builder.builder().withId(8L).withBasisValue(12).withTargetValue(50).build())
                .withValue(30)
                .withChangeInfo("ChangeInfo")
                .withInitiatives("Initiatives")
                .build();
       this.falseMeasure = Measure.Builder.builder()
               .withId(3L)
               .build();
    }

    @Test
    void shouldReturnProperly() {
        Mockito.when(measureRepository.save(any())).thenReturn(measure);
        Measure returnMeasure = this.measureService.saveMeasure(measure);
        assertEquals(returnMeasure.getValue(), 30);
        assertEquals(returnMeasure.getChangeInfo(), "ChangeInfo");
        assertEquals(returnMeasure.getInitiatives(), "Initiatives");
        assertEquals(returnMeasure.getKeyResult().getId(), 8);
        assertEquals(returnMeasure.getCreatedBy().getFirstname(), "Frank");
    }

    @Test
    void shouldNotReturnProperly(){
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> measureService.saveMeasure(falseMeasure));
        assertEquals(400, exception.getRawStatusCode());
        assertEquals("Measure has already an Id", exception.getReason());
    }
}

