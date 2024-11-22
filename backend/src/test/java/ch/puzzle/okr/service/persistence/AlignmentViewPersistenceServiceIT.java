package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.alignment.AlignmentView;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.TestHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringIntegrationTest
class AlignmentViewPersistenceServiceIT {
    @Autowired
    private AlignmentViewPersistenceService alignmentViewPersistenceService;

    private static final List<Long> expectedAlignmentViewIds = List.of(40L, 41L, 42L, 43L, 44L, 45L);

    private static final List<Long> expectedAlignmentViewTeamIds = List.of(4L, 5L, 6L, 8L);

    private static final List<Long> expectedAlignmentViewQuarterId = List.of(9L);

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant(TestHelper.SCHEMA_PITC);
    }

    @Test
    void getAlignmentsByFiltersShouldReturnListOfAlignmentViews() {
        List<AlignmentView> alignmentViewList = alignmentViewPersistenceService.getAlignmentViewListByQuarterId(9L);

        assertEquals(10, alignmentViewList.size());

        assertThat(getAlignmentViewIds(alignmentViewList)).hasSameElementsAs(expectedAlignmentViewIds);
        assertThat(getAlignmentViewTeamIds(alignmentViewList)).hasSameElementsAs(expectedAlignmentViewTeamIds);
        assertThat(getAlignmentViewQuarterIds(alignmentViewList)).hasSameElementsAs(expectedAlignmentViewQuarterId);
    }

    @Test
    void getAlignmentsByFiltersShouldReturnEmptyListOfAlignmentViewsWhenQuarterNotExisting() {
        List<AlignmentView> alignmentViewList = alignmentViewPersistenceService.getAlignmentViewListByQuarterId(311L);

        assertEquals(0, alignmentViewList.size());
    }

    private List<Long> getAlignmentViewIds(List<AlignmentView> alignmentViewIds) {
        return alignmentViewIds.stream().map(AlignmentView::getId).toList();
    }

    private List<Long> getAlignmentViewTeamIds(List<AlignmentView> alignmentViewIds) {
        return alignmentViewIds.stream().map(AlignmentView::getTeamId).toList();
    }

    private List<Long> getAlignmentViewQuarterIds(List<AlignmentView> alignmentViewIds) {
        return alignmentViewIds.stream().map(AlignmentView::getQuarterId).toList();
    }
}
