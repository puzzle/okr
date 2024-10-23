package ch.puzzle.okr.service.persistence;

import ch.puzzle.okr.OkrApplicationContextInitializer;
import ch.puzzle.okr.models.User;
import ch.puzzle.okr.multitenancy.TenantContext;
import ch.puzzle.okr.test.SpringIntegrationTest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static ch.puzzle.okr.Constants.USER;
import static ch.puzzle.okr.util.CollectionUtils.iterableToList;
import static org.junit.jupiter.api.Assertions.*;

@SpringIntegrationTest
class UserPersistenceServiceIT {

    private static final Logger logger = LoggerFactory.getLogger(UserPersistenceServiceIT.class);

    private User createdUser;

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

    @DisplayName("save() should save user with empty user team list")
    @Test
    void saveShouldSaveUserWithEmptyUserTeamList() {
        // arrange
        var newUser = User.Builder.builder() //
                .withFirstname("Hans") //
                .withLastname("Muster") //
                .withEmail("muster@puzzle.ch") //
                .withUserTeamList(List.of()).build();

        // act
        createdUser = userPersistenceService.save(newUser);

        // assert
        assertNotNull(createdUser.getId());
        assertUser("Hans", "Muster", "muster@puzzle.ch", createdUser);
    }

    @DisplayName("save() should save user with null value for user team list")
    @Test
    void saveShouldSaveUserWithNullUserTeamList() {
        // arrange
        var newUser = User.Builder.builder() //
                .withFirstname("Hans") //
                .withLastname("Muster") //
                .withEmail("muster@puzzle.ch") //
                .withUserTeamList(null).build();

        // act
        createdUser = userPersistenceService.save(newUser);

        // assert
        assertNotNull(createdUser.getId());
        assertUser("Hans", "Muster", "muster@puzzle.ch", createdUser);
    }

    @DisplayName("saveAll() should save all users in the input list")
    @Test
    void saveAllShouldSaveAllUsersInTheInputList() {
        // arrange
        var newUser = User.Builder.builder() //
                .withFirstname("Hans") //
                .withLastname("Muster") //
                .withEmail("muster@puzzle.ch") //
                .build();

        // act
        var createdUsers = iterableToList(userPersistenceService.saveAll(List.of(newUser)));

        // assert
        assertEquals(1, createdUsers.size());
        createdUser = createdUsers.get(0);

        assertNotNull(createdUser.getId());
        assertUser("Hans", "Muster", "muster@puzzle.ch", createdUser);
    }

    @DisplayName("getOrCreateUser() should return single user when user found")
    @Test
    void getOrCreateUserShouldReturnSingleUserWhenUserFound() {
        // arrange
        var existingUser = User.Builder.builder().withEmail("wunderland@puzzle.ch").build();

        // act
        var returnedUser = userPersistenceService.getOrCreateUser(existingUser);

        // assert
        assertUser(11L, "Alice", "Wunderland", "wunderland@puzzle.ch", returnedUser);
    }

    @DisplayName("getOrCreateUser() should return saved user when user not found")
    @Test
    void getOrCreateUserShouldReturnSavedUserWhenUserNotFound() {
        // arrange
        var newUser = User.Builder.builder() //
                .withId(null) //
                .withFirstname("firstname") //
                .withLastname("lastname") //
                .withEmail("lastname@puzzle.ch") //
                .build();

        // act
        createdUser = userPersistenceService.getOrCreateUser(newUser);

        // assert
        assertNotNull(createdUser.getId());
        assertUser("firstname", "lastname", "lastname@puzzle.ch", createdUser);
    }

    // uses data from V100_0_0__TestData.sql
    @DisplayName("findByEmail() should return user if email is found")
    @Test
    void findByEmailShouldReturnUserIfEmailIsFound() {
        Optional<User> user = userPersistenceService.findByEmail("gl@gl.com");

        assertTrue(user.isPresent());
        assertEquals("Jaya", user.get().getFirstname());
        assertEquals("Norris", user.get().getLastname());
    }

    @DisplayName("findByEmail() should return empty optional if email is not found")
    @Test
    void findByEmailShouldReturnEmptyOptionalIfEmailIsNotFound() {
        assertTrue(userPersistenceService.findByEmail("not_valid@gl.com").isEmpty());
    }

    @DisplayName("findByEmail() should return empty optional if email is null")
    @Test
    void findByEmailShouldReturnEmptyOptionalIfEmailIsNull() {
        assertTrue(userPersistenceService.findByEmail(null).isEmpty());
    }

    // uses data from V100_0_0__TestData.sql
    @DisplayName("findAllOkrChampions() should return all okr champions")
    @Test
    void findAllOkrChampionsShouldReturnAllOkrChampions() {
        var allOkrChampions = userPersistenceService.findAllOkrChampions();
        allOkrChampions.forEach(user -> logger.warn(user.toString()));

        assertEquals(1, allOkrChampions.size());
        assertEquals("Jaya", allOkrChampions.get(0).getFirstname());
        assertEquals("Norris", allOkrChampions.get(0).getLastname());
    }

    @DisplayName("getModelName() should return user")
    @Test
    void getModelNameShouldReturnUser() {
        assertEquals(USER, userPersistenceService.getModelName());
    }

    private void assertUser(Long id, String firstName, String lastName, String email, User currentUser) {
        assertEquals(id, currentUser.getId());
        assertUser(firstName, lastName, email, currentUser);
    }

    private void assertUser(String firstName, String lastName, String email, User currentUser) {
        assertEquals(firstName, currentUser.getFirstname());
        assertEquals(lastName, currentUser.getLastname());
        assertEquals(email, currentUser.getEmail());
    }
}