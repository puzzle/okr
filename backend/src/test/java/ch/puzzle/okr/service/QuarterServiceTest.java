package ch.puzzle.okr.service;

import ch.puzzle.okr.models.Measure;
import ch.puzzle.okr.models.Quarter;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.QuarterRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;

@ExtendWith(MockitoExtension.class)
class QuarterServiceTest {
    @MockBean
    QuarterRepository quarterRepository = Mockito.mock(QuarterRepository.class);

    @InjectMocks
    private QuarterService quarterService;

    @Test
    void shouldReturnProperQuarter() {
        Quarter quarter = Quarter.Builder
                .builder().withId(3L)
                .withNumber(3)
                .withYear(2022)
                .build();
        Mockito.when(this.quarterRepository.findById(anyLong())).thenReturn(Optional.of(quarter));
        Quarter objectQuarter = this.quarterService.getQuarterById(3L);
        assertEquals(3, objectQuarter.getNumber());
        assertEquals(2022, objectQuarter.getYear());
    }

    @Test
    void shouldThrowResponseException() {
        assertThrows(ResponseStatusException.class, () -> this.quarterService.getQuarterById(null));
    }

    @Test
    void shouldThrowExceptionBecauseOfNotFound() {
        Mockito.when(this.quarterRepository.findById(anyLong())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> {
           this.quarterService.getQuarterById(5L);
        });
    }
}
