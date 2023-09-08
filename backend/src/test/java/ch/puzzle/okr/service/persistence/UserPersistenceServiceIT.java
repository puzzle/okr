package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.Constants;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class UserPersistenceServiceIT {
    User createdUser;
    @Autowired
    private UserPersistenceService userPersistenceService;
    @Autowired
    private CacheManager cacheManager;
    private Cache cache;

    @BeforeEach
    void beforeEach() {
        cache = cacheManager.getCache(Constants.USER_CACHE);
    }

    @AfterEach
    void tearDown() {
        if (createdUser != null) {
            userPersistenceService.deleteById(createdUser.getId());
            createdUser = null;
        }
        cache.clear();
    }

    @Test
    void shouldReturnAllUsersCorrect() throws ResponseStatusException {
        List<User> userList = userPersistenceService.findAll();

        Assertions.assertThat(userList).hasSize(6);
    }

    @Test
    void shouldReturnSingleUserWhenFindingOwnerByValidId() {
        User returnedUser = userPersistenceService.findById(1L);

        assertEquals(1L, returnedUser.getId());
        assertEquals("Paco", returnedUser.getFirstname());
        assertEquals("Eggimann", returnedUser.getLastname());
        assertEquals("peggimann", returnedUser.getUsername());
        assertEquals("peggimann@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void shouldThrowExceptionWhenFindingOwnerNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userPersistenceService.findById(321L));

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("User with id 321 not found", exception.getReason());
    }

    @Test
    void shouldThrowExceptionWhenFindingOwnerWithNullId() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userPersistenceService.findById(null));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Missing identifier for User", exception.getReason());
    }

    @Test
    void findUserByUsername_ShouldReturnExistingUser() {
        User returnedUser = userPersistenceService.findUserByUsername("alice");

        assertEquals(11L, returnedUser.getId());
        assertEquals("Alice", returnedUser.getFirstname());
        assertEquals("Wunderland", returnedUser.getLastname());
        assertEquals("alice", returnedUser.getUsername());
        assertEquals("wunderland@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void findUserByUsername_ShouldThrowExceptionWhenUserNotFound() {
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> userPersistenceService.findUserByUsername("unknown"));

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("User with username unknown not found", exception.getReason());
    }

    @Test
    void findUserByUsername_ShouldAddUserToCache() {
        userPersistenceService.findUserByUsername("alice");

        User cachedUser = cacheManager.getCache(Constants.USER_CACHE).get("alice", User.class);
        assertNotNull(cachedUser);
    }

    @Test
    void getOrCreateUser_ShouldReturnSingleUserWhenUserFound() {
        User existingUser = User.Builder.builder().withUsername("alice").build();

        User returnedUser = userPersistenceService.getOrCreateUser(existingUser);

        assertEquals(11L, returnedUser.getId());
        assertEquals("Alice", returnedUser.getFirstname());
        assertEquals("Wunderland", returnedUser.getLastname());
        assertEquals("alice", returnedUser.getUsername());
        assertEquals("wunderland@puzzle.ch", returnedUser.getEmail());
    }

    @Test
    void getOrCreateUser_ShouldReturnSavedUserWhenUserNotFound() {
        User newUser = User.Builder.builder().withId(null).withFirstname("firstname").withLastname("lastname")
                .withUsername("username").withEmail("lastname@puzzle.ch").build();

        createdUser = userPersistenceService.getOrCreateUser(newUser);

        assertNotNull(createdUser.getId());
        assertEquals("firstname", createdUser.getFirstname());
        assertEquals("lastname", createdUser.getLastname());
        assertEquals("username", createdUser.getUsername());
        assertEquals("lastname@puzzle.ch", createdUser.getEmail());
    }

    @Test
    void getOrCreateUser_ShouldAddUserToCache() {
        User existingUser = User.Builder.builder().withUsername("alice").build();
        userPersistenceService.getOrCreateUser(existingUser);

        User cachedUser = cacheManager.getCache(Constants.USER_CACHE).get("alice", User.class);
        assertNotNull(cachedUser);
    }
}