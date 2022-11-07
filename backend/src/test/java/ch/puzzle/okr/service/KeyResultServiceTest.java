package ch.puzzle.okr.service;

import ch.puzzle.okr.models.KeyResult;
import ch.puzzle.okr.repository.KeyResultRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class KeyResultServiceTest {
    @MockBean
    KeyResultRepository keyResultRepository = Mockito.mock(KeyResultRepository.class);

    @InjectMocks
    private KeyResultService keyResultService;

    KeyResult keyResult;
    List<KeyResult> keyResults;

    @BeforeEach
    void setUp() {
        this.keyResult = KeyResult.Builder.builder()
                .withId(1L)
                .withTitle("Keyresult 1")
                .build();
        this.keyResults = List.of(keyResult, keyResult, keyResult);
    }





    @Test
    void shouldGetKeyResultById() {
        when(keyResultRepository.findById(1L)).thenReturn(Optional.of(keyResult));

        KeyResult keyResult = keyResultService.getKeyResultById(1);

        assertEquals("Keyresult 1", keyResult.getTitle());
        assertEquals(1, keyResult.getId());
    }

    @Test
    void shouldThrowExceptionWhenKeyResultDoesntExist() {
        assertThrows(ResponseStatusException.class, () ->
                keyResultService.getKeyResultById(1));
    }

    @Test
    void shouldEditKeyresult() {
        KeyResult newKeyresult = KeyResult.Builder
                .builder()
                .withId(1L).withTitle("Keyresult 1 update")
                .build();
        Mockito.when(keyResultRepository.save(any())).thenReturn(newKeyresult);
        Mockito.when(keyResultRepository.findById(1L)).thenReturn(Optional.of(keyResult));

        keyResultService.updateKeyResult(newKeyresult);
        assertEquals(1L, newKeyresult.getId());
        assertEquals("Keyresult 1 update", newKeyresult.getTitle());
    }

    @Test
    void shouldThrowErrorWhenKeyResultDoesntExistDuringPut() {
        Mockito.when(keyResultRepository.findById(any())).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> keyResultService.updateKeyResult(keyResult));
    }
}
