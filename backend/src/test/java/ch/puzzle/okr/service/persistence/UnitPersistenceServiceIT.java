package ch.puzzle.okr.service.persistence;

import static org.junit.jupiter.api.Assertions.*;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.repository.UnitRepository;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.WithMockAuthUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoSpyBean;

@SpringIntegrationTest
class UnitPersistenceServiceIT {
    @MockitoSpyBean
    private UnitRepository unitRepository;
    @Autowired
    private UnitPersistenceService unitPersistenceService;

    @DisplayName("Should save user on save() with empty user team list")
    @Test
    @WithMockAuthUser
    void saveShouldSaveUserWithEmptyUserTeamList() {
        // arrange
        var newUnit = Unit.Builder.builder().unitName("Muster").build();

        // act
        var createdUnit = unitPersistenceService.save(newUnit);

        // assert
        assertNotNull(createdUnit.getId());
        assertUser("Jaya", "Norris", "gl@gl.com", createdUnit.getCreatedBy());
        unitPersistenceService.deleteById(createdUnit.getId());
    }

    @DisplayName("Should mark as deleted on deleteById() per default")
    @Test
    @WithMockAuthUser
    void shouldMarkAsDeletedOnMethodCall() {
        // arrange
        var entity = Unit.Builder.builder().unitName("Muster").build();
        var newEntity = unitPersistenceService.save(entity);

        long entityId = newEntity.getId();

        // act
        unitPersistenceService.deleteById(entityId);

        // assert
        assertTrue(unitPersistenceService.findById(entityId).isDeleted());

        Mockito.verify(unitRepository, Mockito.times(1)).markAsDeleted(entityId);
    }

    //
    private void assertUser(String firstName, String lastName, String email, User currentUser) {
        assertEquals(firstName, currentUser.getFirstName());
        assertEquals(lastName, currentUser.getLastName());
        assertEquals(email, currentUser.getEmail());
    }
}