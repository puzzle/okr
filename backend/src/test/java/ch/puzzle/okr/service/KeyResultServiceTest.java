package ch.puzzle.okr.service;

import ch.puzzle.okr.dto.KeyResultDto;
import ch.puzzle.okr.mapper.KeyResultMapper;
import ch.puzzle.okr.models.*;
import ch.puzzle.okr.repository.KeyResultRepository;
import ch.puzzle.okr.repository.*;
import org.checkerframework.checker.units.qual.K;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.postgresql.hostchooser.HostRequirement.any;

@ExtendWith(MockitoExtension.class)
@ExtendWith(SpringExtension.class)
@SpringBootTest
@ContextConfiguration(classes = KeyResultServiceTest.class)
class KeyResultServiceTest {

    @Mock
    KeyResultRepository keyResultRepository;
    @Mock
    KeyResultMapper keyResultMapper;

    @InjectMocks
    private KeyResultService keyResultService;

    private KeyResultDto keyResultDTO;
    private User user;
    private Objective objective;
    private Quarter quarter;
    private KeyResult keyResult;
    List<KeyResult> keyResults;

    @BeforeEach
    void setup() {
        this.user = User.Builder.builder()
                .withId(1L)
                .withEmail("newMail@tese.com")
                .build();

        this.objective = Objective.Builder.builder()
                .withId(5L)
                .withTitle("Objective 1")
                .build();

        this.quarter = Quarter.Builder.builder()
                .withId(5L)
                .withNumber(2)
                .withYear(2022)
                .build();

        this.keyResult = KeyResult.Builder.builder()
                .withId(5L)
                .withTitle("Keyresult 1")
                .withObjective(this.objective)
                .withOwner(this.user)
                .withQuarter(this.quarter)
                .build();

        this.keyResultDTO = new KeyResultDto(5L, 5L, "", "", 5L, "", "", 5L, 2, 2022, ExpectedEvolution.INCREASE, Unit.PERCENT, 0L, 1L);
        this.keyResults = List.of(keyResult, keyResult, keyResult);
    }


    @Test
    void shouldGetKeyResultById() {
        when(keyResultRepository.findById(1L)).thenReturn(Optional.of(keyResult));
        KeyResult keyResult = keyResultService.getKeyResultById(1);

        assertEquals("Keyresult 1", keyResult.getTitle());
        assertEquals(5, keyResult.getId());
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

    @Test
    void createKeyResult() {
        Mockito.when(this.keyResultRepository.save(any())).thenReturn(this.keyResult);
        KeyResult keyResult = this.keyResultService.createKeyResult(this.keyResult);
        assertEquals("Keyresult 1", keyResult.getTitle());
    }
}
