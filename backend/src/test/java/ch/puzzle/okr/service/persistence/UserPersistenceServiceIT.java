package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringIntegrationTest
class UserPersistenceServiceIT {

    private static final String EMAIL_ALICE = "wunderland@puzzle.ch";

    User createdUser;

    @Autowired
    private UserPersistenceService userPersistenceService;

    @BeforeEach
    void setUp() {
        TenantContext.setCurrentTenant("pitc");
    }

    @AfterEach
    void tearDown() {
        if (createdUser != null) {
            userPersistenceService.deleteById(createdUser.getId());
            createdUser = null;
        }
        TenantContext.setCurrentTenant(null);
    }

    @Test
    void getOrCreateUserShouldReturnSingleUserWhenUserFound() {
        User existingUser = User.Builder.builder().withEmail(EMAIL_ALICE).build();

        User returnedUser = userPersistenceService.getOrCreateUser(existingUser);

        assertEquals(11L, returnedUser.getId());
        assertEquals("Alice", returnedUser.getFirstname());
        assertEquals("Wunderland", returnedUser.getLastname());
        assertEquals("wunderland@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void getOrCreateUserShouldReturnSavedUserWhenUserNotFound() {
        User newUser = User.Builder.builder() //
                .withId(null) //
                .withFirstname("firstname") //
                .withLastname("lastname") //
                .withEmail("lastname@puzzle.ch") //
                .build();

        createdUser = userPersistenceService.getOrCreateUser(newUser);

        assertNotNull(createdUser.getId());
        assertEquals("firstname", createdUser.getFirstname());
        assertEquals("lastname", createdUser.getLastname());
        assertEquals("lastname@puzzle.ch", createdUser.getEmail());
    }
}