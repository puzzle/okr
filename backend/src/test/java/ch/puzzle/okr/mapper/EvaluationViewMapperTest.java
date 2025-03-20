package ch.puzzle.okr.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;

import ch.puzzle.okr.dto.EvaluationDto;
import ch.puzzle.okr.models.evaluation.EvaluationView;
import ch.puzzle.okr.models.evaluation.EvaluationViewId;
import ch.puzzle.okr.test.EvaluationViewTestHelper;
import java.util.List;
import java.util.stream.Stream;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EvaluationViewMapperTest {
    @InjectMocks
    private EvaluationViewMapper mapper;

    private static Stream<Arguments> fromDtoArgs() {
        return Stream
                .of(Arguments
                        .of(List.of(1L, 2L, 3L),
                            3L,
                            List
                                    .of(new EvaluationViewId(1L, 3L),
                                        new EvaluationViewId(2L, 3L),
                                        new EvaluationViewId(3L, 3L))),
                    Arguments.of(List.of(), 3L, List.of()),
                    Arguments.of(List.of(7L), 5L, List.of(new EvaluationViewId(7L, 5L))),
                    Arguments
                            .of(List.of(10L, 20L),
                                2L,
                                List.of(new EvaluationViewId(10L, 2L), new EvaluationViewId(20L, 2L))));
    }

    private static Stream<Arguments> toDtoArge() {
        EvaluationViewId id = new EvaluationViewId(1L, 3L);
        List<Integer> data1 = List.of(1, 3, 5, 7, 9, 11, 13, 15, 17, 19, 21);
        List<Integer> data2 = List.of(0, 2, 4, 6, 8, 10, 12, 14, 16, 18, 20);
        List<Integer> data3 = List.of(1, 4, 7, 10, 13, 16, 19, 22, 25, 28, 31);
        EvaluationView evaluationView1 = EvaluationViewTestHelper.createEvaluationView(id, data1);
        EvaluationView evaluationView2 = EvaluationViewTestHelper.createEvaluationView(id, data2);
        EvaluationView evaluationView3 = EvaluationViewTestHelper.createEvaluationView(id, data3);

        EvaluationDto evaluationDto1 = EvaluationViewTestHelper.generateEvaluationDto(data1);

        List<Integer> evaluationDtoData12 = List.of(1, 5, 9, 13, 17, 21, 25, 29, 33, 37, 41);
        EvaluationDto evaluationDto12 = EvaluationViewTestHelper.generateEvaluationDto(evaluationDtoData12);

        List<Integer> evaluationDtoData123 = List.of(2, 9, 16, 23, 30, 37, 44, 51, 58, 65, 72);
        EvaluationDto evaluationDto123 = EvaluationViewTestHelper.generateEvaluationDto(evaluationDtoData123);
        return Stream
                .of(Arguments.of(evaluationDto1, List.of(evaluationView1)),
                    Arguments.of(evaluationDto12, List.of(evaluationView1, evaluationView2)),
                    Arguments.of(evaluationDto123, List.of(evaluationView1, evaluationView2, evaluationView3)));
    }

    @ParameterizedTest
    @MethodSource("fromDtoArgs")
    void shouldMapTeamsIdsAndQuarterToEvaluationViewIds(List<Long> teamIds, Long quarterId,
                                                        List<EvaluationViewId> evaluationViewIds) {
        var result = mapper.fromDto(teamIds, quarterId);
        assertEquals(evaluationViewIds, result);
    }

    @ParameterizedTest
    @MethodSource("toDtoArge")
    void shouldEvaluationViewsToDto(EvaluationDto dto, List<EvaluationView> entities) {
        var result = mapper.toDto(entities);
        assertEquals(dto, result);
    }
}
