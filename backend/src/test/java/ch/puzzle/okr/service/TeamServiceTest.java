package ch.puzzle.okr.service;

import ch.puzzle.okr.common.BusinessException;
import ch.puzzle.okr.models.Team;
import ch.puzzle.okr.repository.TeamRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
class TeamServiceTest {
    @MockBean
    TeamRepository teamRepository = Mockito.mock(TeamRepository.class);

    @InjectMocks
    private TeamService teamService;

    @BeforeEach
    void setUp() {
        Team teamPuzzle = Team.Builder.builder()
                .withId(5L).
                withName("Puzzle")
                .build();
        Mockito.when(teamRepository.findById(5L)).thenReturn(Optional.of(teamPuzzle));
        Mockito.when(teamRepository.findById(6L)).thenReturn(Optional.empty());
    }

    @Test
    void shouldGetTheTeam() throws BusinessException {
        Team team = teamService.getTeamById(5);
        Assertions.assertThat(team.getName()).isEqualTo("Puzzle");
    }

    @Test
    void shouldNotFindTheTeam() {
        BusinessException exception = assertThrows(BusinessException.class, () -> teamService.getTeamById(6));
        assertEquals(404, exception.getCode());
        assertEquals("Team with id 6 not found", exception.getMessage());
    }
}