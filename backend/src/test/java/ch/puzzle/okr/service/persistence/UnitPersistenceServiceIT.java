package ch.puzzle.okr.service.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import ch.puzzle.okr.models.Unit;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import ch.puzzle.okr.test.WithMockAuthUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

@SpringIntegrationTest
class UnitPersistenceServiceIT {

    @Autowired
    private UnitPersistenceService unitPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("pitc");
    }

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
        assertUser("Mocked", "User", "gl@gl.com", createdUnit.getCreatedBy());
        unitPersistenceService.deleteById(createdUnit.getId());
    }

    //
    private void assertUser(String firstName, String lastName, String email, User currentUser) {
        assertEquals(firstName, currentUser.getFirstName());
        assertEquals(lastName, currentUser.getLastName());
        assertEquals(email, currentUser.getEmail());
    }
}