package ch.puzzle.okr.service.business;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import ch.puzzle.okr.exception.OkrResponseStatusException;
import ch.puzzle.okr.models.alignment.Alignment;
import ch.puzzle.okr.models.alignment.KeyResultAlignment;
import ch.puzzle.okr.models.keyresult.KeyResult;
import ch.puzzle.okr.models.keyresult.KeyResultMetric;
import ch.puzzle.okr.service.persistence.AlignmentPersistenceService;
import ch.puzzle.okr.service.validation.AlignmentValidationService;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

@ExtendWith(MockitoExtension.class)
class AlignmentBusinessServiceTest {
    @MockitoBean
    AlignmentPersistenceService alignmentPersistenceService = mock(AlignmentPersistenceService.class);
    @MockitoBean
    AlignmentValidationService alignmentValidationService = mock(AlignmentValidationService.class);

    KeyResult keyResult;
    List<KeyResultAlignment> alignments;

    @InjectMocks
    AlignmentBusinessService alignmentBusinessService;

    @BeforeEach
    void setUp() {
        this.keyResult = KeyResultMetric.Builder
                .builder()
                .withId(1L)
                .withBaseline(10.0)
                .withDescription("Awesome Keyresult")
                .withStretchGoal(100.0)
                .build();

        this.alignments = new ArrayList<>();
        this.alignments.add(KeyResultAlignment.Builder.builder().withId(12L).withTargetKeyResult(keyResult).build());
        this.alignments.add(KeyResultAlignment.Builder.builder().withId(132L).withTargetKeyResult(keyResult).build());
        this.alignments.add(KeyResultAlignment.Builder.builder().withId(9L).withTargetKeyResult(keyResult).build());
    }

    @DisplayName("Should throw exception when validation fails")
    @Test
    void shouldThrowExceptionWhenValidationFailsUsingUpdateEntity() {
        doThrow(new OkrResponseStatusException(HttpStatus.BAD_REQUEST, "Error Message"))
                .when(alignmentValidationService)
                .validateOnUpdate(eq(1L), any(KeyResultAlignment.class));

        KeyResultAlignment keyResultAlignment = new KeyResultAlignment();
        assertThrows(OkrResponseStatusException.class,
                     () -> alignmentBusinessService.updateEntity(1L, keyResultAlignment));
    }

    @DisplayName("Should save new entity when updating entity")
    @Test
    void shouldSaveNewEntityUsingUpdateEntity() {
        Alignment mockedAlignment = mock(Alignment.class);
        when(alignmentPersistenceService.save(any(Alignment.class))).thenAnswer(i -> i.getArguments()[0]);

        Alignment alignment = alignmentBusinessService.updateEntity(1L, mockedAlignment);

        verify(alignmentPersistenceService, times(1)).save(mockedAlignment);
        assertEquals(mockedAlignment, alignment);
    }

    @DisplayName("Should update key-result")
    @Test
    void shouldUpdateKeyResult() {
        KeyResult mockedKeyresult = mock(KeyResult.class);
        when(alignmentPersistenceService.findByKeyResultAlignmentId(1L)).thenReturn(this.alignments);

        alignmentBusinessService.updateKeyResultId(1L, mockedKeyresult);

        ArgumentCaptor<KeyResultAlignment> captor = ArgumentCaptor.forClass(KeyResultAlignment.class);
        verify(alignmentPersistenceService, times(3)).save(captor.capture());
        captor.getAllValues().forEach(c -> assertEquals(c.getAlignmentTarget(), mockedKeyresult));
    }

    @DisplayName("Should update nothing if no key-results are found")
    @Test
    void shouldUpdateNothingIfNoKeyResultAreFound() {
        KeyResult mockedKeyresult = mock(KeyResult.class);
        when(alignmentPersistenceService.findByKeyResultAlignmentId(1L)).thenReturn(List.of());

        alignmentBusinessService.updateKeyResultId(1L, mockedKeyresult);

        verify(alignmentPersistenceService, never()).save(any());
    }
}