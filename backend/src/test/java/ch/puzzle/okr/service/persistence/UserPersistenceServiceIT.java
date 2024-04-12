package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

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
    }

    @Test
    void shouldReturnAllUsersCorrect() throws ResponseStatusException {
        List<User> userList = userPersistenceService.findAll();

        Assertions.assertThat(userList.size()).isGreaterThanOrEqualTo(7);
    }

    @Test
    void shouldReturnSingleUserWhenFindingOwnerByValidId() {
        User returnedUser = userPersistenceService.findById(1L);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Paco", returnedUser.getFirstname());
        assertEquals("Eggimann", returnedUser.getLastname());
        assertEquals("peggimann@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenFindingOwnerNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userPersistenceService.findById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        assertEquals("MODEL_WITH_ID_NOT_FOUND", exception.getReason());
    }

    @Test
    void shouldThrowExceptionWhenFindingOwnerWithNullId() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userPersistenceService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("ATTRIBUTE_NULL", exception.getReason());
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
        User newUser = User.Builder.builder().withId(null).withFirstname("firstname").withLastname("lastname")
                .withEmail("lastname@puzzle.ch").build();

        createdUser = userPersistenceService.getOrCreateUser(newUser);

        assertNotNull(createdUser.getId());
        assertEquals("firstname", createdUser.getFirstname());
        assertEquals("lastname", createdUser.getLastname());
        assertEquals("lastname@puzzle.ch", createdUser.getEmail());
    }
}